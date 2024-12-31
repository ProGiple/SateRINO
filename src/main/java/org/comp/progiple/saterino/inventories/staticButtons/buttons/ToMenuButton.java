package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.menus.main.MainMenu;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuConfig;
import org.example.novasparkle.Items.Item;
import org.example.novasparkle.Menus.MenuManager;

public class ToMenuButton extends Item implements Button {
    public ToMenuButton(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void onClick(Player player) {
        ConfigurationSection section = MainMenuConfig.getSection("menu");
        MenuManager.openInventory(player, new MainMenu(player, section.getString("title"),
                (byte) (section.getInt("rows") * 9), section.getConfigurationSection("items.decorations")));
    }
}
