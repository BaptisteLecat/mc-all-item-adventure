package fr.dreamin.desCodeurs.component.listener.player.join;

import fr.dreamin.api.colors.StringColor;
import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.component.player.DPlayer;
import fr.dreamin.desCodeurs.manager.player.PlayersManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.setJoinMessage(StringColor.WHITE.getColor() + "[" + StringColor.GREEN.getColor() + "+" + StringColor.WHITE.getColor() + "] " + StringColor.GREEN.getColor() + event.getPlayer().getName());

    if (PlayersManager.contains(event.getPlayer().getName())) {
      DPlayer dPlayer = PlayersManager.getPlayer(event.getPlayer().getName());

      if (dPlayer == null) PlayersManager.getDPlayerSet().add(new DPlayer(event.getPlayer()));
      else {
        dPlayer.setPlayer(event.getPlayer());
        dPlayer.startTick();
      }
    }
    else PlayersManager.getDPlayerSet().add(new DPlayer(event.getPlayer()));
  }

}
