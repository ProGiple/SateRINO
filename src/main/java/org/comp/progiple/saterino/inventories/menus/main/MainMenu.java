package org.comp.progiple.saterino.inventories.menus.main;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.menus.main.sellerItems.SellerItemsSetter;
import org.comp.progiple.saterino.inventories.staticButtons.ButtonSetter;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuManager;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.novasparkle.lunaspring.Menus.AMenu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MainMenu extends AMenu {
    private final ButtonSetter buttonSetter;
    private final SellerItemsSetter sellerItemsSetter;
    private final Player player;
    public MainMenu(Player player, String title, int rows, ConfigurationSection decorationSection) {
        super(player, title, (byte) (rows * 9), decorationSection);
        this.player = player;

        ConfigurationSection section = MainMenuManager.getSection("menu.items");
        this.buttonSetter = new ButtonSetter(Objects.requireNonNull(section.getConfigurationSection("clickable")),
                (byte) PlayerData.getPlayerDataMap().get(this.player.getName()).getInt("level"));

        LinkedHashMap<Byte, Byte> byteByteMap = new LinkedHashMap<>();
        ConfigurationSection sellerItemsSection = Objects.requireNonNull(section.getConfigurationSection("sellerItems"));
        for (String key : sellerItemsSection.getKeys(false)) {
            byteByteMap.put(Byte.parseByte(key), (byte) sellerItemsSection.getInt(key));
        }
        this.sellerItemsSetter = new SellerItemsSetter(byteByteMap,
                (byte) PlayerData.getPlayerDataMap().get(this.player.getName()).getInt("level"));
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        for (Button button : this.buttonSetter.getButtonList()) {
            this.getInventory().setItem(button.getSlot(), button.getItemStack());
        }
        this.addItems(this.sellerItemsSetter.getSellerItemList());
        this.insertAllItems();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null) return;
        e.setCancelled(true);

        for (Button button : this.buttonSetter.getButtonList()) {
            if (button.getItemStack().equals(itemStack)) {
                button.onClick(this.player);
                return;
            }
        }

        for (Item item : this.sellerItemsSetter.getSellerItemList()) {
            if (item.getItemStack().equals(itemStack)) {
                ((Button) item).onClick(this.player);
                break;
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
