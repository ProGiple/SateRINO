package org.comp.progiple.saterino.others;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.itemConfigs.ItemsDataManager;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "saterino";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ProGiple";
    }

    @Override
    public @NotNull String getVersion() {
        return "latest";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("level")) {
            PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
            return String.valueOf(playerData.getInt("level"));
        }
        if (params.equalsIgnoreCase("raiting")) {
            PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
            return String.valueOf(playerData.getInt("raiting"));
        }
        if (params.equalsIgnoreCase("timeToNextUpdate")) {
            LocalTime now = LocalTime.now();
            LocalTime currentTime = null;
            for (String stringTime : Config.getStringList("config.updateTime")) {
                String[] splited = stringTime.split(":");
                if (splited.length != 2) continue;

                LocalTime time = LocalTime.of(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), 0);
                if (currentTime == null) currentTime = time;
                if (time.isAfter(now) && time.isBefore(currentTime)) currentTime = time;
            }

            if (currentTime == null) currentTime = LocalTime.of(0, 0);
            int hour, minute;
            if (currentTime.isAfter(now)) {
                hour = currentTime.getHour() - now.getHour();
                minute = currentTime.getMinute() - now.getMinute();
            }
            else {
                hour = (24 - now.getHour()) + currentTime.getHour();
                minute = (60 - now.getMinute()) + currentTime.getMinute();
            }

            return Config.getStr("placeholders.timeToNextUpdate")
                    .replace("HH", String.valueOf(hour < 10 ? "0" + hour : hour))
                    .replace("MM", String.valueOf(minute < 10 ? "0" + minute : minute));
        }
        if (params.contains("item")) {
            String[] splited = params.split("_");
            ConfigurationSection section = ItemsDataManager.getSection(splited[1]);
            switch (splited[2]) {
                case "material" -> {
                    return section.getString("material");
                }
                case "amount" -> {
                    return String.valueOf(section.getInt("count"));
                }
                case "moneyPerOne" -> {
                    return String.valueOf(section.getDouble("moneyPerOne"));
                }
                case "fullCost" -> {
                    return String.valueOf(BigDecimal.valueOf(section.getDouble("moneyPerOne") * section.getInt("count"))
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                }
            }
        }

        return "unknown placeholder"; //
    }
}
