package fr.dreamin.desCodeurs;

import fr.dreamin.api.cmd.SimpleCommand;
import fr.dreamin.desCodeurs.component.cmd.ItemsCmd;
import fr.dreamin.desCodeurs.component.cmd.TestCmd;
import fr.dreamin.desCodeurs.component.config.Codex;
import fr.dreamin.desCodeurs.component.game.Game;
import fr.dreamin.mctools.McTools;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

  @Getter private static Main instance;
  @Getter private static Codex codex;
  @Getter private static Game game;

  @Override
  public void onEnable() {
    instance = this;

    McTools.setInstance(this);

    saveDefaultConfig();
    codex = new Codex(this);

    game = new Game(this);


    loadCommands();

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  private void loadCommands() {
    SimpleCommand.createCommand("test", new TestCmd());
    SimpleCommand.createCommand("items", new ItemsCmd());
  }


}
