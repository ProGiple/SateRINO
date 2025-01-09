package org.comp.progiple.saterino.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.others.configs.Config;
import org.novasparkle.lunaspring.Menus.Items.Item;

import java.util.List;

public class ErrorItem extends Item implements Button {
    private final byte neededLevel;
    public ErrorItem(Material material, String displayName, List<String> lore, byte slot, byte neededLevel) {
        super(material, Utils.color(displayName), lore, 1, slot);
        this.neededLevel = neededLevel;

        this.getLore().replaceAll(line -> line.replace("$need_level", String.valueOf(this.neededLevel)));
        this.setLore(this.getLore());
    }

    @Override
    public void onClick(Player player) {
        player.sendMessage(Config.getMessage("noLevel").replace("$need_level", String.valueOf(this.neededLevel)));
    }
}
