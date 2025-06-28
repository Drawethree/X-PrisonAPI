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

    String getNameWithoutColor();

    int getGuiSlot();

    Material getGuiMaterial();

    String getGuiName();

    String getGuiBase64();

    String getAuthor();

    Collection<String> getGuiDescription();

    boolean isEnabled();

    int getMaxLevel();

    long getBaseCost();

    long getIncreaseCost();

    int getRequiredPickaxeLevel();

    boolean isRefundEnabled();

    int getRefundGuiSlot();

    double getRefundPercentage();

    void reload();
}