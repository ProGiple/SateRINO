package org.comp.progiple.saterino.others.configs.itemConfigs;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.SateRINO;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuConfig;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class ItemsData {
    private static final File file;
    private static FileConfiguration config;

    static {
        file = new File(SateRINO.getPlugin().getDataFolder(), "itemsData.yml");
        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public String getString(String path) {return config.getString(path);}

    public int getInt(String path) {return config.getInt(path);}

    @SneakyThrows
    public void setItem(byte index, String material, int count, double cost) {
        config.set(String.format("%d.material", index), material);
        config.set(String.format("%d.moneyPerOne", index), cost);
        config.set(String.format("%d.count", index), count);
        config.save(file);
    }

    public boolean updateItem(byte index, byte level, Random random) {
        ConfigurationSection levelItemsSection = SellerItemsConfig.getSection(String.format("items.%d", (int) level));
        if (levelItemsSection == null) return false;

        List<String> levelItems = new ArrayList<>(levelItemsSection.getKeys(false));
        int randomId = random.nextInt(levelItems.size());
        String itemId = levelItems.get(randomId);
        ConfigurationSection itemSection = levelItemsSection.getConfigurationSection(itemId);
        assert itemSection != null;

        String stringCount = itemSection.getString("count");
        assert stringCount != null;
        String[] counts = stringCount.split("-");

        int finalCount = Integer.parseInt(counts[0]);
        if (counts.length > 1) finalCount = random.nextInt(Integer.parseInt(counts[1])
                - Integer.parseInt(counts[0])) + Integer.parseInt(counts[0]);

        String stringCost = itemSection.getString("moneyPerOne");
        assert stringCost != null;
        String[] costs = stringCost.split("-");

        double finalCost = Double.parseDouble(costs[0]);
        if (costs.length > 1) finalCost = Double.parseDouble(costs[0]) + (Math.random() * (Double.parseDouble(costs[1]) - Double.parseDouble(costs[0])));

        ItemsData.setItem(index, itemId, finalCount, BigDecimal.valueOf(finalCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return true;
    }

    @SuppressWarnings("deprecation")
    public void updateItems(Player player) {
        ItemsData.updateItems(false);
        Config.getMessageList("playerUpdateItems").forEach(line -> Bukkit.getServer().broadcastMessage(line.
                replace("$player", player.getName())));
    }

    @SuppressWarnings("deprecation")
    public void updateItems(boolean showMessage) {
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
