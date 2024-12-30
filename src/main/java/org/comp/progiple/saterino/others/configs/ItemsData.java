package org.comp.progiple.saterino.others.configs;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.SateRINO;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemsData {
    private static final File file;
    private static FileConfiguration config;

    static {
        file = new File(SateRINO.getPlugin().getDataFolder(), "itemsData.yml");
        reload();
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public static String getString(String path) {return config.getString(path);}

    public static int getInt(String path) {return config.getInt(path);}

    @SneakyThrows
    public static void setItem(byte index, String material, int count, double cost) {
        config.set(String.format("%d.material", index), material);
        config.set(String.format("%d.moneyPerOne", index), cost);
        config.set(String.format("%d.count", index), count);
        config.save(file);
    }

    public static boolean updateItem(byte index, byte level, Random random) {
        ConfigurationSection levelItemsSection = SellerItemsConfig.getSection(String.format("items.%d", (int) level));
        if (levelItemsSection == null) return false;

        List<String> levelItems = new ArrayList<>(levelItemsSection.getKeys(false));
        int randomId = random.nextInt(levelItems.size());
        String itemId = levelItems.get(randomId);
        ConfigurationSection itemSection = levelItemsSection.getConfigurationSection(itemId);

        assert itemSection != null;
        Object objectCount = itemSection.get("count");
        int finalCount;
        if (objectCount != null) {
            if (objectCount instanceof String) {
                String[] counts = ((String) objectCount).split("-");
                finalCount = random.nextInt(Integer.parseInt(counts[1])
                        - Integer.parseInt(counts[0])) + Integer.parseInt(counts[0]);
            }
            else finalCount = (int) objectCount;
        }
        else return false;

        Object objectCost = itemSection.get("moneyPerOne");
        double finalCost;
        if (objectCost != null) {
            if (objectCost instanceof String) {
                String[] costs = ((String) objectCost).split("-");
                finalCost = Double.parseDouble(costs[0]) + (Math.random() * (Double.parseDouble(costs[1]) - Double.parseDouble(costs[0])));
            }
            else finalCost = (double) objectCost;
        }
        else return false;

        ItemsData.setItem(index, itemId, finalCount, BigDecimal.valueOf(finalCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return true;
    }

    @SuppressWarnings("deprecation")
    public static void updateItems(Player player) {
        ItemsData.updateItems(false);
        Config.getMessageList("playerUpdateItems").forEach(line -> Bukkit.getServer().broadcastMessage(line.
                replace("$player", player.getName())));
    }

    @SuppressWarnings("deprecation")
    public static void updateItems(boolean showMessage) {
        ConfigurationSection section = MainMenuConfig.getSection("menu.items.sellerItems");
        Random random = new Random();
        byte i = 1;

        for (String key : section.getKeys(false)) {
            if (ItemsData.updateItem(i, (byte) section.getInt(key), random)) i++;
        }

        if (showMessage) {
            Config.getMessageList("updateItems").forEach(line -> Bukkit.getServer().broadcastMessage(line));
        }
    }
}
