package net.mcreator.pokecrafterstcg.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.pokecrafterstcg.init.PokecraftersTcgModItems;

public class RCGiveUltraCardProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double cardroll = 0;
		cardroll = Mth.nextInt(RandomSource.create(), 1, 7);
		if (cardroll == 1) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.VENUSAUR_198.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 2) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.CHARIZARD_199.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 3) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.BLASTOISE_200.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 4) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ALAKAZAM_201.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 5) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ZAPDOS_202.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 6) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ERIKASINVITATION_203.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.GIOVANNISCHARISMA_204.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		}
	}
}