package net.mcreator.pokecrafterstcg.binder;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registro de los MenuType utilizados por el Binder.
 *
 * Esta clase es nuestra y no será regenerada por MCreator.
 */
public final class BinderMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    Registries.MENU,
                    PokecraftersTcgMod.MODID
            );

    public static final Supplier<MenuType<BinderMenu>> BINDER_MENU =
            MENUS.register(
                    "binder",
                    () -> new MenuType<>(
                            BinderMenu::new,
                            FeatureFlags.DEFAULT_FLAGS
                    )
            );

    private BinderMenus() {
    }
}