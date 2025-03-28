package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.itemConfigs.ItemsDataManager;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.novasparkle.lunaspring.Menus.Items.Item;

public class UpdateItemsButton extends Item implements Button {
    public UpdateItemsButton(ConfigurationSection section) {
        super(section, section.getInt("slot"));
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
        ItemsDataManager.updateItems(player);
        player.closeInventory();
    }
}
