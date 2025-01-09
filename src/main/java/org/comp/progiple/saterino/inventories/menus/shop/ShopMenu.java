package org.comp.progiple.saterino.inventories.menus.shop;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.menus.shop.shopItems.ShopItem;
import org.comp.progiple.saterino.inventories.menus.shop.shopItems.ShopItemsSetter;
import org.comp.progiple.saterino.inventories.staticButtons.ButtonSetter;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.comp.progiple.saterino.others.configs.menuConfigs.ShopMenuManager;
import org.novasparkle.lunaspring.Menus.Items.Item;
import org.novasparkle.lunaspring.Menus.AMenu;

public class ShopMenu extends AMenu {
    private final ButtonSetter buttonSetter;
    private final ShopItemsSetter shopItemsSetter;
    private final Player player;
    public ShopMenu(Player player, String title, byte size, ConfigurationSection decorationSection) {
        super(player, title, size, decorationSection);

        this.player = player;
        PlayerData playerData = PlayerData.getPlayerDataMap().get(this.player.getName());
        byte level = (byte) playerData.getInt("level");

        this.buttonSetter = new ButtonSetter(ShopMenuManager.getSection("menu.items.clickable"), level);
        this.shopItemsSetter = new ShopItemsSetter(level);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        for (Button button : this.buttonSetter.getButtonList()) {
            this.getInventory().setItem(button.getSlot(), button.getItemStack());
        }
        this.shopItemsSetter.getShopItemList().forEach(item -> item.insert(this.getInventory(), item.getSlot()));
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

        for (Item item : this.shopItemsSetter.getShopItemList()) {
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
