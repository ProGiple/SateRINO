package org.comp.progiple.saterino.others.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.SateRINO;
import org.novasparkle.lunaspring.Configuration.Configuration;
import org.novasparkle.lunaspring.Configuration.IConfig;

import java.util.List;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        SateRINO.getPlugin().saveDefaultConfig();
        config = new IConfig(SateRINO.getPlugin());
    }

    public void reload() {
        SateRINO.getPlugin().reloadConfig();
        config.reload(SateRINO.getPlugin());
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public String getStr(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public String getMessage(String messageId) {
        return Utils.color(getStr(String.format("messages.%s", messageId)));
    }

    public List<String> getMessageList(String messageId) {
        List<String> list = config.getStringList(String.format("messages.%s", messageId));
        list.replaceAll(Utils::color);
        return list;
    }
}
