package org.comp.progiple.saterino;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.comp.progiple.saterino.inventories.menus.main.MainMenu;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.itemConfigs.ItemsData;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.comp.progiple.saterino.others.configs.itemConfigs.ShopItemsConfig;
import org.comp.progiple.saterino.others.configs.menuConfigs.MainMenuConfig;
import org.comp.progiple.saterino.others.configs.itemConfigs.SellerItemsConfig;
import org.comp.progiple.saterino.others.configs.menuConfigs.ShopMenuConfig;
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
                        if (strings.length >= 2 && !strings[1].isEmpty()) {
                            switch (strings[1]) {
                                case "config" -> Config.reload();
                                case "mainMenu" -> MainMenuConfig.reload();
                                case "shopMenu" -> ShopMenuConfig.reload();
                                case "itemsData" -> ItemsData.reload();
                                case "sellerItems" -> SellerItemsConfig.reload();
                                case "shopItems" -> ShopItemsConfig.reload();
                            }
                        }
                        else {
                            Config.reload();
                            MainMenuConfig.reload();
                            ShopMenuConfig.reload();
                            ItemsData.reload();
                            SellerItemsConfig.reload();
                            ShopItemsConfig.reload();
                        }
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
        if (strings.length == 2 && strings[0].equalsIgnoreCase("reload")) {
            return List.of("config", "mainMenu", "shopMenu", "itemsData", "sellerItems", "shopItems");
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
                    MainMenuConfig.getString("menu.title"), MainMenuConfig.getInt("menu.rows"),
                        MainMenuConfig.getSection("menu.items.decorations")));
        }
    }
}
