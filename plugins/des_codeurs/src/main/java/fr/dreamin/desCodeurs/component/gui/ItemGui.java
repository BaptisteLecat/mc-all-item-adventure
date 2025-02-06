package fr.dreamin.desCodeurs.component.gui;

import fr.dreamin.api.colors.ComponentColor;
import fr.dreamin.api.colors.StringColor;
import fr.dreamin.api.gui.SingleGuiInterface;
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

import java.util.ArrayList;
import java.util.List;

public class ItemGui extends SingleGuiInterface {

  @Override
  public Component name(Player player) {
    return ComponentColor.WHITE.colored("Liste des objets");
  }

  @Override
  public boolean closable(Player player) {
    return true;
  }

  @Override
  public Gui guiUpper(Player player) {
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
          new SoundHandler("danganronpa:click_button", SoundCategory.MASTER, 1F, 1F).play(player, null);
          Main.getGame().getGuiManager().getFilterItems().next(player);
          new ItemGui().open(player);
        }
      })
      .setContent(getItems(Main.getGame().getGuiManager().getFilterItems().getCurrent(player)))
      .build();
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

  public String convertString(String input) {
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
