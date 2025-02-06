package fr.dreamin.desCodeurs.component.player.hud;

import fr.dreamin.api.colors.StringColor;
import fr.dreamin.api.msg.GradientText;
import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.component.player.DPlayer;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PScoreBoard {

  private FastBoard db;
  private DPlayer dPlayer;
  private final GradientText gradientText = new GradientText("Des Codeurs", List.of("#7734ec", "#8a5bdc", "#9867ee"));

  public PScoreBoard(DPlayer dPlayer) {
    this.dPlayer = dPlayer;
    createSb();
  }

  public void updateAll() {
    List<String> sb = new ArrayList<>(List.of("§7"));

    sb.add(Main.getCodex().getTotalIntFromMaterialsPlayers() + "/" + Main.getCodex().getMaterials().size());

    if (!Main.getCodex().getMaterialsPlayers().isEmpty()) {
      sb.add(" ");
      Main.getCodex().getMaterialsPlayers().forEach((uuid, materials) -> {
        sb.add(Bukkit.getOfflinePlayer(uuid).getName() + ": " + materials.size() + " | " + Main.getCodex().getPercentageValidatedByPlayer(uuid) + "%");
      });
    }

    sb.addAll(getFooter());
    db.updateLines(sb);
  }

  public void updateTitle() {
    db.updateTitle(this.gradientText.getGradientText());
  }

  public void createSb() {
    this.db = new FastBoard(this.dPlayer.getPlayer());
    db.updateTitle(this.gradientText.getGradientText());
    updateAll();
  }

  public void removeSb() {
    if (this.db != null) {
      this.db.delete();
      this.db = null;
    }
  }

  public void reload() {
    removeSb();
    createSb();
  }

  public static String getCurrentDateTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    LocalDateTime now = LocalDateTime.now();
    return now.format(formatter);
  }

  private List<String> getFooter() {
    List<String> sb = new ArrayList<>();
    sb.add("§7");
    sb.add((StringColor.GRAY.colored("  " + getCurrentDateTime() + "  " )));
    return sb;
  }

}
