package fr.dreamin.desCodeurs;

import fr.dreamin.api.cmd.SimpleCommand;
import fr.dreamin.desCodeurs.component.cmd.*;
import fr.dreamin.desCodeurs.component.config.Codex;
import fr.dreamin.desCodeurs.manager.gui.GuiManager;
import fr.dreamin.desCodeurs.manager.listener.ListenerManager;
import fr.dreamin.mctools.McTools;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

  @Getter private static Main instance;
  @Getter private static GuiManager guiManager;
  @Getter private static Codex codex;

  @Override
  public void onEnable() {
    instance = this;

    McTools.setInstance(this);

    saveDefaultConfig();
    codex = new Codex(this);

    guiManager = new GuiManager();

    new ListenerManager(this);

    loadCommands();

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  private void loadCommands() {
    SimpleCommand.createCommand("test", new TestCmd());
    SimpleCommand.createCommand("items", new ItemsCmd());
    SimpleCommand.createCommand("additem", new AddItemCmd());
    SimpleCommand.createCommand("removeitem", new RemoveItemCmd());
    SimpleCommand.createCommand("removeitem", new ReloadGame());
  }


}
