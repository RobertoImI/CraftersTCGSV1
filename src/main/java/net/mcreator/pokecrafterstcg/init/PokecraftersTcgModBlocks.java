/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.pokecrafterstcg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.pokecrafterstcg.block.BoosterBundle151Block;
import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

public class PokecraftersTcgModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(PokecraftersTcgMod.MODID);
	public static final DeferredBlock<Block> BOOSTER_BUNDLE_151;
	static {
		BOOSTER_BUNDLE_151 = REGISTRY.register("booster_bundle_151", BoosterBundle151Block::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}