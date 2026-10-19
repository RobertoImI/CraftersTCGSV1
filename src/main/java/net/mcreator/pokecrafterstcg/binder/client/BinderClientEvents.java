package net.mcreator.pokecrafterstcg.binder.client;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;
import net.mcreator.pokecrafterstcg.binder.BinderMenus;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Eventos exclusivos del CLIENTE.
 *
 * Es importante mantener esto separado para que un servidor dedicado
 * no intente cargar clases gráficas de Minecraft.
 */
@EventBusSubscriber(
        modid = PokecraftersTcgMod.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class BinderClientEvents {

    private BinderClientEvents() {
    }

    @SubscribeEvent
    public static void registerScreens(
            RegisterMenuScreensEvent event
    ) {

        event.register(
                BinderMenus.BINDER_MENU.get(),
                BinderScreen::new
        );
    }
}