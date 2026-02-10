package dev.drawethree.xprison.api.blocks;

import dev.drawethree.xprison.api.blocks.factory.MineBlockFactory;
import dev.drawethree.xprison.api.blocks.factory.impl.MineBlockFactoryImpl;

/**
 * API interface for the Blocks features.
 */
public interface XPrisonBlocksAPI {

	default MineBlockFactory getMineBlockFactory() {
		return new MineBlockFactoryImpl();
	}

}
