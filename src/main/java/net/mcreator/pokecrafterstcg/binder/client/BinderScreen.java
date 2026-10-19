package net.mcreator.pokecrafterstcg.binder.client;

import net.mcreator.pokecrafterstcg.binder.BinderActionMessage;
import net.mcreator.pokecrafterstcg.binder.BinderAttachments;
import net.mcreator.pokecrafterstcg.binder.BinderCardRegistry;
import net.mcreator.pokecrafterstcg.binder.BinderData;
import net.mcreator.pokecrafterstcg.binder.BinderMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Pantalla del Binder.
 *
 * 18 posiciones por vista:
 *
 * 9 izquierda
 * 9 derecha
 *
 * Ya no hay:
 *
 * - TCG BINDER
 * - COLLECTION
 * - campo "Ir a"
 *
 * Las cartas no obtenidas se muestran como referencia visual apagada.
 * Las obtenidas se muestran completamente a color.
 */
public class BinderScreen
        extends AbstractContainerScreen<BinderMenu> {

    private static final int GUI_WIDTH = 430;
    private static final int GUI_HEIGHT = 305;

    private static final int TOTAL_PAGES =
            BinderCardRegistry.TOTAL_PAGES;

    /*
     * ------------------------------------------------------------
     * PÁGINAS
     * ------------------------------------------------------------
     */

    private static final int LEFT_PAGE_X = 22;
    private static final int RIGHT_PAGE_X = 226;

    private static final int PAGE_Y = 12;

    private static final int PAGE_WIDTH = 182;
    private static final int PAGE_HEIGHT = 168;

    /*
     * ------------------------------------------------------------
     * POSICIONES DE CARTA
     * ------------------------------------------------------------
     */

    private static final int SLOT_WIDTH = 48;
    private static final int SLOT_HEIGHT = 50;

    private static final int COLUMN_GAP = 8;
    private static final int ROW_GAP = 3;

    /*
     * 3 slots de 48 = 144
     * 2 gaps de 8 = 16
     *
     * Total = 160
     *
     * Página = 182
     *
     * Sobran 22 px = 11 px por margen.
     *
     * De esta manera las columnas quedan
     * completamente centradas.
     */
    private static final int SIDE_CONTENT_WIDTH =
            SLOT_WIDTH * 3
                    + COLUMN_GAP * 2;

    private static final int CONTENT_MARGIN_X =
            (PAGE_WIDTH - SIDE_CONTENT_WIDTH)
                    / 2;

    /*
     * Imagen ligeramente más grande.
     *
     * Antes era 2.0F.
     */
    private static final float CARD_SCALE =
            2.35F;

    /*
     * Tamaño visual aproximado:
     *
     * 16 x 2.35 = 37.6 px
     */
    private static final float RENDERED_CARD_SIZE =
            16.0F * CARD_SCALE;

    private int currentPage = 0;

    private Button previousButton;
    private Button nextButton;

    public BinderScreen(
            BinderMenu menu,
            Inventory inventory,
            Component title
    ) {

        super(
                menu,
                inventory,
                title
        );

        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;

        /*
         * Ocultamos textos automáticos vanilla.
         */
        this.titleLabelY = 1000;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected void init() {

        super.init();

        /*
         * Flecha izquierda.
         *
         * Bajamos toda la navegación.
         */
        previousButton =
                Button.builder(
                                Component.literal("<"),
                                button ->
                                        previousPage()
                        )
                        .bounds(
                                leftPos + 145,
                                topPos + 187,
                                28,
                                20
                        )
                        .build();

        addRenderableWidget(
                previousButton
        );

        /*
         * Flecha derecha.
         */
        nextButton =
                Button.builder(
                                Component.literal(">"),
                                button ->
                                        nextPage()
                        )
                        .bounds(
                                leftPos + 257,
                                topPos + 187,
                                28,
                                20
                        )
                        .build();

        addRenderableWidget(
                nextButton
        );

        updateButtons();
    }

    private void previousPage() {

        if (currentPage > 0) {

            currentPage--;

            updateButtons();
        }
    }

    private void nextPage() {

        if (currentPage
                < TOTAL_PAGES - 1) {

            currentPage++;

            updateButtons();
        }
    }

    private void updateButtons() {

        if (previousButton != null) {

            previousButton.active =
                    currentPage > 0;
        }

        if (nextButton != null) {

            nextButton.active =
                    currentPage
                            < TOTAL_PAGES - 1;
        }
    }

    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {

        /*
         * Fondo general oscuro.
         */
        graphics.fill(
                leftPos,
                topPos,
                leftPos + imageWidth,
                topPos + imageHeight,
                0xEE161616
        );

        /*
         * Página izquierda.
         */
        graphics.fill(
                leftPos + LEFT_PAGE_X,
                topPos + PAGE_Y,
                leftPos + LEFT_PAGE_X
                        + PAGE_WIDTH,
                topPos + PAGE_Y
                        + PAGE_HEIGHT,
                0xFFE3DDCF
        );

        /*
         * Página derecha.
         */
        graphics.fill(
                leftPos + RIGHT_PAGE_X,
                topPos + PAGE_Y,
                leftPos + RIGHT_PAGE_X
                        + PAGE_WIDTH,
                topPos + PAGE_Y
                        + PAGE_HEIGHT,
                0xFFE3DDCF
        );

        /*
         * Lomo central.
         */
        graphics.fill(
                leftPos + 212,
                topPos + PAGE_Y,
                leftPos + 218,
                topPos + PAGE_Y
                        + PAGE_HEIGHT,
                0xFF363636
        );

        /*
         * Línea que separa Binder e inventario.
         */
        graphics.fill(
                leftPos + 20,
                topPos + 213,
                leftPos + 410,
                topPos + 215,
                0xFF555555
        );

        /*
         * Fondo inventario.
         */
        graphics.fill(
                leftPos + 126,
                topPos + 216,
                leftPos + 304,
                topPos + 302,
                0xFF242424
        );

        renderInventorySlotBackgrounds(
                graphics
        );
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        this.renderBackground(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        super.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        /*
         * Posiciones virtuales del Binder.
         */
        renderBinderCards(
                graphics,
                mouseX,
                mouseY
        );

        this.renderTooltip(
                graphics,
                mouseX,
                mouseY
        );
    }

    @Override
    protected void renderLabels(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {

        /*
         * ELIMINADOS:
         *
         * TCG BINDER
         * COLLECTION
         */

        /*
         * Indicador más abajo.
         */
        String pageText =
                "Página "
                        + (currentPage + 1)
                        + " / "
                        + TOTAL_PAGES;

        int width =
                font.width(pageText);

        graphics.drawString(
                font,
                pageText,
                (imageWidth - width) / 2,
                194,
                0xFFFFFF,
                false
        );

        graphics.drawString(
                font,
                "Inventario",
                134,
                211,
                0xFFFFFF,
                false
        );
    }

    /**
     * Renderiza las posiciones correspondientes
     * a la página actual.
     */
    private void renderBinderCards(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {

        BinderData binder =
                getClientBinderData();

        int firstIndex =
                currentPage
                        * BinderCardRegistry.CARDS_PER_PAGE;

        for (int localIndex = 0;
             localIndex
                     < BinderCardRegistry.CARDS_PER_PAGE;
             localIndex++) {

            int cardIndex =
                    firstIndex + localIndex;

            if (cardIndex
                    >= BinderCardRegistry.TOTAL_CARDS) {
                break;
            }

            int cardNumber =
                    cardIndex + 1;

            boolean rightPage =
                    localIndex >= 9;

            int indexOnSide =
                    rightPage
                            ? localIndex - 9
                            : localIndex;

            int row =
                    indexOnSide / 3;

            int column =
                    indexOnSide % 3;

            int pageX =
                    rightPage
                            ? RIGHT_PAGE_X
                            : LEFT_PAGE_X;

            /*
             * Esta fórmula centra perfectamente
             * las tres columnas dentro de la página.
             */
            int slotX =
                    pageX
                            + CONTENT_MARGIN_X
                            + column
                            * (SLOT_WIDTH
                            + COLUMN_GAP);

            int slotY =
                    PAGE_Y
                            + 5
                            + row
                            * (SLOT_HEIGHT
                            + ROW_GAP);

            renderCardSlot(
                    graphics,
                    binder,
                    cardNumber,
                    slotX,
                    slotY,
                    mouseX,
                    mouseY
            );
        }
    }

    private void renderCardSlot(
            GuiGraphics graphics,
            BinderData binder,
            int cardNumber,
            int slotX,
            int slotY,
            int mouseX,
            int mouseY
    ) {

        ItemStack card =
                BinderCardRegistry
                        .getCard(cardNumber);

        int stored =
                binder == null
                        ? 0
                        : binder.getCount(
                        cardNumber
                );

        boolean owned =
                stored > 0;

        /*
         * --------------------------------------------------------
         * ESPACIO / FUNDA DE LA CARTA
         * --------------------------------------------------------
         */

        /*
         * Borde.
         */
        graphics.fill(
                leftPos + slotX,
                topPos + slotY,
                leftPos + slotX
                        + SLOT_WIDTH,
                topPos + slotY
                        + SLOT_HEIGHT,
                0x55454545
        );

        /*
         * Interior gris claro.
         */
        graphics.fill(
                leftPos + slotX + 2,
                topPos + slotY + 2,
                leftPos + slotX
                        + SLOT_WIDTH - 2,
                topPos + slotY
                        + SLOT_HEIGHT - 2,
                0x55B4B0A7
        );

        if (!card.isEmpty()) {

            /*
             * Centramos el item matemáticamente
             * dentro de su espacio.
             */
            float iconOffset =
                    (SLOT_WIDTH - RENDERED_CARD_SIZE) / 2.0F;

            int iconX =
                    Math.round(slotX + iconOffset);

            int iconY =
                    slotY + 2;

            /*
             * ========================================================
             * MODO DE DIAGNÓSTICO
             * ========================================================
             *
             * SOLO renderizamos la carta si el Binder dice que el
             * jugador tiene al menos una copia almacenada.
             *
             * Si stored == 0, NO dibujamos la carta en absoluto.
             *
             * Esta prueba sirve para confirmar definitivamente si
             * la carta que vemos en pantalla viene de renderLargeItem()
             * o si está siendo dibujada desde otro lugar.
             */
            if (owned) {

                /*
                 * Carta obtenida:
                 * se muestra completamente a color.
                 */
                renderLargeItem(
                        graphics,
                        card,
                        leftPos + iconX,
                        topPos + iconY
                );

            } else {

                /*
                 * Carta NO obtenida:
                 *
                 * Ahora que ya comprobamos que renderLargeItem() es quien
                 * realmente dibuja la carta, podemos volver a mostrarla
                 * como referencia.
                 *
                 * IMPORTANTE:
                 * renderLargeItem() ya NO usa Z = 100. La carta se dibuja
                 * en el plano normal de la GUI, por lo que las capas que
                 * vienen después sí pueden cubrirla correctamente.
                 */
                renderLargeItem(
                        graphics,
                        card,
                        leftPos + iconX,
                        topPos + iconY
                );

                /*
                 * Terminamos el render del ItemStack antes de aplicar
                 * las capas visuales del estado "no obtenida".
                 */
                graphics.flush();

                /*
                 * MUY IMPORTANTE:
                 *
                 * GuiGraphics.renderItem() mete internamente el item
                 * en una profundidad alta para que quede delante de
                 * otros elementos 2D de la GUI.
                 *
                 * Por eso los overlays anteriores se veían solamente
                 * alrededor de la carta: estaban realmente DETRÁS.
                 *
                 * Aquí forzamos la capa de "no obtenida" muy por delante
                 * del render del ItemStack.
                 */
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, 300);

                /*
                 * Capa gris neutra sobre TODO el slot.
                 */
                graphics.fill(
                        leftPos + slotX + 2,
                        topPos + slotY + 2,
                        leftPos + slotX + SLOT_WIDTH - 2,
                        topPos + slotY + SLOT_HEIGHT - 2,
                        0x70A0A0A0
                );

                /*
                 * Capa negra semitransparente.
                 *
                 * La carta queda reconocible debajo, pero claramente
                 * apagada cuando todavía no está obtenida.
                 */
                graphics.fill(
                        leftPos + slotX + 2,
                        topPos + slotY + 2,
                        leftPos + slotX + SLOT_WIDTH - 2,
                        topPos + slotY + SLOT_HEIGHT - 2,
                        0x70000000
                );

                graphics.pose().popPose();
                graphics.flush();
            }
        }

        /*
         * --------------------------------------------------------
         * NÚMERO
         * --------------------------------------------------------
         *
         * Ahora se centra usando TODO el ancho
         * de la posición, no una coordenada fija.
         */

        String numberText =
                String.format(
                        "%03d",
                        cardNumber
                );

        int numberWidth =
                font.width(numberText);

        int numberX =
                slotX
                        + (SLOT_WIDTH
                        - numberWidth)
                        / 2;

        /*
         * El número siempre queda por encima de la mica.
         */
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 400);

        graphics.drawString(
                font,
                numberText,
                leftPos + numberX,
                topPos + slotY + 39,
                0xFF303030,
                false
        );

        graphics.pose().popPose();

        /*
         * Cantidad de duplicados.
         */
        if (stored > 1) {

            String amount =
                    "x" + stored;

            int amountWidth =
                    font.width(amount);

            graphics.drawString(
                    font,
                    amount,
                    leftPos
                            + slotX
                            + SLOT_WIDTH
                            - amountWidth
                            - 3,
                    topPos
                            + slotY
                            + 3,
                    0xFFFFFFFF,
                    true
            );
        }

        /*
         * Tooltip.
         */
        if (!card.isEmpty()
                && isMouseOverSlot(
                mouseX,
                mouseY,
                slotX,
                slotY
        )) {

            graphics.renderTooltip(
                    font,
                    card,
                    mouseX,
                    mouseY
            );
        }
    }

    /**
     * Detecta clics sobre las posiciones virtuales.
     */
    @Override
    public boolean mouseClicked(
            double mouseX,
            double mouseY,
            int button
    ) {

        int cardNumber =
                getCardAtMouse(
                        mouseX,
                        mouseY
                );

        if (cardNumber != -1) {

            Minecraft minecraft =
                    Minecraft.getInstance();

            if (minecraft.player == null) {
                return true;
            }

            ItemStack carried =
                    this.menu.getCarried();

            /*
             * ----------------------------------------------------
             * GUARDAR
             * ----------------------------------------------------
             */

            if (!carried.isEmpty()) {

                /*
                 * Solamente enviamos petición si corresponde
                 * con esa posición.
                 */
                if (BinderCardRegistry.matches(
                        cardNumber,
                        carried
                )) {

                    byte action =
                            button == 1

                                    ? BinderActionMessage
                                    .DEPOSIT_ONE

                                    : BinderActionMessage
                                    .DEPOSIT_ALL;

                    sendAction(
                            cardNumber,
                            action
                    );
                }

                return true;
            }

            /*
             * ----------------------------------------------------
             * RETIRAR
             * ----------------------------------------------------
             */

            BinderData binder =
                    getClientBinderData();

            if (binder == null
                    || binder.getCount(cardNumber)
                    <= 0) {
                return true;
            }

            if (button == 0) {

                byte action =
                        Screen.hasShiftDown()

                                ? BinderActionMessage
                                .WITHDRAW_STACK

                                : BinderActionMessage
                                .WITHDRAW_ONE;

                sendAction(
                        cardNumber,
                        action
                );

                return true;
            }
        }

        return super.mouseClicked(
                mouseX,
                mouseY,
                button
        );
    }

    /**
     * Envía una solicitud de acción del Binder
     * desde el cliente hacia el servidor.
     *
     * El servidor seguirá siendo quien valida
     * realmente la operación.
     */
    private void sendAction(
            int cardNumber,
            byte action
    ) {

        PacketDistributor.sendToServer(
                new BinderActionMessage(
                        cardNumber,
                        action
                )
        );
    }

    /**
     * Obtiene la carta situada debajo
     * del cursor.
     */
    private int getCardAtMouse(
            double mouseX,
            double mouseY
    ) {

        int firstIndex =
                currentPage
                        * BinderCardRegistry.CARDS_PER_PAGE;

        for (int localIndex = 0;
             localIndex
                     < BinderCardRegistry.CARDS_PER_PAGE;
             localIndex++) {

            int cardIndex =
                    firstIndex + localIndex;

            if (cardIndex
                    >= BinderCardRegistry.TOTAL_CARDS) {
                break;
            }

            boolean rightPage =
                    localIndex >= 9;

            int indexOnSide =
                    rightPage
                            ? localIndex - 9
                            : localIndex;

            int row =
                    indexOnSide / 3;

            int column =
                    indexOnSide % 3;

            int pageX =
                    rightPage
                            ? RIGHT_PAGE_X
                            : LEFT_PAGE_X;

            int slotX =
                    pageX
                            + CONTENT_MARGIN_X
                            + column
                            * (SLOT_WIDTH
                            + COLUMN_GAP);

            int slotY =
                    PAGE_Y
                            + 5
                            + row
                            * (SLOT_HEIGHT
                            + ROW_GAP);

            if (isMouseOverSlot(
                    mouseX,
                    mouseY,
                    slotX,
                    slotY
            )) {

                return cardIndex + 1;
            }
        }

        return -1;
    }

    private boolean isMouseOverSlot(
            double mouseX,
            double mouseY,
            int slotX,
            int slotY
    ) {

        int screenX =
                leftPos + slotX;

        int screenY =
                topPos + slotY;

        return mouseX >= screenX
                && mouseX
                < screenX + SLOT_WIDTH
                && mouseY >= screenY
                && mouseY
                < screenY + SLOT_HEIGHT;
    }

    private BinderData getClientBinderData() {

        Minecraft minecraft =
                Minecraft.getInstance();

        if (minecraft.player == null) {
            return null;
        }

        return minecraft.player.getData(
                BinderAttachments.BINDER_DATA
        );
    }

    private void renderLargeItem(
            GuiGraphics graphics,
            ItemStack stack,
            int screenX,
            int screenY
    ) {

        graphics.pose().pushPose();

        /*
         * No elevamos artificialmente la carta en Z.
         * Dejamos que GuiGraphics maneje el orden normal de la GUI.
         */
        graphics.pose().translate(
                screenX,
                screenY,
                0
        );

        graphics.pose().scale(
                CARD_SCALE,
                CARD_SCALE,
                1.0F
        );

        graphics.renderItem(
                stack,
                0,
                0
        );

        graphics.pose().popPose();
    }

    private void renderInventorySlotBackgrounds(
            GuiGraphics graphics
    ) {

        int startX =
                BinderMenu.PLAYER_INVENTORY_X;

        int startY =
                BinderMenu.PLAYER_INVENTORY_Y;

        for (int row = 0;
             row < 3;
             row++) {

            for (int column = 0;
                 column < 9;
                 column++) {

                drawInventorySlot(
                        graphics,
                        startX
                                + column * 18,
                        startY
                                + row * 18
                );
            }
        }

        for (int column = 0;
             column < 9;
             column++) {

            drawInventorySlot(
                    graphics,
                    startX
                            + column * 18,
                    startY + 58
            );
        }
    }

    private void drawInventorySlot(
            GuiGraphics graphics,
            int x,
            int y
    ) {

        graphics.fill(
                leftPos + x - 1,
                topPos + y - 1,
                leftPos + x + 17,
                topPos + y + 17,
                0xFF555555
        );

        graphics.fill(
                leftPos + x,
                topPos + y,
                leftPos + x + 16,
                topPos + y + 16,
                0xFF202020
        );
    }
}