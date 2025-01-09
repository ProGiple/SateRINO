package org.comp.progiple.saterino.others.configs.itemConfigs;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.SateRINO;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuManager;
import org.novasparkle.lunaspring.Configuration.Configuration;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class ItemsDataManager {
    private final Configuration config;

    static {
        config = new Configuration(new File(SateRINO.getPlugin().getDataFolder(), "itemsData.yml"));
    }

    public void reload() {
        config.reload();
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public String getString(String path) {return config.getString(path);}

    public int getInt(String path) {return config.getInt(path);}

    @SneakyThrows
    public void setItem(byte index, String material, int count, double cost) {
        config.setString(String.format("%d.material", index), material);
        config.set(String.format("%d.moneyPerOne", index), cost);
        config.setInt(String.format("%d.count", index), count);
        config.save();
    }

    public boolean updateItem(byte index, byte level, Random random) {
        ConfigurationSection levelItemsSection = SellerItemsManager.getSection(String.format("items.%s", String.valueOf(level)));
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

        ItemsDataManager.setItem(index, itemId, finalCount, BigDecimal.valueOf(finalCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return true;
    }

    @SuppressWarnings("deprecation")
    public void updateItems(Player player) {
        ItemsDataManager.updateItems(false);
        Config.getMessageList("playerUpdateItems").forEach(line -> Bukkit.getServer().broadcastMessage(line.
                replace("$player", player.getName())));
    }

    @SuppressWarnings("deprecation")
    public void updateItems(boolean showMessage) {
        ConfigurationSection section = MainMenuManager.getSection("menu.items.sellerItems");
        Random random = new Random();

        byte i = 1;
        for (String key : section.getKeys(false)) {
            if (ItemsDataManager.updateItem(i, (byte) section.getInt(key), random)) i++;
        }

        if (showMessage) {
            Config.getMessageList("updateItems").forEach(line -> Bukkit.getServer().broadcastMessage(line));
        }
    }
}
