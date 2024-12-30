package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.ItemsData;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.example.novasparkle.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class UpdateItemsButton extends Item implements Button {
    public UpdateItemsButton(ConfigurationSection section) {
        super(section);
        this.getLore().replaceAll(line -> line.replace("$cost", String.valueOf(Config.getInt("config.updateItemsCost"))));
        this.setLore(this.getLore());
    }

    @Override
    public void onClick(Player player) {
        PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
        int raiting = playerData.getInt("raiting");

        int neededRaiting = Config.getInt("config.updateItemsCost");
        if (raiting < neededRaiting) {
            player.sendMessage(Config.getMessage("noRaiting").replace("$need_raiting", String.valueOf(neededRaiting - raiting)));
            return;
        }

        playerData.set("raiting", raiting - neededRaiting);
        ItemsData.updateItems(player);
        player.closeInventory();
    }
}
