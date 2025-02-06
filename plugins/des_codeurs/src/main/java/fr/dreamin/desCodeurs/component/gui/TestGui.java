package fr.dreamin.desCodeurs.component.gui;

import fr.dreamin.api.colors.ComponentColor;
import fr.dreamin.api.colors.StringColor;
import fr.dreamin.api.gui.SplitGuiInterface;
import fr.dreamin.api.items.ItemBuilder;
import fr.dreamin.api.packUtils.ItemsPreset;
import fr.dreamin.api.sound.SoundHandler;
import fr.dreamin.desCodeurs.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.*;
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
      .build();
  }

  @Override
  public PagedGui<Item> guiLower(Player player) {
    player.sendMessage("gui:" + this.title);

    List<Item> items = filterName(Main.getGame().getGuiManager().getFilterItems().getCurrent(player), player);

    player.sendMessage(String.valueOf(items.size()));

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
      .addIngredient('P', new AbstractPagedGuiBoundItem() {

        @Override
        public ItemProvider getItemProvider(Player player1) {
          if (getGui().hasPreviousPage()) return new ItemBuilder(ItemsPreset.arrowLeft.getItem()).setName("Previous").toGuiItem();
          return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setHideToolType(true).toGuiItem();
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click event) {
          if (getItemProvider(player).get().getType().equals(Material.ARROW)) new SoundHandler(Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1).play(player, null);
          getGui().goBack();
        }
      })
      .addIngredient('N',new AbstractPagedGuiBoundItem(){

        @Override
        public ItemProvider getItemProvider(Player player1) {
          if (getGui().hasNextPage()) return new ItemBuilder(ItemsPreset.arrowRight.getItem()).setName("Next").toGuiItem();
          return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setHideToolType(true).toGuiItem();
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click event) {
          if (getItemProvider(player).get().getType().equals(Material.ARROW)) new SoundHandler(Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1).play(player, null);
          getGui().goForward();
        }
      })
      .setContent(items)
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
        guiLower(player).setContent(filterName(Main.getGame().getGuiManager().getFilterItems().getCurrent(player), player));
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
