package dev.drawethree.xprison.api.enchants.events;

import dev.drawethree.xprison.api.enchants.model.XPrisonEnchantment;
import dev.drawethree.xprison.api.shared.events.player.XPrisonPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an enchantment successfully triggers.
 */
public class XPrisonEnchantTriggerEvent extends XPrisonPlayerEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	private final XPrisonEnchantment enchantment;
	private final int level;

	public XPrisonEnchantTriggerEvent(Player player, XPrisonEnchantment enchantment, int level) {
		super(player);
		this.enchantment = enchantment;
		this.level = level;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	/**
	 * @return The enchantment that was triggered.
	 */
	public XPrisonEnchantment getEnchantment() {
		return enchantment;
	}

	/**
	 * @return The level of the enchantment on the pickaxe.
	 */
	public int getLevel() {
		return level;
	}
}
