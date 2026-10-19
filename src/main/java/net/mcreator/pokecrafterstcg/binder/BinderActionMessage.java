package net.mcreator.pokecrafterstcg.binder;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client -> Server
 *
 * El cliente solamente solicita una acción.
 *
 * El servidor es quien valida:
 *
 * - qué carta es
 * - qué tiene el jugador
 * - cuánto se puede guardar
 * - cuánto se puede retirar
 */
public record BinderActionMessage(
        int cardNumber,
        byte action
) implements CustomPacketPayload {

    public static final byte DEPOSIT_ALL = 0;
    public static final byte DEPOSIT_ONE = 1;
    public static final byte WITHDRAW_ONE = 2;
    public static final byte WITHDRAW_STACK = 3;

    public static final Type<BinderActionMessage> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PokecraftersTcgMod.MODID,
                            "binder_action"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            BinderActionMessage
            > STREAM_CODEC =
            StreamCodec.of(

                    (buffer, message) -> {

                        buffer.writeVarInt(
                                message.cardNumber()
                        );

                        buffer.writeByte(
                                message.action()
                        );
                    },

                    buffer ->
                            new BinderActionMessage(
                                    buffer.readVarInt(),
                                    buffer.readByte()
                            )
            );

    @Override
    public Type<BinderActionMessage> type() {
        return TYPE;
    }

    public static void handleData(
            BinderActionMessage message,
            IPayloadContext context
    ) {

        if (context.flow()
                != PacketFlow.SERVERBOUND) {
            return;
        }

        context.enqueueWork(() -> {

            if (!(context.player()
                    instanceof ServerPlayer player)) {
                return;
            }

            /*
             * Importantísimo:
             * solamente aceptamos acciones si realmente
             * tiene abierto nuestro Binder.
             */
            if (!(player.containerMenu
                    instanceof BinderMenu binderMenu)) {
                return;
            }

            binderMenu.handleBinderAction(
                    message.cardNumber(),
                    message.action()
            );
        });
    }
}