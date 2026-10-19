package net.mcreator.pokecrafterstcg.binder;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Attachment persistente exclusivo del Binder.
 *
 * NO utilizamos PokecraftersTcgModVariables.
 *
 * De esta manera MCreator puede regenerar sus variables
 * sin tocar nuestros datos.
 */
@EventBusSubscriber(
        modid = PokecraftersTcgMod.MODID
)
public final class BinderAttachments {

    public static final DeferredRegister<AttachmentType<?>>
            ATTACHMENT_TYPES =
            DeferredRegister.create(
                    NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                    PokecraftersTcgMod.MODID
            );

    public static final Supplier<AttachmentType<BinderData>>
            BINDER_DATA =
            ATTACHMENT_TYPES.register(
                    "binder_data",
                    () -> AttachmentType
                            .serializable(BinderData::new)
                            .build()
            );

    private BinderAttachments() {
    }

    /**
     * Copiamos el Binder cuando Minecraft crea
     * una nueva instancia del Player.
     *
     * Esto cubre principalmente muerte/respawn.
     *
     * Como hacemos una copia completa y no sumamos datos,
     * no existe duplicación.
     */
    @SubscribeEvent
    public static void clonePlayer(
            PlayerEvent.Clone event
    ) {

        BinderData original =
                event.getOriginal()
                        .getData(BINDER_DATA);

        BinderData clone =
                new BinderData();

        clone.copyFrom(original);

        event.getEntity().setData(
                BINDER_DATA,
                clone
        );
    }

    /**
     * Sincroniza al entrar al servidor.
     */
    @SubscribeEvent
    public static void login(
            PlayerEvent.PlayerLoggedInEvent event
    ) {

        if (event.getEntity()
                instanceof ServerPlayer player) {

            BinderNetworking.syncToPlayer(player);
        }
    }

    /**
     * Sincroniza después de reaparecer.
     */
    @SubscribeEvent
    public static void respawn(
            PlayerEvent.PlayerRespawnEvent event
    ) {

        if (event.getEntity()
                instanceof ServerPlayer player) {

            BinderNetworking.syncToPlayer(player);
        }
    }

    /**
     * Sincroniza al cambiar dimensión.
     */
    @SubscribeEvent
    public static void changeDimension(
            PlayerEvent.PlayerChangedDimensionEvent event
    ) {

        if (event.getEntity()
                instanceof ServerPlayer player) {

            BinderNetworking.syncToPlayer(player);
        }
    }
}