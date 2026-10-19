package net.mcreator.pokecrafterstcg.binder;

import com.mojang.brigadier.CommandDispatcher;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Registra el comando:
 *
 * /binder
 *
 * No requiere permisos de operador.
 */
@EventBusSubscriber(
        modid = PokecraftersTcgMod.MODID
)
public final class BinderCommand {

    private BinderCommand() {
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {

        CommandDispatcher<CommandSourceStack> dispatcher =
                event.getDispatcher();

        dispatcher.register(

                Commands.literal("binder")

                        .executes(context -> {

                            ServerPlayer player =
                                    context.getSource().getPlayerOrException();

                            openBinder(player);

                            return 1;
                        })
        );
    }

    /**
     * Abre el Binder desde el servidor.
     *
     * La GUI NO se abre directamente desde el cliente.
     * Esto mantiene la arquitectura correcta para multijugador.
     */
    private static void openBinder(
            ServerPlayer player
    ) {

        player.openMenu(
                new SimpleMenuProvider(

                        (containerId,
                         inventory,
                         menuPlayer) ->

                                new BinderMenu(
                                        containerId,
                                        inventory
                                ),

                        Component.literal("")
                )
        );

        /*
         * Nos aseguramos de que el cliente reciba
         * los datos actuales justo al abrirlo.
         */
        BinderNetworking.syncToPlayer(
                player
        );
    }
}