package fr.dreamin.desCodeurs.component.game;

import fr.dreamin.desCodeurs.manager.ChestManager;
import fr.dreamin.desCodeurs.manager.gui.GuiManager;
import fr.dreamin.desCodeurs.manager.listener.ListenerManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Game {

  private ChestManager chestManager;
  private GuiManager guiManager;


  public Game(JavaPlugin plugin) {
    this.chestManager = new ChestManager(this);
    this.guiManager = new GuiManager(this);

    new ListenerManager(plugin);
  }

}
