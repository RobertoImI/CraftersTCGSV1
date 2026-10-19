package net.mcreator.pokecrafterstcg.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.mcreator.pokecrafterstcg.procedures.RamdonCardBoosterProcedure;

public class Booster151Item extends Item {
	public Booster151Item() {
		super(new Item.Properties().rarity(Rarity.RARE));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		RamdonCardBoosterProcedure.execute(entity);
		return ar;
	}
}