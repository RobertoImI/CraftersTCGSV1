package net.mcreator.pokecrafterstcg.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class Venusaur182Item extends Item {
	public Venusaur182Item() {
		super(new Item.Properties());
	}

	@Override
	public int getEnchantmentValue() {
		return 5;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}