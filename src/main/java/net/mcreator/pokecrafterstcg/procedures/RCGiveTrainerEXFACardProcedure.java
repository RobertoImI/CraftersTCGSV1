package net.mcreator.pokecrafterstcg.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.pokecrafterstcg.init.PokecraftersTcgModItems;

public class RCGiveTrainerEXFACardProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double cardroll = 0;
		cardroll = Mth.nextInt(RandomSource.create(), 1, 16);
		if (cardroll == 1) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.VENUSAUR_182.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 2) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.CHARIZARD_183.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 3) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.BLASTOISE_184.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 4) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ARBOK_185.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 5) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.NINETALES_186.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 6) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.WIGGLYTUFF_187.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 7) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ALAKAZAM_188.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 8) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.GOLEM_189.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 9) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.KANGASKHAN_190.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 10) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.JYNX_191.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 11) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ZAPDOS_192.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 12) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.MEW_193.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 13) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.BILLSTRANSFER_194.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 14) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.DAISYSHELP_195.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (cardroll == 15) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.ERIKASINVITATION_196.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(PokecraftersTcgModItems.GIOVANNISCHARISMA_197.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		}
	}
}