package fr.dreamin.desCodeurs.component.listener.player.dead;

import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.manager.player.PlayersManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeadListener implements Listener {

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    if (!PlayersManager.contains(event.getPlayer())) return;


    if (Main.getCodex().isHardcoreMode()) Main.getCodex().getMaterialsPlayers().get(event.getPlayer().getUniqueId()).clear();
  }

}
