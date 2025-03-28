package org.comp.progiple.saterino.inventories.menus.main.sellerItems;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.saterino.inventories.ErrorItem;
import org.comp.progiple.saterino.others.configs.itemConfigs.ItemsDataManager;
import org.comp.progiple.saterino.others.configs.itemConfigs.SellerItemsManager;
import org.novasparkle.lunaspring.Menus.Items.Item;

import java.util.*;

@Getter
public class SellerItemsSetter {
    private final List<Item> sellerItemList = new ArrayList<>();
    public SellerItemsSetter(LinkedHashMap<Byte, Byte> slotToLevelMap, byte playerLevel) {
        ConfigurationSection successfulSection = SellerItemsManager.getSection("staticItems.successful");
        ConfigurationSection errorSection = SellerItemsManager.getSection("staticItems.error");

        int i = 0;
        for (Map.Entry<Byte, Byte> entry : slotToLevelMap.entrySet()) {
            i++;
            byte slot = entry.getKey();
            byte level = entry.getValue();
            System.out.println(i);
            System.out.println(slot);
            System.out.println(level);

            ConfigurationSection itemSection = ItemsDataManager.getSection(String.valueOf(i));
            System.out.println(itemSection);
            String id = itemSection.getString("material");
            if (id == null) continue;
            ConfigurationSection sellerItemSection = SellerItemsManager.getSection(String.format("items.%s.%s", String.valueOf(level), id));
            if (sellerItemSection == null) continue;

            Material material = Material.getMaterial(id);
            String name = successfulSection.getString("displayName");
            if (name == null || name.isEmpty()) name = sellerItemSection.getString("displayName");;

            List<String> lore = successfulSection.getStringList("lore");
            int count = itemSection.getInt("count");
            double moneyPerOne = itemSection.getDouble("moneyPerOne");

            if (level > playerLevel) {
                String errorMaterialString = errorSection.getString("material");
                if (errorMaterialString != null && !errorMaterialString.isEmpty()) material = Material.getMaterial(errorMaterialString);

                String errorName = errorSection.getString("displayName");
                if (errorName != null && !errorName.isEmpty()) name = errorName;
                lore = errorSection.getStringList("lore");

                ErrorItem errorItem = new ErrorItem(material, name, lore, slot, level);
                this.sellerItemList.add(errorItem);
                continue;
            }

            SellerItem sellerItem = new SellerItem(material, name, lore, slot, level, count, moneyPerOne);
            this.sellerItemList.add(sellerItem);
        }
    }
}
