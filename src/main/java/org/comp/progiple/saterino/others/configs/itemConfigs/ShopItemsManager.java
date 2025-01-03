package org.comp.progiple.saterino.others.configs.itemConfigs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.comp.progiple.saterino.SateRINO;
import org.novasparkle.lunaspring.Configuration.Configuration;

import java.io.File;

@UtilityClass
public class ShopItemsManager {
    private final Configuration config;

    static {
        config = new Configuration(new File(SateRINO.getPlugin().getDataFolder(), "items/shopItems.yml"));
        reload();
    }

    public void reload() {
        config.reload();
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }
}
