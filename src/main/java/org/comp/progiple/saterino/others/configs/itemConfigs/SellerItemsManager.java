package org.comp.progiple.saterino.others.configs.itemConfigs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.saterino.SateRINO;
import org.novasparkle.lunaspring.Configuration.Configuration;

import java.io.File;

@UtilityClass
public class SellerItemsManager {
    private final Configuration config;

    static {
        config = new Configuration(new File(SateRINO.getPlugin().getDataFolder(), "items/sellerItems.yml"));
        reload();
    }

    public void reload() {
        config.reload();
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }
}
