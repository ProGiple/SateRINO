package org.comp.progiple.saterino.others.configs.itemConfigs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.comp.progiple.saterino.SateRINO;

import java.io.File;

@UtilityClass
public class ShopItemsConfig {
    private static final File file;
    private static FileConfiguration config;

    static {
        file = new File(SateRINO.getPlugin().getDataFolder(), "items/sellerItems.yml");
        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }
}
