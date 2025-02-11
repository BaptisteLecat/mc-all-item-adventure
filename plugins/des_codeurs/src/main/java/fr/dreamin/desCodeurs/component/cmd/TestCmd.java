package fr.dreamin.desCodeurs.component.cmd;

import fr.dreamin.desCodeurs.manager.ChestManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TestCmd implements CommandExecutor, TabExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    Player player  = (Player) sender;

    player.openInventory(ChestManager.getInventory());

    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    return Arrays.stream(Material.values()).map(Material::name).toList();
  }
}
