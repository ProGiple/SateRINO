package org.comp.progiple.saterino.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Button {
    ItemStack getItemStack();
    byte getSlot();
    void onClick(Player player);
}
