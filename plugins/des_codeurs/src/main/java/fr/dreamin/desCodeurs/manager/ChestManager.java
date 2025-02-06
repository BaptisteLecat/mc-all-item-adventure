package fr.dreamin.desCodeurs.manager;

import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.component.game.Game;
import lombok.Getter;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class ChestManager {

  private final Game game;

  private Chest chest;
  private Inventory inventory;

  private HashMap<UUID, List<ItemStack>> items = new HashMap<>();

  public ChestManager(Game game) {
    this.game = game;

    this.chest = (Chest) Main.getCodex().getChestLocation().getBlock().getState();
    this.inventory = chest.getInventory();
  }

}
