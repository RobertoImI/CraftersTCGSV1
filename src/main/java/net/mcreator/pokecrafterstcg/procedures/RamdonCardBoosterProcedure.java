package net.mcreator.pokecrafterstcg.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.pokecrafterstcg.init.PokecraftersTcgModItems;

public class RamdonCardBoosterProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double roll = 0;
		double cardroll = 0;
		for (int index0 = 0; index0 < 5; index0++) {
			roll = Mth.nextInt(RandomSource.create(), 1, 10000);
			if (roll <= 9368) {
				RCGiveCommonCardProcedure.execute(entity);
			} else if (roll <= 9868) {
				RCGiveEXCardProcedure.execute(entity);
			} else if (roll <= 9948) {
				RCGiveFullArtCardProcedure.execute(entity);
			} else if (roll <= 9988) {
				RCGiveTrainerEXFACardProcedure.execute(entity);
			} else if (roll <= 9998) {
				RCGiveUltraCardProcedure.execute(entity);
			} else {
				RCGiveMegaHitCardProcedure.execute(entity);
			}
		}
		if (entity instanceof Player _player) {
			ItemStack _stktoremove = new ItemStack(PokecraftersTcgModItems.BOOSTER_151.get());
			_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
		}
	}
}