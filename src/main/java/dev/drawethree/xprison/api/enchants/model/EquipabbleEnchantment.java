package dev.drawethree.xprison.api.enchants.model;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface EquipabbleEnchantment {

    void onEquip(Player player, ItemStack pickaxe, int level);
    void onUnequip(Player player, ItemStack pickaxe, int level);
}