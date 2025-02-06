package fr.dreamin.desCodeurs.component.gui;

import fr.dreamin.api.colors.ComponentColor;
import fr.dreamin.api.colors.StringColor;
import fr.dreamin.api.gui.SplitGuiInterface;
import fr.dreamin.api.items.ItemBuilder;
import fr.dreamin.api.sound.SoundHandler;
import fr.dreamin.desCodeurs.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.Click;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.window.AnvilWindow;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class TestGui extends SplitGuiInterface {

  private String title = "";

  public TestGui() {

  }

  public TestGui(String title) {
    this.title = title;
  }

  @Override
  public Component name(Player player) {
    return ComponentColor.WHITE.colored("Liste des objets");
  }

  @Override
  public Gui guiUpper(Player player) {
    return Gui.normal()
      .setStructure(3, 1, "X D #")
      .addIngredient('X', Item.simple(new xyz.xenondevs.invui.item.ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(this.title)))
//      .addIngredient('D', )
      .build();
  }

  @Override
  public Gui guiLower(Player player) {
    player.sendMessage("gui:" + this.title);

    player.sendMessage(String.valueOf(filterName(Main.getGame().getGuiManager().getFilterItems().getCurrent(player), player).size()));

    return PagedGui.items().setStructure(
        ". X X X X X X X .",
        ". X X X X X X X .",
        "P . . . F . . . N")
      .addIngredient('X', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
      .addIngredient('F', new AbstractItem() {
        @Override
        public ItemProvider getItemProvider(Player player) {
          return new xyz.xenondevs.invui.item.ItemBuilder(new ItemBuilder(Material.NAME_TAG).setCustomModelData(5).setName(StringColor.YELLOW.colored("Filtre: " + Main.getGame().getGuiManager().getFilterItems().getKey(player))).toItemStack());

        }

        @Override
        public void handleClick(ClickType clickType, Player player, Click click) {
          Main.getGame().getGuiManager().getFilterItems().next(player);

        }
      })
      .setContent(filterName(Main.getGame().getGuiManager().getFilterItems().getCurrent(player), player))
      .build();
  }


  @Override
  public void open(Player player) {
    Window window = AnvilWindow.split()
      .setViewer(player)
      .setUpperGui(guiUpper(player))
      .setLowerGui(guiLower(player))
      .addRenameHandler(e -> {
        this.title = e;
        guiLower(player).notifyWindows();
      })
      .build();

    window.open();

  }

  private void reload() {
  }

  private List<Item> getItems(List<Material> materials) {
    List<Item> result = new ArrayList<>();

    materials.forEach(material -> {
      result.add(new AbstractItem() {
        @Override
        public ItemProvider getItemProvider(Player player) {
          List<String> lore = new ArrayList<>();
          boolean enchant = false;

          if (Main.getCodex().containsMaterialFromPlayers(material)) {
            enchant = true;
            lore.addAll(Main.getCodex().getDItemFromMaterial(material).get().getLore());
          }

          return new xyz.xenondevs.invui.item.ItemBuilder(new ItemBuilder(material).setLore(lore).setEnchantGlint(enchant).toItemStack());
        }

        @Override
        public void handleClick(ClickType clickType, Player player, Click click) {
          player.sendMessage(StringColor.GOLD.getColor() + "Lien de l'item: " + StringColor.GRAY.getColor() + "https://minecraft.wiki/w/" + convertString(material.name()));
        }
      });
    });

    return result;
  }

  private List<Item> filterName(List<Material> materials, Player player) {
    List<Item> result;

    if (this.title != null && !this.title.equals("")) {
      result = new ArrayList<>();
      getItems(materials).stream().forEach(i -> {
        if (i.getItemProvider(player).get().getI18NDisplayName().contains(this.title)) {
          player.sendMessage("true");
          result.add(i);
        }
        else player.sendMessage("false");

      });
    } else result = getItems(materials);

    return result;
  }

  private String convertString(String input) {
    String[] words = input.split("_");
    StringBuilder convertedString = new StringBuilder();

    for (String word : words) {
      if (word.length() > 0) {
        convertedString.append(Character.toUpperCase(word.charAt(0)));
        convertedString.append(word.substring(1).toLowerCase());
      }
      convertedString.append("_");
    }

    // Remove the trailing underscore
    if (convertedString.length() > 0) {
      convertedString.setLength(convertedString.length() - 1);
    }

    return convertedString.toString();
  }



}
