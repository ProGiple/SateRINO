package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.menus.main.MainMenu;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuManager;
import org.novasparkle.lunaspring.Items.Item;
import org.novasparkle.lunaspring.Menus.MenuManager;

public class ToMenuButton extends Item implements Button {
    public ToMenuButton(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void onClick(Player player) {
        MenuManager.openInventory(player, new MainMenu(player,
                MainMenuManager.getString("menu.title"), MainMenuManager.getInt("menu.rows"),
                MainMenuManager.getSection("menu.items.decorations")));
    }
}
