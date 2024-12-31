package org.comp.progiple.saterino;

import org.bukkit.scheduler.BukkitRunnable;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.itemConfigs.ItemsData;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Runnable extends BukkitRunnable {
    @Override
    public void run() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = time.format(formatter);

        if (Config.getStringList("config.updateTime").contains(formattedTime)) {
            ItemsData.updateItems(true);
        }
    }
}
