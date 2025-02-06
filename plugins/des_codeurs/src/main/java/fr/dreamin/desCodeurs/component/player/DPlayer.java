package fr.dreamin.desCodeurs.component.player;

import fr.dreamin.api.time.Tick;
import fr.dreamin.desCodeurs.component.player.hud.PScoreBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter @Setter
public class DPlayer extends Tick {

  private Player player;

  private final PScoreBoard scoreboard;

  public DPlayer(Player player) {
    this.player = player;

    this.scoreboard = new PScoreBoard(this);

    startTick();
  }

  @Override
  public void tick() {
    super.tick();

    if (this.scoreboard != null) {
      if (getActualTick() % 2 == 0) this.scoreboard.updateTitle();
      this.scoreboard.updateAll();
    }
  }

  public void setPlayer(Player player) {
    this.player = player;
    this.scoreboard.reload();
  }
}
