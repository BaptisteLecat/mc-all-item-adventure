package fr.dreamin.desCodeurs.component.team;

import fr.dreamin.desCodeurs.component.player.DPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class Team {

  private String name;
  private Set<DPlayer> dPlayers = new HashSet<>();

  public Team(String name, Set<DPlayer> dPlayers) {
    this.name = name;
    this.dPlayers.addAll(dPlayers);
  }

}
