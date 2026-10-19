package net.mcreator.pokecrafterstcg.binder;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Networking del Binder.
 *
 * Aprovechamos el sistema de networking que ya
 * contiene la clase principal del mod.
 */
@EventBusSubscriber(
        modid = PokecraftersTcgMod.MODID,
        bus = EventBusSubscriber.Bus.MOD
)
public final class BinderNetworking {

    private BinderNetworking() {
    }

    @SubscribeEvent
    public static void setup(
            FMLCommonSetupEvent event
    ) {

        PokecraftersTcgMod.addNetworkMessage(
                BinderSyncMessage.TYPE,
                BinderSyncMessage.STREAM_CODEC,
                BinderSyncMessage::handleData
        );

        PokecraftersTcgMod.addNetworkMessage(
                BinderActionMessage.TYPE,
                BinderActionMessage.STREAM_CODEC,
                BinderActionMessage::handleData
        );
    }

    public static void syncToPlayer(
            ServerPlayer player
    ) {

        BinderData data =
                player.getData(
                        BinderAttachments.BINDER_DATA
                );

        PacketDistributor.sendToPlayer(
                player,
                new BinderSyncMessage(data)
        );
    }
}