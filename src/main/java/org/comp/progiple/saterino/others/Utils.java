package org.comp.progiple.saterino.others;

import org.bukkit.ChatColor;
import org.comp.progiple.saterino.others.configs.itemConfigs.SellerItemsManager;

public class Utils {
    public static String color(String sr) {
        return ChatColor.translateAlternateColorCodes('&', sr);
    }

    public static byte getMaxLevel() {
        byte maxLevel = 0;
        for (String level : SellerItemsManager.getSection("items").getKeys(false)) {
            byte byteLevel = Byte.parseByte(level);
            if (byteLevel > maxLevel) maxLevel = byteLevel;
        }
        return maxLevel;
    }
}
