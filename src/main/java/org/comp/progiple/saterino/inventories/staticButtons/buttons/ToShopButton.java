package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.menus.shop.ShopMenu;
import org.comp.progiple.saterino.others.configs.menuConfigs.ShopMenuManager;
import org.novasparkle.lunaspring.Items.Item;
import org.novasparkle.lunaspring.Menus.MenuManager;

public class ToShopButton extends Item implements Button {
    public ToShopButton(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void onClick(Player player) {
        ConfigurationSection section = ShopMenuManager.getSection("menu");
        MenuManager.openInventory(player, new ShopMenu(player, section.getString("title"),
                (byte) (section.getInt("rows") * 9), section.getConfigurationSection("items.decorations")));
    }
}
