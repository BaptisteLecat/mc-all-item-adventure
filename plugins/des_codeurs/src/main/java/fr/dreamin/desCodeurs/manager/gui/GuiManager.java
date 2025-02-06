package fr.dreamin.desCodeurs.manager.gui;

import fr.dreamin.desCodeurs.component.game.Game;
import fr.dreamin.desCodeurs.manager.gui.predicate.FilterItems;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuiManager {
  private final Game game;

  private FilterItems filterItems = new FilterItems();

  public GuiManager(Game game) {
    this.game = game;
  }


}