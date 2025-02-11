package fr.dreamin.desCodeurs.component.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.dreamin.api.mysl.DatabaseManager;
import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.component.DItem;
import fr.dreamin.desCodeurs.component.player.hud.PScoreBoard;
import fr.dreamin.desCodeurs.component.team.Team;
import fr.dreamin.desCodeurs.manager.ChestManager;
import fr.dreamin.desCodeurs.utils.ApiRequest;
import fr.dreamin.desCodeurs.utils.GameUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.*;

@Getter @Setter
public class Codex {

  private static final int DELAY_TO_LOAD = 40;

  private final JavaPlugin instance;

  private FileConfiguration config;

  private Gson gson = new Gson();

  //  >>>>>>> GENERAL <<<<<<<
  private String pluginName = "DesCodeurs", prefix, broadcastPrefix, packUrl, gameId, apiUrl;

  //  >>>>>>> HARDCORE <<<<<<<
  private boolean hardcoreMode = false, hardcoreTeam = false;
  private Set<Team> teams = new HashSet<>();

  //  >>>>>>> NO-MATERIALS <<<<<<<
  private List<Material> materialsNotPossible = new ArrayList<>(), materials = new ArrayList<>(Arrays.asList(Material.values()));


  private HashMap<UUID, List<DItem>> materialsPlayers = new HashMap<>();


  public Codex(JavaPlugin instance) {
    this.instance = instance;
    this.config = instance.getConfig();

    refresh();
  }

  public void refresh() {
    reloadConfigFile();
    this.instance.getLogger().info("Configuration Loading...");

    initGlobal();
    initHardCore();

    this.instance.getLogger().info("Configuration Loaded");
  }

  public void reloadConfigFile() {
    this.instance.reloadConfig();
    this.config = this.instance.getConfig();
  }

  private void initGlobal() {
    this.prefix = config.getString("desCodeurs.prefix", "§8» §f");
    this.broadcastPrefix = config.getString("desCodeurs.broadcast-prefix", "[§c§lDesCodeurs§r] ");

    this.apiUrl = config.getString("apiUrl", "https://plugin-firebase-api-384868196694.europe-west9.run.app/");
    this.gameId = config.getString("gameId", null);

    if (this.gameId == null || this.gameId == "null") {
      //add gameId

      Location locA = new Location(Bukkit.getWorld("world"), -config.getInt("desCodeurs.chest.range.x", 50000), 0, -config.getInt("desCodeurs.chest.range.x", 50000));
      Location locB = new Location(Bukkit.getWorld("world"), config.getInt("desCodeurs.chest.range.x", 50000), 256, config.getInt("desCodeurs.chest.range.x", 50000));

      Location chestLocation = GameUtils.randomLocation(locA, locB);

      Bukkit.getScheduler().runTaskLater(this.instance, () -> {
        ChestManager.init(chestLocation);

        JsonObject chestCoord = new JsonObject();
        chestCoord.addProperty("x", chestLocation.getX());
        chestCoord.addProperty("y", chestLocation.getY());
        chestCoord.addProperty("z", chestLocation.getZ());


        JsonObject json = new JsonObject();

        json.addProperty("gameKey", "gameKey");
        json.add("chestCoord", chestCoord);
        json.add("scores", new JsonArray());

        String response = ApiRequest.post(this.apiUrl, json.toString());


        JsonObject result = gson.fromJson(response, JsonObject.class);
        config.set("gameId", result.get("id").getAsString());
        this.instance.saveConfig();

      }, DELAY_TO_LOAD);
    }
    else {
      //get game info
      String response = ApiRequest.get(this.apiUrl, this.gameId);

      System.out.println(response);

      JsonObject result = gson.fromJson(response, JsonObject.class);

      JsonObject chestCoord = result.get("chestCoord").getAsJsonObject();

      ChestManager.init(new Location(Bukkit.getWorld("world"), chestCoord.get("x").getAsDouble(), chestCoord.get("y").getAsDouble(), chestCoord.get("z").getAsDouble()));

      JsonArray scores = result.get("scores").getAsJsonArray();

      scores.forEach(score -> {
        JsonObject playerScore = score.getAsJsonObject();
        JsonObject timeStamp = playerScore.get("createdAt").getAsJsonObject();
        this.addMaterialFromPlayer(UUID.fromString(playerScore.get("uidPlayer").getAsString()), Material.getMaterial(playerScore.get("materialName").getAsString()), PScoreBoard.convertTimestampToLocalDateTime(timeStamp.get("_seconds").getAsLong()));

      });

    }

    this.materialsNotPossible = config.getList("no-materials").stream().map(e -> Material.getMaterial((String) e)).toList();

    assert materialsNotPossible != null;
    this.materials.removeAll(materialsNotPossible);
  }

  private void initHardCore() {
    this.hardcoreMode = config.getBoolean("hardcore-mode.enable", false);
    this.hardcoreTeam = config.getBoolean("hardcore-mode.team.enable", false);

    if (this.hardcoreTeam) this.teams.clear();
  }

  public void saveMaterialsNotPossible() {
    List<String> list = materialsNotPossible.stream().map(Material::name).toList();
    config.set("no-materials", list);
    instance.saveConfig();
  }

  public int getTotalIntFromMaterialsPlayers() {
    int total = 0;
    for (List<DItem> dItems : materialsPlayers.values()) {
      total += dItems.size();
    }
    return total;
  }

  public int getPercentageValidatedByPlayer(UUID playerUUID) {
    int totalMaterials = getTotalIntFromMaterialsPlayers();
    int validatedMaterials = materialsPlayers.getOrDefault(playerUUID, Collections.emptyList()).size();

    if (totalMaterials == 0) return 0;

    double percentage = (validatedMaterials / (double) totalMaterials) * 100;
    return (int) Math.floor(percentage);
  }

  public boolean containsMaterialFromPlayers(Material material) {
    for (List<DItem> dItems : this.materialsPlayers.values()) {
      List<Material> list = dItems.stream().map(DItem::getMaterial).toList();
      if (list.contains(material)) return true;
    }
    return false;
  }

  //function to return DItem from material, from materialsPlayers
  public Optional<DItem> getDItemFromMaterial(Material material) {
    for (List<DItem> dItems : this.materialsPlayers.values()) {
      for (DItem dItem : dItems) {
        if (dItem.getMaterial().equals(material)) return Optional.of(dItem);
      }
    }
    return Optional.empty();
  }

  public void addMaterialFromPlayer(UUID playerUUID, Material material) {
    this.materialsPlayers.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(new DItem(material, playerUUID, PScoreBoard.getCurrentDateTime()));
    if (getTotalIntFromMaterialsPlayers() == getMaterials().size()) Bukkit.broadcastMessage(getBroadcastPrefix() + "§c§lTous les matériaux ont été validés!");

    JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("uidPlayer", playerUUID.toString());
    jsonObject.addProperty("materialName", material.name());

    System.out.println(jsonObject);

    ApiRequest.post(this.apiUrl + "/" + this.gameId, jsonObject.toString());

  }

  public void addMaterialFromPlayer(UUID playerUUID, Material material, LocalDateTime date) {
    this.materialsPlayers.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(new DItem(material, playerUUID, PScoreBoard.formatDateTime(date)));

    if (getTotalIntFromMaterialsPlayers() == getMaterials().size()) Bukkit.broadcastMessage(getBroadcastPrefix() + "§c§lTous les matériaux ont été validés!");
  }


}
