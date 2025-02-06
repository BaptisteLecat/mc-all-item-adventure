package fr.dreamin.desCodeurs.component;

import fr.dreamin.api.colors.StringColor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

@Getter
public class DItem {

  private final Material material;
  private final UUID uuid;
  private final String at;

  public DItem(Material material, UUID uuid, String at) {
    this.material = material;
    this.uuid = uuid;
    this.at = at;
  }

  public List<String> getLore() {
    return List.of(" ", StringColor.GOLD.getColor() + "Par: " + StringColor.GRAY.getColor() + Bukkit.getOfflinePlayer(uuid).getName(), StringColor.GOLD.getColor() + "Heure: " + StringColor.GRAY.getColor() + at);
  }

}
