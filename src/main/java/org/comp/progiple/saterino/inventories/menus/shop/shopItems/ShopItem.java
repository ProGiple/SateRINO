package org.comp.progiple.saterino.inventories.menus.shop.shopItems;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.others.Utils;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.example.novasparkle.Items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ShopItem extends Item implements Button {
    private final byte slot;
    private final int cost;
    private final ShopItemMode mode;
    // Mode
    private List<String> commands = new ArrayList<>();
    private Material modeItemMaterial;
    private byte modeItemAmount;
    public ShopItem(ConfigurationSection itemSection) {
        super(Material.STONE, 52);

        this.slot = (byte) itemSection.getInt("slot");
        this.cost = itemSection.getInt("raitingCost");
        this.mode = ShopItemMode.valueOf(itemSection.getString("mode.type"));

        Material material = Material.getMaterial(Objects.requireNonNull(itemSection.getString("displayMaterial")));
        int amount = itemSection.getInt("displayAmount");
        String displayName = Utils.color(itemSection.getString("displayName"));

        List<String> lore = itemSection.getStringList("displayLore");
        lore.replaceAll(line -> Utils.color(line.replace("$cost", String.valueOf(this.cost))));
        this.setAll(material, amount, displayName, lore, false);

        if (this.mode == ShopItemMode.COMMAND) this.commands = itemSection.getStringList("mode.commands");
        else {
            this.modeItemMaterial = Material.getMaterial(Objects.requireNonNull(itemSection.getString("mode.item")));
            this.modeItemAmount = (byte) itemSection.getInt("mode.amount");
        }
    }

    @Override
    public void onClick(Player player) {
        PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
        int raiting = playerData.getInt("raiting");

        if (raiting >= this.cost) {
            player.sendMessage(Config.getMessage("buyItem"));
            playerData.set("raiting", raiting - this.cost);

            if (this.mode == ShopItemMode.ITEM) {
                ItemStack item = new ItemStack(this.modeItemMaterial, this.modeItemAmount);
                player.getInventory().addItem(item);
            }
            else {
                this.commands.forEach(command -> {
                    String dispatchedCommand = command
                            .replace("player_execute:", "")
                            .replace("console_execute:", "");
                    if (command.equalsIgnoreCase("player_execute:")) {
                        Bukkit.dispatchCommand(player, dispatchedCommand);
                    }

                    if (command.equalsIgnoreCase("console_execute:")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), dispatchedCommand);
                    }
                });
            }
        }
        else player.sendMessage(Config.getMessage("noRaiting").replace("$need_raiting", String.valueOf(this.cost - raiting)));
    }
}
