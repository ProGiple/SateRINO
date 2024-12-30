package org.comp.progiple.saterino;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.Menu;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.ItemsData;
import org.comp.progiple.saterino.others.configs.MainMenuConfig;
import org.comp.progiple.saterino.others.configs.SellerItemsConfig;
import org.example.novasparkle.Menus.MenuManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            switch (strings[0]) {
                case "open" -> this.openMenu(commandSender);
                case "reload" -> {
                    if (commandSender.hasPermission("saterino.admin")) {
                        Config.reload();
                        MainMenuConfig.reload();
                        SellerItemsConfig.reload();
                        ItemsData.reload();
                        commandSender.sendMessage(Config.getMessage("reloadPlugin"));
                    }
                    else commandSender.sendMessage(Config.getMessage("noPerm"));
                }
                case "update" -> {
                    if (commandSender.hasPermission("saterino.admin")) {
                        ItemsData.updateItems(true);
                    }
                    else commandSender.sendMessage(Config.getMessage("noPerm"));
                }
            }
        }
        else this.openMenu(commandSender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("open", "reload", "update");
        }
        return List.of();
    }

    private void openMenu(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MenuManager.openInventory(player, new Menu(player,
                    MainMenuConfig.getString("menu.title"), MainMenuConfig.getInt("menu.rows"),
                        MainMenuConfig.getSection("menu.items.decorations")));
        }
    }
}
