package dev.drawethree.xprison.api.enchants.model;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface XPrisonEnchantment {

    int getId();

    String getRawName();

    String getName();

    String getGuiName();

    String getBase64();

    String getAuthor();

    Material getMaterial();

    Collection<String> getDescription();

    boolean isEnabled();

    int getGuiSlot();

    int getMaxLevel();

    long getCost();

    long getIncreaseCost();

    int getRequiredPickaxeLevel();

    boolean isMessagesEnabled();

    boolean isRefundEnabled();

    int getRefundGuiSlot();

    double getRefundPercentage();

    void onEquip(Player p, ItemStack pickAxe, int enchantLevel);

    void onUnequip(Player p, ItemStack pickAxe, int enchantLevel);

    void onBlockBreak(BlockBreakEvent e, int enchantLevel);

    void reload();
}