package fr.dreamin.desCodeurs.manager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class ChestManager {

  @Getter private static Location chestLoc;
  @Getter private static Chest chest;
  @Getter private static Inventory inventory;

  @Getter private static HashMap<UUID, List<ItemStack>> items = new HashMap<>();

  public static void init(Location location) {
    chestLoc = location;
    Block block = chestLoc.getBlock();

    chestLoc.clone().add(0, -1, 0).getBlock().setType(Material.CHISELED_STONE_BRICKS);

    block.setType(Material.CHEST);

    if (block.getState() instanceof Chest ch) {
      inventory = ch.getBlockInventory();
      chest = ch;
    }
    else System.out.println("Unable to find a chest at " + chestLoc);
  }

}
