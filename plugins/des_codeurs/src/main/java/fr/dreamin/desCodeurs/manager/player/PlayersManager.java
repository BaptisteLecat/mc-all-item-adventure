package fr.dreamin.desCodeurs.manager.player;

import fr.dreamin.desCodeurs.component.player.DPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PlayersManager {

  @Getter private static Set<DPlayer> dPlayerSet = new HashSet<>();

  // #################################################################
  // ---------------------- PRIVATE METHODS --------------------------
  // #################################################################

  private static Stream<DPlayer> getFilteredPlayers(Set<DPlayer> playerSet, Predicate<DPlayer> predicate) {
    return playerSet.stream().filter(predicate);
  }

  // #################################################################
  // ---------------------- GETTER METHODS --------------------------
  // #################################################################

  public static List<DPlayer> getDPlayers() {
    return List.copyOf(dPlayerSet);
  }
  public static List<DPlayer> getDPlayers(Predicate<DPlayer> predicate) {
    return getFilteredPlayers(dPlayerSet, predicate).toList();
  }


  // #################################################################
  // ---------------------- SEARCH METHODS --------------------------
  // #################################################################

  public static DPlayer getPlayer(Player player) {
    return dPlayerSet.stream().filter(dPlayer -> dPlayer.getPlayer().equals(player)).findFirst().orElse(null);
  }

  public static DPlayer getPlayer(UUID uuid) {
    return dPlayerSet.stream().filter(dPlayer -> dPlayer.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
  }

  public static DPlayer getPlayer(String name) {
    return dPlayerSet.stream().filter(dPlayer -> dPlayer.getPlayer().getName().equals(name)).findFirst().orElse(null);
  }

  // #################################################################
  // ---------------------- CONTAINS METHODS --------------------------
  // #################################################################

  public static boolean contains(Player player) {
    return dPlayerSet.stream().anyMatch(dPlayer -> dPlayer.getPlayer().equals(player));
  }
  public static boolean contains(String name) {
    return dPlayerSet.stream().anyMatch(dPlayer -> dPlayer.getPlayer().getName().equals(name));
  }
  public static boolean contains(UUID uuid) {
    return dPlayerSet.stream().anyMatch(dPlayer -> dPlayer.getPlayer().getUniqueId().equals(uuid));
  }
}