package net.mcreator.pokecrafterstcg.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class Bulbasaur001Item extends Item {
	public Bulbasaur001Item() {
		super(new Item.Properties());
	}

	@Override
	public boolean isPiglinCurrency(ItemStack stack) {
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 10;
	}
}