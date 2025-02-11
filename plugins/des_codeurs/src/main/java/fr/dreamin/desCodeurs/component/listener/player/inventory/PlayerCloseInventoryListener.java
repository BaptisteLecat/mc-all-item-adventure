package fr.dreamin.desCodeurs.component.listener.player.inventory;

import fr.dreamin.api.colors.StringColor;
import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.manager.ChestManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import java.util.Arrays;

public class PlayerCloseInventoryListener implements Listener {

  @EventHandler
  public void onPlayerCloseInventory(InventoryCloseEvent event) {

    if (event.getInventory().getHolder() instanceof Chest chest && (chest.getLocation().distance(ChestManager.getChestLoc()) < 2) ) {
      Arrays.stream(event.getInventory().getContents()).filter(is -> is != null && !is.getType().equals(Material.AIR)).forEach(is -> {
        if (!Main.getCodex().containsMaterialFromPlayers(is.getType())) {
          Main.getCodex().addMaterialFromPlayer(event.getPlayer().getUniqueId(), is.getType());
          Bukkit.broadcastMessage(Main.getCodex().getPrefix() + StringColor.GOLD.getColor() + event.getPlayer().getName() + StringColor.GRAY.getColor() + " a validé: " + StringColor.GREEN.getColor() + is.getI18NDisplayName());
          is.setAmount(is.getAmount() - 1);
        }
      });
    }
  }

}
