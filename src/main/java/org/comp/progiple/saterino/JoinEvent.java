package org.comp.progiple.saterino;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.comp.progiple.saterino.others.configs.PlayerData;

public class JoinEvent implements org.bukkit.event.Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String nick = e.getPlayer().getName();
        if (!PlayerData.getPlayerDataMap().containsKey(nick)) {
            new PlayerData(nick);
        }
    }
}
