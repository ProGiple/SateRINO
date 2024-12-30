package org.comp.progiple.saterino;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.comp.progiple.saterino.others.Placeholders;
import org.comp.progiple.saterino.others.configs.ItemsData;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.example.novasparkle.Events.MenuHandler;

import java.io.File;
import java.util.Objects;

public final class SateRINO extends JavaPlugin {
    @Getter private static SateRINO plugin;
    @Getter private static Economy economy = null;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveResource("items/sellerItems.yml", false);
        plugin.saveResource("menus/main.yml", false);
        plugin.saveResource("itemsData.yml", false);

        File dir = new File(plugin.getDataFolder(), "data");
        if (dir.exists() && dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                new PlayerData(file);
            }
        }

        if (!this.setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
        }

        Command command = new Command();
        Objects.requireNonNull(getCommand("saterino")).setExecutor(command);
        Objects.requireNonNull(getCommand("saterino")).setTabCompleter(command);

        this.reg(new Listener());
        this.reg(new MenuHandler());
        ItemsData.updateItems(false);
        new Runnable().runTaskTimer(plugin, 0, 60 * 20L);
    }

    private void reg(org.bukkit.event.Listener listener) {
        getServer().getPluginManager().registerEvents(listener, plugin);
    }

    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
