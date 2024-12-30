package org.comp.progiple.saterino.inventories.staticButtons.buttons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.example.novasparkle.Items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpgradeLevelButton extends Item implements Button {
    public UpgradeLevelButton(ConfigurationSection section, byte currentLevel) {
        super(currentLevel < Utils.getMaxLevel() ? section :
                Objects.requireNonNull(section.getConfigurationSection("if_max_level")), section.getInt("slot"));
        this.getLore().replaceAll(line -> line
                .replace("$cost", String.valueOf(Config.getInt(String.format("config.levelBuying.%d", ((int) currentLevel) + 1))))
                .replace("$next_level", String.valueOf(currentLevel + 1)));
        this.setLore(this.getLore());
    }

    @Override
    public void onClick(Player player) {
        PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
        byte level = (byte) playerData.getInt("level");
        int raiting = playerData.getInt("raiting");

        if (level >= Config.getInt("config.maxLevel")) {
            player.sendMessage(Config.getMessage("maxLevel"));
            return;
        }

        int neededRaiting = Config.getInt(String.format("config.levelBuying.%s", level + 1));
        if (raiting < neededRaiting) {
            player.sendMessage(Config.getMessage("noRaiting").replace("$need_raiting", String.valueOf(neededRaiting - raiting)));
            return;
        }

        playerData.set("level", level + 1);
        playerData.set("raiting", raiting - neededRaiting);
        player.closeInventory();
    }
}
