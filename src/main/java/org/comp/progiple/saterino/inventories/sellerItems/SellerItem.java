package org.comp.progiple.saterino.inventories.sellerItems;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.saterino.SateRINO;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.Menu;
import org.comp.progiple.saterino.others.configs.Config;
import org.comp.progiple.saterino.others.configs.ItemsData;
import org.comp.progiple.saterino.others.configs.PlayerData;
import org.comp.progiple.saterino.others.configs.SellerItemsConfig;
import org.example.novasparkle.Items.Item;
import org.example.novasparkle.Menus.AMenu;
import org.example.novasparkle.Menus.IMenu;
import org.example.novasparkle.Menus.MenuManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public class SellerItem extends Item implements Button {
    private final Material material;
    private final int fullRaiting;
    private final double fullCost;
    private final int amount;
    private final byte level;
    public SellerItem(Material material, String displayName, List<String> lore, byte slot, byte level, int count, double moneyPerOne) {
        super(material, displayName, lore, 1, slot);
        this.material = material;
        this.level = level;
        this.amount = count;
        ConfigurationSection section = SellerItemsConfig.getSection(String.format("items.%d.%s", this.level, material.name()));

        this.fullCost = BigDecimal.valueOf(moneyPerOne * this.amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.fullRaiting = (this.amount / section.getInt("raiting.per")) * section.getInt("raiting.amo");

        lore.replaceAll(line -> line
                .replace("$count", String.valueOf(this.amount))
                .replace("$cost", String.valueOf(this.fullCost))
                .replace("$raiting", String.valueOf(this.fullRaiting))
                .replace("$need_level", String.valueOf(this.level)));
        this.setLore(this.getLore());
    }

    @Override @SuppressWarnings("deprecation")
    public void onClick(Player player) {
        if (player.getInventory().contains(this.material, this.amount)) {
            int deletedAmount = this.amount;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null || item.getType() == Material.AIR) continue;
                byte byteAmount = (byte) item.getAmount();

                if (deletedAmount < byteAmount) byteAmount = (byte) deletedAmount;
                deletedAmount -= byteAmount;
                item.setAmount(item.getAmount() - byteAmount);
                if (deletedAmount <= 0) break;
            }
            SateRINO.getEconomy().depositPlayer(player, this.fullCost);

            PlayerData playerData = PlayerData.getPlayerDataMap().get(player.getName());
            playerData.set("raiting", this.fullRaiting);

            for (Map.Entry<Inventory, IMenu> entry : MenuManager.getActiveInventories().entrySet()) {
                IMenu iMenu = entry.getValue();
                if (iMenu instanceof Menu) {
                    AMenu aMenu = (AMenu) iMenu;
                    aMenu.getPlayer().closeInventory();
                }
            }

            byte index = 0;
            for (String key : ItemsData.getSection("").getKeys(false)) {
                if (ItemsData.getString(String.format("%s.material", key)).equalsIgnoreCase(this.material.name())
                    && ItemsData.getInt(String.format("%s.count", key)) == this.amount) {
                    index = Byte.parseByte(key);
                    break;
                }
            }
            if (index != 0) {
                ItemsData.updateItem(index, this.level, new Random());
                for (String line : Config.getMessageList("playerCompleteItem")) {
                    line = line.replace("$player", player.getName())
                            .replace("$task_id", String.valueOf(index))
                            .replace("$task_item_name", this.getDisplayName())
                            .replace("$task_item_id", this.material.name())
                            .replace("$task_item_count", String.valueOf(this.amount))
                            .replace("$task_item_cost", String.valueOf(this.fullCost));
                    Bukkit.getServer().broadcastMessage(line);
                }
            }
        }
        else player.sendMessage(Config.getMessage("noItems")
                .replace("$need_items", String.valueOf(this.amount)));
    }
}
