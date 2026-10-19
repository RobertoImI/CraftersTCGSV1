package net.mcreator.pokecrafterstcg.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.pokecrafterstcg.init.PokecraftersTcgModItems;

public class RCGiveFullArtCardProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double cardroll = 0;
		cardroll = Mth.nextInt(RandomSource.create(), 1, 16);
		if (cardroll == 1) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.BULBASAUR_166.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 2) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.IVYSAUR_167.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 3) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.CHARMANDER_168.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 4) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.CHARMELEON_169.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 5) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.SQUIRTLE_170.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 6) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.WARTORTLE_171.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 7) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.CATERPIE_172.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 8) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.PIKACHU_173.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 9) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.NIDOKING_174.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 10) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.PSYDUCK_175.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 11) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.POLIWHIRL_176.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 12) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.MACHOKE_177.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 13) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.TANGELA_178.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 14) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.MRMIME_179.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 15) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.OMANYTE_180.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.DRAGONAIR_181.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		}
	}
}