package net.mcreator.pokecrafterstcg.item;

import net.minecraft.world.item.Item;

public class Voltorb100Item extends Item {
	public Voltorb100Item() {
		super(new Item.Properties());
	}

	@Override
	public int getEnchantmentValue() {
		return 5;
	}
}