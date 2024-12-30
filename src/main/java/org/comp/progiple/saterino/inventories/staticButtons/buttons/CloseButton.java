package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.example.novasparkle.Items.Item;

public class CloseButton extends Item implements Button {
    public CloseButton(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void onClick(Player player) {
        player.closeInventory();
    }
}
