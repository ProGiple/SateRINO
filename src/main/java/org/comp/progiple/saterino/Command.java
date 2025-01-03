package org.comp.progiple.saterino;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.menus.main.MainMenu;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.itemConfigs.ItemsDataManager;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.comp.progiple.saterino.others.configs.itemConfigs.ShopItemsManager;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuManager;
import org.comp.progiple.saterino.others.configs.itemConfigs.SellerItemsManager;
import org.comp.progiple.saterino.others.configs.menuConfigs.ShopMenuManager;
import org.novasparkle.lunaspring.Menus.MenuManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            switch (strings[0]) {
                case "open" -> this.openMenu(commandSender);
                case "reload" -> {
                    if (commandSender.hasPermission("saterino.admin")) {
                        if (strings.length >= 2 && !strings[1].isEmpty()) {
                            switch (strings[1]) {
                                case "config" -> Config.reload();
                                case "mainMenu" -> MainMenuManager.reload();
                                case "shopMenu" -> ShopMenuManager.reload();
                                case "itemsData" -> ItemsDataManager.reload();
                                case "sellerItems" -> SellerItemsManager.reload();
                                case "shopItems" -> ShopItemsManager.reload();
                            }
                        }
                        else {
                            Config.reload();
                            MainMenuManager.reload();
                            ShopMenuManager.reload();
                            ItemsDataManager.reload();
                            SellerItemsManager.reload();
                            ShopItemsManager.reload();
                        }
                        commandSender.sendMessage(Config.getMessage("reloadPlugin"));
                    }
                    else commandSender.sendMessage(Config.getMessage("noPerm"));
                }
                case "update" -> {
                    if (commandSender.hasPermission("saterino.admin")) {
                        ItemsDataManager.updateItems(true);
                    }
                    else commandSender.sendMessage(Config.getMessage("noPerm"));
                }
                case "add" -> {
                    if (commandSender.hasPermission("saterino.admin")) {
                        if (strings.length >= 3 && !strings[1].isEmpty()) {
                            PlayerData playerData = PlayerData.getPlayerDataMap().get(strings[2]);
                            int upd = strings.length >= 4 && !strings[3].isEmpty() ? Integer.parseInt(strings[3]) : 1;
                            if (playerData == null) {
                                commandSender.sendMessage(Config.getMessage("dataIsNull"));
                                return true;
                            }

                            if (strings[1].equalsIgnoreCase("level")) {
                                int level = playerData.getInt("level");
                                if (level + upd >= Utils.getMaxLevel()) {
                                    commandSender.sendMessage(Config.getMessage("maxLevel"));
                                    return true;
                                }
                                else {
                                    playerData.set("level", level + upd);
                                }
                            }
                            else {
                                playerData.set("raiting", playerData.getInt("raiting") + upd);
                            }
                            commandSender.sendMessage(Config.getMessage("givedStats"));
                        }
                        else commandSender.sendMessage("недостаточно аргументов, йоу");
                    }
                    else commandSender.sendMessage(Config.getMessage("noPerm"));
                }
                case "set" -> {
                    if (commandSender.hasPermission("saterino.admin")) {
                        if (strings.length >= 3 && !strings[1].isEmpty()) {
                            PlayerData playerData = PlayerData.getPlayerDataMap().get(strings[2]);
                            int upd = strings.length >= 4 && !strings[3].isEmpty() ? Integer.parseInt(strings[3]) : 1;
                            if (playerData == null) {
                                commandSender.sendMessage(Config.getMessage("dataIsNull"));
                                return true;
                            }

                            if (strings[1].equalsIgnoreCase("level")) {
                                if (upd >= Utils.getMaxLevel()) {
                                    commandSender.sendMessage(Config.getMessage("maxLevel"));
                                    return true;
                                }
                                else {
                                    playerData.set("level", upd);
                                }
                            }
                            else {
                                playerData.set("raiting", upd);
                            }
                            commandSender.sendMessage(Config.getMessage("givedStats"));
                        }
                        else commandSender.sendMessage("недостаточно аргументов, йоу");
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
            return List.of("open", "reload", "update", "add", "set");
        }
        if (strings.length == 2 && strings[0].equalsIgnoreCase("reload")) {
            return List.of("config", "mainMenu", "shopMenu", "itemsData", "sellerItems", "shopItems");
        }
        if (strings.length == 2 && (strings[0].equalsIgnoreCase("add") || strings[0].equalsIgnoreCase("set"))) {
            return List.of("raiting", "level");
        }
        if (strings.length == 3 && (strings[0].equalsIgnoreCase("add") || strings[0].equalsIgnoreCase("set"))) {
            List<String> list = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
            return list;
        }
        return List.of();
    }

    private void openMenu(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!PlayerData.getPlayerDataMap().containsKey(player.getName())) {
                new PlayerData(player.getName());
            }
            MenuManager.openInventory(player, new MainMenu(player,
                    MainMenuManager.getString("menu.title"), MainMenuManager.getInt("menu.rows"),
                        MainMenuManager.getSection("menu.items.decorations")));
        }
    }
}
