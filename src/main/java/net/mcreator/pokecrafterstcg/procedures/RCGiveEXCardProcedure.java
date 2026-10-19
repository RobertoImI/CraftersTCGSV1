package net.mcreator.pokecrafterstcg.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.pokecrafterstcg.init.PokecraftersTcgModItems;

public class RCGiveEXCardProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double cardroll = 0;
		cardroll = Mth.nextInt(RandomSource.create(), 1, 12);
		if (cardroll == 1) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.VENUSAUR_003.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 2) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.CHARIZARD_006.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 3) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.BLASTOISE_009.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 4) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ARBOK_024.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 5) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.NINETALES_038.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 6) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.WIGGLYTUFF_040.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 7) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ALAKAZAM_065.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 8) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.GOLEM_076.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 9) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.KANGASKHAN_115.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 10) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.JYNX_124.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 11) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ZAPDOS_145.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.MEW_151.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		}
	}
}