package org.comp.progiple.saterino.others.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.SateRINO;

import java.util.List;

@UtilityClass
public class Config {
    private static FileConfiguration config;
    static {
        SateRINO.getPlugin().saveDefaultConfig();
        Config.reload();
    }

    public void reload() {
        SateRINO.getPlugin().reloadConfig();
        config = SateRINO.getPlugin().getConfig();
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
