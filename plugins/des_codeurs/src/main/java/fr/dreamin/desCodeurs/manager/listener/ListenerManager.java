package fr.dreamin.desCodeurs.manager.listener;

import fr.dreamin.desCodeurs.component.listener.player.inventory.PlayerCloseInventoryListener;
import fr.dreamin.desCodeurs.component.listener.player.join.PlayerJoinListener;
import fr.dreamin.desCodeurs.component.listener.player.leave.PlayerLeaveListener;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListenerManager {

  @Getter
  private static JavaPlugin javaPlugin;
  @Getter private static PluginManager pm;
  @Getter private static Set<Listener> listenerList = new HashSet<>();

  public ListenerManager(JavaPlugin plugin) {
    javaPlugin = plugin;
    pm = plugin.getServer().getPluginManager();
    registerAllListeners(new Class[]{
      PlayerJoinListener.class,
      PlayerLeaveListener.class,
      PlayerCloseInventoryListener.class
    });

  }

  public static void registerAllListeners(Class<?>[] listenerClasses) {
    for (Class<?> listenerClass : listenerClasses) {
      registerListener(listenerClass);
    }
  }

  public static void registerListener(Class<?> listener) {
    try {

      if (!Listener.class.isAssignableFrom(listener)) return;

      Listener l = (Listener) listener.getDeclaredConstructor().newInstance();
      pm.registerEvents(l, javaPlugin);
      listenerList.add(l);
    } catch (Exception e) {
      javaPlugin.getLogger().severe("Could not register listener: " + listener.getName());
      e.printStackTrace();
    }
  }

  public static void unregisterAllListeners(Class<?>[] listenerClasses) {
    for (Class<?> listenerClass : listenerClasses) {
      unloadListener(listenerClass);
    }
  }

  //Need to be fix
  public static void unloadListener(Class<?> listener) {
    try {
      for (Listener l : listenerList) {
        if (l.getClass().getName().equals(listener.getClass().getName())) HandlerList.unregisterAll(l);
      }

    } catch (Exception e) {
      javaPlugin.getLogger().severe("Could not unregister listener: " + listener.getName());
      e.printStackTrace();
    }
  }
}
