package org.comp.progiple.saterino.inventories.sellerItems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.others.configs.Config;
import org.example.novasparkle.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class ErrorItem extends Item implements Button {
    private final byte neededLevel;
    public ErrorItem(Material material, String displayName, List<String> lore, byte slot, byte neededLevel) {
        super(material, displayName, lore, 1, slot);
        this.neededLevel = neededLevel;

        this.getLore().replaceAll(line -> line.replace("$need_level", String.valueOf(this.neededLevel)));
        this.setLore(this.getLore());
    }

    @Override
    public void onClick(Player player) {
        player.sendMessage(Config.getMessage("noLevel").replace("$need_level", String.valueOf(this.neededLevel)));
    }
}
