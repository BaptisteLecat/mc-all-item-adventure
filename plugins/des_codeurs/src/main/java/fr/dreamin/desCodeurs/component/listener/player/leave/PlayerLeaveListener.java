package fr.dreamin.desCodeurs.component.listener.player.leave;

import fr.dreamin.api.colors.StringColor;
import fr.dreamin.desCodeurs.manager.player.PlayersManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    event.setQuitMessage(StringColor.WHITE.getColor() + "[" + StringColor.RED.getColor() + "-" + StringColor.WHITE.getColor() + "] " + StringColor.RED.getColor() + event.getPlayer().getName());

    if (PlayersManager.contains(event.getPlayer())) PlayersManager.getPlayer(event.getPlayer()).stopTick();

  }

}
