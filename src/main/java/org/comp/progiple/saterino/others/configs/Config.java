package org.comp.progiple.saterino.others.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.SateRINO;

import java.util.List;

public class Config {
    private static FileConfiguration config;
    static {
        SateRINO.getPlugin().saveDefaultConfig();
        Config.reload();
    }

    public static void reload() {
        SateRINO.getPlugin().reloadConfig();
        config = SateRINO.getPlugin().getConfig();
    }

    public static int getInt(String path) {
        return config.getInt(path);
    }

    public static String getStr(String path) {
        return config.getString(path);
    }

    public static List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public static String getMessage(String messageId) {
        return Utils.color(getStr(String.format("messages.%s", messageId)));
    }

    public static List<String> getMessageList(String messageId) {
        List<String> list = config.getStringList(String.format("messages.%s", messageId));
        list.replaceAll(Utils::color);
        return list;
    }
}
