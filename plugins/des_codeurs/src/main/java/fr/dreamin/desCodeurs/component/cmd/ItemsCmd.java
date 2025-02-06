package fr.dreamin.desCodeurs.component.cmd;

import fr.dreamin.desCodeurs.component.gui.ItemGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemsCmd implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

    Player player = (Player) sender;

    new ItemGui().open(player);

    return false;
  }
}
