package fr.dreamin.desCodeurs.component.player;

import fr.dreamin.api.bossBar.BarManager;
import fr.dreamin.api.colors.StringColor;
import fr.dreamin.api.math.MathUtils;
import fr.dreamin.api.time.Tick;
import fr.dreamin.desCodeurs.component.player.hud.PScoreBoard;
import fr.dreamin.desCodeurs.manager.ChestManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

@Getter @Setter
public class DPlayer extends Tick {

  private Player player;

  private final PScoreBoard scoreboard;
  private BarManager barManager;

  public DPlayer(Player player) {
    this.player = player;

    this.scoreboard = new PScoreBoard(this);
    setBarMananger(player);

    startTick();
  }

  @Override
  public void tick() {
    super.tick();

    if (this.player == null) return;

    if (this.barManager != null && ChestManager.getChest() != null) {
      Location chestLoc = ChestManager.getChest().getLocation();
      if (this.player.getWorld().equals(chestLoc.getWorld())) {
        if (!this.barManager.isShow()) this.barManager.showBar();
        this.barManager.setBarTitle(StringColor.GOLD.getColor() + "X: " + StringColor.GREEN.getColor() + chestLoc.getX() + StringColor.GOLD.getColor() + " | Y: " + StringColor.GREEN.getColor() + chestLoc.getY() + StringColor.GOLD.getColor() + " | Z: " + StringColor.GREEN.getColor() + chestLoc.getZ() + StringColor.GOLD.getColor() + " | distance : " + StringColor.GREEN.getColor() + MathUtils.getDirectionalArrow(player, chestLoc.clone().add(0.5, 0, 0.5)) + " " + Math.floor(this.player.getLocation().distance(chestLoc)) + "m");
      }
      else {
        if (this.barManager.isShow()) this.barManager.hideBar();
      }
    }

    if (this.scoreboard != null) {
      if (getActualTick() % 2 == 0) this.scoreboard.updateTitle();
      this.scoreboard.updateAll();
    }
  }

  public void setPlayer(Player player) {
    this.player = player;
    setBarMananger(player);
    this.scoreboard.reload();
  }

  private void setBarMananger(Player player) {
    if (this.barManager!= null) {
      this.barManager.hideBar();
    }
    this.barManager = new BarManager(player);
    this.barManager.setColor(BarColor.GREEN);
    this.barManager.showBar();
  }
}
