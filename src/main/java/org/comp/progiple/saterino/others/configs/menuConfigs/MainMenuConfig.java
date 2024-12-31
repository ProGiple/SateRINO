package org.comp.progiple.saterino.others.configs.menuConfigs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.comp.progiple.saterino.SateRINO;

import java.io.File;

@UtilityClass
public class MainMenuConfig {
    private static final File file;
    private static FileConfiguration config;

    static {
        file = new File(SateRINO.getPlugin().getDataFolder(), "menus/main.yml");
        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }
}
