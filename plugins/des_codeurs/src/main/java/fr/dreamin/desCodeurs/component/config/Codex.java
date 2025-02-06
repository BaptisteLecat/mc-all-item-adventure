package fr.dreamin.desCodeurs.component.config;

import fr.dreamin.api.mysl.DatabaseManager;
import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.component.DItem;
import fr.dreamin.desCodeurs.component.player.hud.PScoreBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class Codex {

  private static final int DELAY_TO_LOAD = 20;

  private final JavaPlugin instance;

  private FileConfiguration config;

  //  >>>>>>> GENERAL <<<<<<<
  private String pluginName = "DesCodeurs", prefix, broadcastPrefix, packUrl;

  //  >>>>>>> NO-MATERIALS <<<<<<<
  private List<Material> materialsNotPossible = new ArrayList<>(), materials = new ArrayList<>(Arrays.asList(Material.values()));

  //  >>>>>>>> SQL <<<<<<<<
  private String host, dbName, username, password, defaultPrefix;
  private int port;
  private DatabaseManager database;

  //  >>>>>>>> REQUEST SQL <<<<<<<<
  private Location chestLocation = new Location(Bukkit.getWorld("world"), 28, 102, -9);


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
    initSQLData();
    initSQLRequest();

    this.instance.getLogger().info("Configuration Loaded");
  }

  public void reloadConfigFile() {
    this.instance.reloadConfig();
    this.config = this.instance.getConfig();
  }

  private void initGlobal() {
    this.prefix = config.getString("desCodeurs.prefix", "§8» §f");
    this.broadcastPrefix = config.getString("desCodeurs.broadcast-prefix", "[§c§lDesCodeurs§r] ");



    this.materialsNotPossible = config.getList("no-materials").stream().map(e -> Material.getMaterial((String) e)).toList();

    assert materialsNotPossible != null;
    this.materials.removeAll(materialsNotPossible);
  }

  public void initSQLData() {
    this.host = config.getString("sql.host");
    this.port = config.getInt("sql.port");
    this.dbName = config.getString("sql.dbName");
    this.username = config.getString("sql.username");
    this.password = config.getString("sql.password");
    this.defaultPrefix = config.getString("sql.defaultPrefix");

    this.database = new DatabaseManager("DesCodeurs", host, port, dbName, username, password);
  }

  public void initSQLRequest() {
    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

    }, DELAY_TO_LOAD);
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
  }


}
