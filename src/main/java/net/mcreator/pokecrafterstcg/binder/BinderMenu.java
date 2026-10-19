package net.mcreator.pokecrafterstcg.binder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Backend del Binder.
 *
 * Los slots del inventario son vanilla.
 *
 * Las 207 posiciones del Binder son virtuales y
 * se almacenan mediante BinderData.
 */
public class BinderMenu
        extends AbstractContainerMenu {

    public static final int PLAYER_INVENTORY_X = 134;
    public static final int PLAYER_INVENTORY_Y = 220;

    private final Player player;

    public BinderMenu(
            int containerId,
            Inventory playerInventory
    ) {

        super(
                BinderMenus.BINDER_MENU.get(),
                containerId
        );

        this.player =
                playerInventory.player;

        addPlayerInventory(
                playerInventory
        );
    }

    private void addPlayerInventory(
            Inventory inventory
    ) {

        /*
         * Inventario principal.
         */
        for (int row = 0; row < 3; row++) {

            for (int column = 0;
                 column < 9;
                 column++) {

                int slotIndex =
                        column
                                + row * 9
                                + 9;

                int x =
                        PLAYER_INVENTORY_X
                                + column * 18;

                int y =
                        PLAYER_INVENTORY_Y
                                + row * 18;

                addSlot(
                        new Slot(
                                inventory,
                                slotIndex,
                                x,
                                y
                        )
                );
            }
        }

        /*
         * Hotbar.
         */
        for (int column = 0;
             column < 9;
             column++) {

            int x =
                    PLAYER_INVENTORY_X
                            + column * 18;

            int y =
                    PLAYER_INVENTORY_Y
                            + 58;

            addSlot(
                    new Slot(
                            inventory,
                            column,
                            x,
                            y
                    )
            );
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    /**
     * Shift-click desde inventario.
     *
     * Si el item es una carta:
     * la guardamos completa directamente.
     */
    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return ItemStack.EMPTY;
        }

        if (index < 0
                || index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot =
                this.slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack =
                slot.getItem();

        int cardNumber =
                BinderCardRegistry
                        .getCardNumber(stack);

        if (cardNumber == -1) {
            return ItemStack.EMPTY;
        }

        BinderData data =
                serverPlayer.getData(
                        BinderAttachments.BINDER_DATA
                );

        int amount =
                stack.getCount();

        int added =
                data.add(
                        cardNumber,
                        amount
                );

        if (added <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack original =
                stack.copy();

        stack.shrink(added);

        slot.setChanged();

        this.broadcastChanges();

        BinderNetworking.syncToPlayer(
                serverPlayer
        );

        return original;
    }

    /**
     * Acciones realizadas al hacer clic
     * sobre una posición virtual.
     */
    public void handleBinderAction(
            int cardNumber,
            byte action
    ) {

        if (!(player
                instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (cardNumber < 1
                || cardNumber
                > BinderCardRegistry.TOTAL_CARDS) {
            return;
        }

        BinderData data =
                serverPlayer.getData(
                        BinderAttachments.BINDER_DATA
                );

        switch (action) {

            case BinderActionMessage.DEPOSIT_ALL ->
                    depositFromCursor(
                            serverPlayer,
                            data,
                            cardNumber,
                            false
                    );

            case BinderActionMessage.DEPOSIT_ONE ->
                    depositFromCursor(
                            serverPlayer,
                            data,
                            cardNumber,
                            true
                    );

            case BinderActionMessage.WITHDRAW_ONE ->
                    withdrawToInventory(
                            serverPlayer,
                            data,
                            cardNumber,
                            1
                    );

            case BinderActionMessage.WITHDRAW_STACK ->
                    withdrawToInventory(
                            serverPlayer,
                            data,
                            cardNumber,
                            64
                    );
        }
    }

    /**
     * Guarda cartas actualmente transportadas
     * por el cursor.
     */
    private void depositFromCursor(
            ServerPlayer player,
            BinderData data,
            int cardNumber,
            boolean onlyOne
    ) {

        ItemStack carried =
                this.getCarried();

        /*
         * El servidor comprueba que la carta
         * realmente sea la correspondiente.
         */
        if (!BinderCardRegistry.matches(
                cardNumber,
                carried
        )) {
            return;
        }

        int requested =
                onlyOne
                        ? 1
                        : carried.getCount();

        int added =
                data.add(
                        cardNumber,
                        requested
                );

        if (added <= 0) {
            return;
        }

        carried.shrink(added);

        if (carried.isEmpty()) {
            this.setCarried(ItemStack.EMPTY);
        }

        this.broadcastChanges();

        BinderNetworking.syncToPlayer(
                player
        );
    }

    /**
     * Retira cartas del Binder.
     *
     * Nunca descontamos primero.
     *
     * Primero intentamos meterlas en inventario,
     * calculamos cuántas realmente entraron y
     * solamente entonces las retiramos del Binder.
     */
    private void withdrawToInventory(
            ServerPlayer player,
            BinderData data,
            int cardNumber,
            int requested
    ) {

        int stored =
                data.getCount(cardNumber);

        if (stored <= 0) {
            return;
        }

        int amount =
                Math.min(
                        requested,
                        stored
                );

        ItemStack template =
                BinderCardRegistry
                        .getCard(cardNumber);

        if (template.isEmpty()) {
            return;
        }

        ItemStack toInsert =
                template.copy();

        /*
         * Respetamos el tamaño máximo real
         * del item.
         */
        amount =
                Math.min(
                        amount,
                        toInsert.getMaxStackSize()
                );

        toInsert.setCount(amount);

        int before =
                toInsert.getCount();

        player.getInventory().add(
                toInsert
        );

        int remaining =
                toInsert.isEmpty()
                        ? 0
                        : toInsert.getCount();

        int inserted =
                before - remaining;

        if (inserted <= 0) {
            return;
        }

        data.remove(
                cardNumber,
                inserted
        );

        this.broadcastChanges();

        BinderNetworking.syncToPlayer(
                player
        );
    }
}