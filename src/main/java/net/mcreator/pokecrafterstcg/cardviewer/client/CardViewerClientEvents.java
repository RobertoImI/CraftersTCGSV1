package net.mcreator.pokecrafterstcg.cardviewer.client;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;
import net.mcreator.pokecrafterstcg.binder.BinderCardRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;


/**
 * Abre el Card Viewer cuando el jugador
 * hace clic derecho sosteniendo una carta.
 *
 * Por ahora funciona con las cartas 001, 002 y 003.
 */
@EventBusSubscriber(
        modid = PokecraftersTcgMod.MODID,
        value = Dist.CLIENT
)
public final class CardViewerClientEvents {

    private CardViewerClientEvents() {
    }


    @SubscribeEvent
    public static void onInteraction(
            InputEvent.InteractionKeyMappingTriggered event
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();


        /*
         * Debemos estar dentro de un mundo
         * y tener jugador.
         */
        if (minecraft.player == null
                || minecraft.level == null) {

            return;
        }


        /*
         * Si ya hay una pantalla abierta,
         * no hacemos nada.
         */
        if (minecraft.screen != null) {
            return;
        }


        /*
         * Solo queremos la acción de USAR.
         *
         * Clic derecho.
         */
        if (!event.isUseItem()) {
            return;
        }


        /*
         * Revisamos el item de la mano
         * desde la cual vino la interacción.
         */
        ItemStack stack =
                minecraft.player
                        .getItemInHand(
                                event.getHand()
                        );


        /*
         * Nuestro BinderCardRegistry ya sabe
         * que algo como:
         *
         * bulbasaur_001
         *
         * corresponde a la carta número 1.
         */
        int cardNumber =
                BinderCardRegistry
                        .getCardNumber(stack);


        /*
         * PRUEBA V1:
         *
         * Bulbasaur 001, Ivysaur 002 y Venusaur 003.
         */
        // V2: solo las cartas con textura HD disponible (001, 002, 003).
        // No interceptamos clic derecho de otros items.
        if (cardNumber < 1 || cardNumber > 3) {
            return;
        }


        /*
         * Evitamos que Minecraft procese además
         * el clic derecho normal del item.
         */
        event.setCanceled(true);


        /*
         * Abrimos el visor.
         */
        minecraft.setScreen(
                new CardViewerScreen(
                        cardNumber
                )
        );
    }
}