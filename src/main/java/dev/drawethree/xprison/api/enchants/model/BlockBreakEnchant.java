package dev.drawethree.xprison.api.enchants.model;

import org.bukkit.event.block.BlockBreakEvent;

public interface BlockBreakEnchant {
    void onBlockBreak(BlockBreakEvent event, int enchantLevel);

}