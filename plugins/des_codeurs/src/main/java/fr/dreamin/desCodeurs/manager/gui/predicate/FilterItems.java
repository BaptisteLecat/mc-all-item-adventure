package fr.dreamin.desCodeurs.manager.gui.predicate;

import fr.dreamin.desCodeurs.Main;
import fr.dreamin.desCodeurs.component.DItem;
import fr.dreamin.desCodeurs.component.player.DPlayer;
import fr.dreamin.desCodeurs.manager.player.PlayersManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FilterItems {

  private HashMap<String, Function<Player, List<Material>>> predicatesItems = new HashMap<>();
  private HashMap<Player, Integer> predicatesPlayerItems = new HashMap<>();

  public FilterItems() {
    // Initialisation des différents filtres
    predicatesItems.put("Tout", d -> Main.getCodex().getMaterials());
    predicatesItems.put("Validé", d -> {

      List<Material> result = new ArrayList<>();

      for (List<DItem> items : Main.getCodex().getMaterialsPlayers().values()) {
        result.addAll(items.stream().map(DItem::getMaterial).toList());
      }

      return result;
    });
    predicatesItems.put("Non validé", d -> {

      List<Material> result = new ArrayList<>(Main.getCodex().getMaterials());

      for (List<DItem> items : Main.getCodex().getMaterialsPlayers().values()) {
        result.removeAll(items.stream().map(DItem::getMaterial).toList());
      }

      return result;

    });
  }

  //function to get a list of material from player where integer equals index of predicatesItems , name of fonction getCurrent
  public List<Material> getCurrent(Player player) {
    return predicatesItems.getOrDefault(getKey(player), d -> new ArrayList<>()).apply(player);
  }

  // Passer au Predicate suivant pour un joueur
  public void next(Player player) {
    // Récupérer l'index actuel du joueur (ou mettre à zéro s'il n'est pas présent)
    int predicateIndex = this.predicatesPlayerItems.getOrDefault(player, 2);

    // Passer à l'index suivant
    predicateIndex++;

    // Si l'index dépasse la taille, on revient à zéro
    if (predicateIndex >= predicatesItems.size()) predicateIndex = 0;

    // Mettre à jour l'index du joueur
    this.predicatesPlayerItems.put(player, predicateIndex);
  }

  public String getKey(Player player) {
    return (String) predicatesItems.keySet().toArray()[this.predicatesPlayerItems.getOrDefault(player, 0)];
  }

  private static Optional<DPlayer> getDPlayer(Player player) {
    return Optional.ofNullable(PlayersManager.getPlayer(player));
  }

}
