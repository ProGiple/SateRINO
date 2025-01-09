package org.comp.progiple.saterino.inventories.menus.shop.shopItems;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.saterino.inventories.ErrorItem;
import org.comp.progiple.saterino.others.configs.itemConfigs.ShopItemsManager;
import org.novasparkle.lunaspring.Menus.Items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ShopItemsSetter {
    private final List<Item> shopItemList = new ArrayList<>();
    public ShopItemsSetter(byte playerLevel) {
        ConfigurationSection errorSection = ShopItemsManager.getSection("noLevel_error_item");

        for (String key : ShopItemsManager.getSection("shopItems").getKeys(false)) {
            ConfigurationSection itemSection = ShopItemsManager.getSection(String.format("shopItems.%s", key));
            byte needLevel = (byte) itemSection.getInt("needLevel");
            if (needLevel > playerLevel) {
                String name = errorSection.getString("displayName");
                assert name != null;
                if (!name.isEmpty()) name = itemSection.getString("displayName");

                String stringMaterial = errorSection.getString("material");
                Material material = stringMaterial == null ? Material.getMaterial(Objects.requireNonNull(itemSection.getString("displayMaterial"))) : Material.getMaterial(stringMaterial);

                ErrorItem errorItem = new ErrorItem(material, name,
                        errorSection.getStringList("lore"), (byte) itemSection.getInt("slot"), needLevel);
                this.shopItemList.add(errorItem);
            }
            else this.shopItemList.add(new ShopItem(itemSection));
        }
    }
}
