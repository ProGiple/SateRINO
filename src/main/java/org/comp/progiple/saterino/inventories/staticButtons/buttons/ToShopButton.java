package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.menus.shop.ShopMenu;
import org.comp.progiple.saterino.others.configs.menuConfigs.ShopMenuConfig;
import org.example.novasparkle.Items.Item;
import org.example.novasparkle.Menus.MenuManager;

public class ToShopButton extends Item implements Button {
    public ToShopButton(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void onClick(Player player) {
        ConfigurationSection section = ShopMenuConfig.getSection("menu");
        MenuManager.openInventory(player, new ShopMenu(player, section.getString("title"),
                (byte) (section.getInt("rows") * 9), section.getConfigurationSection("items.decorations")));
    }
}
