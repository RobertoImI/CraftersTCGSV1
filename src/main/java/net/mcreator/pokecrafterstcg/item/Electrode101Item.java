package net.mcreator.pokecrafterstcg.item;

import net.minecraft.world.item.Item;

public class Electrode101Item extends Item {
	public Electrode101Item() {
		super(new Item.Properties());
	}

	@Override
	public int getEnchantmentValue() {
		return 5;
	}
}