package fr.dreamin.desCodeurs.component.listener.player.inventory;

import fr.dreamin.desCodeurs.Main;
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
    if (event.getInventory().equals(Main.getGame().getChestManager().getInventory())) {

      Arrays.stream(event.getInventory().getContents()).filter(is -> is != null && !is.getType().equals(Material.AIR)).forEach(is -> {
        if (!Main.getCodex().containsMaterialFromPlayers(is.getType())) {
          Main.getCodex().addMaterialFromPlayer(event.getPlayer().getUniqueId(), is.getType());
          is.setAmount(is.getAmount() - 1);
//          event.getInventory().remove(is);
        }
      });

    }

  }

  @EventHandler
  public void test4(InventoryClickEvent event) {
//    Bukkit.broadcastMessage("Click event");
//
//    if (event.getClickedInventory().equals(Main.getGame().getChestManager().getInventory())) Bukkit.broadcastMessage("Chest click");
//
//    Bukkit.broadcastMessage(event.getCurrentItem().getI18NDisplayName());

//    Arrays.stream(event.getInventory().getContents()).filter(is -> is != null && !is.getType().equals(Material.AIR)).forEach(is -> {
//      Bukkit.broadcastMessage("Item: " + is.getType());
//    });
  }

}
