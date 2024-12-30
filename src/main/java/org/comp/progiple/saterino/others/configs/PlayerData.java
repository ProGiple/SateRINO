package org.comp.progiple.saterino.others.configs;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.comp.progiple.saterino.SateRINO;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    @Getter private static final Map<String, PlayerData> playerDataMap = new HashMap<>();
    private final File file;
    private FileConfiguration config;

    public PlayerData(String nick) {
        SateRINO plugin = SateRINO.getPlugin();
        this.file = new File(plugin.getDataFolder(), String.format("data/%s.yml", nick));
        this.load();
        playerDataMap.put(nick, this);
    }

    public PlayerData(File file) {
        this.file = file;
        this.load();
        playerDataMap.put(this.file.getName().replace(".yml", ""), this);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    @SneakyThrows
    public void set(String path, int value) {
        this.config.set(path, value);
        this.config.save(this.file);
    }

    private void load() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            this.set("level", 0);
            this.set("raiting", 0);
        }
    }
}
