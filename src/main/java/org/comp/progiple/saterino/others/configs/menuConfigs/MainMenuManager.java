package org.comp.progiple.saterino.others.configs.menuConfigs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.saterino.SateRINO;
import org.novasparkle.lunaspring.Configuration.Configuration;

import java.io.File;

@UtilityClass
public class MainMenuManager {
    private final Configuration config;

    static {
        config = new Configuration(new File(SateRINO.getPlugin().getDataFolder(), "menus/main.yml"));
        reload();
    }

    public void reload() {
        config.reload();
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }
}
