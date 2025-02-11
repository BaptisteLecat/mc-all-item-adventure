package fr.dreamin.desCodeurs.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Random;

public class GameUtils {

  public static Location randomLocation(Location locA, Location locB) {
    World world = locA.getWorld();
    if (!world.equals(locB.getWorld())) {
      throw new IllegalArgumentException("Les deux points doivent être dans le même monde !");
    }

    Random random = new Random();
    double minX = Math.min(locA.getX(), locB.getX());
    double minZ = Math.min(locA.getZ(), locB.getZ());

    double maxX = Math.max(locA.getX(), locB.getX());
    double maxZ = Math.max(locA.getZ(), locB.getZ());

    int randomX;
    int randomZ;
    int highestY;
    Location randomLocation;
    Biome biome;

    do {
      randomX = (int) (minX + (maxX - minX) * random.nextDouble());
      randomZ = (int) (minZ + (maxZ - minZ) * random.nextDouble());

      // Trouver le bloc le plus élevé à la position X et Z sélectionnée aléatoirement
      highestY = world.getHighestBlockYAt(randomX, randomZ);

      randomLocation = new Location(world, randomX + 0.5, highestY, randomZ + 0.5);
      biome = world.getBiome(randomX, highestY, randomZ);
    } while (isOceanOrRiverBiome(biome));

    return randomLocation.add(0, 1, 0);
  }

  private static boolean isOceanOrRiverBiome(Biome biome) {
    return biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.WARM_OCEAN || biome == Biome.LUKEWARM_OCEAN || biome == Biome.COLD_OCEAN || biome == Biome.DEEP_LUKEWARM_OCEAN || biome == Biome.DEEP_COLD_OCEAN || biome == Biome.FROZEN_OCEAN || biome == Biome.DEEP_FROZEN_OCEAN || biome == Biome.RIVER || biome == Biome.FROZEN_RIVER;
  }

}
