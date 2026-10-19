package net.mcreator.pokecrafterstcg.binder;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server -> Client
 *
 * Sincroniza las cantidades del Binder.
 */
public record BinderSyncMessage(
        BinderData data
) implements CustomPacketPayload {

    public static final Type<BinderSyncMessage> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PokecraftersTcgMod.MODID,
                            "binder_sync"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            BinderSyncMessage
            > STREAM_CODEC =
            StreamCodec.of(

                    (buffer, message) ->

                            buffer.writeNbt(
                                    message.data()
                                            .serializeNBT(
                                                    buffer.registryAccess()
                                            )
                            ),

                    buffer -> {

                        BinderData data =
                                new BinderData();

                        data.deserializeNBT(
                                buffer.registryAccess(),
                                buffer.readNbt()
                        );

                        return new BinderSyncMessage(data);
                    }
            );

    @Override
    public Type<BinderSyncMessage> type() {
        return TYPE;
    }

    public static void handleData(
            BinderSyncMessage message,
            IPayloadContext context
    ) {

        if (context.flow()
                != PacketFlow.CLIENTBOUND) {
            return;
        }

        context.enqueueWork(() -> {

            BinderData localData =
                    context.player()
                            .getData(
                                    BinderAttachments.BINDER_DATA
                            );

            localData.copyFrom(
                    message.data()
            );

        }).exceptionally(exception -> {

            context.connection().disconnect(
                    Component.literal(
                            "Error sincronizando Binder: "
                                    + exception.getMessage()
                    )
            );

            return null;
        });
    }
}