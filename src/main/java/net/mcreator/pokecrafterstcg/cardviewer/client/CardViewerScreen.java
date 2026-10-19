package net.mcreator.pokecrafterstcg.cardviewer.client;

import com.mojang.math.Axis;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


/**
 * Card Viewer HD.
 *
 * Controles:
 *
 * - Mantener clic izquierdo + arrastrar:
 *      inclinar la carta en X / Y.
 *
 * - Clic izquierdo corto:
 *      voltear frente / reverso.
 *
 * - Clic derecho / ESC:
 *      cerrar.
 *
 * V1:
 * solamente carta 001.
 */
public class CardViewerScreen extends Screen {

    /*
     * ============================================================
     * TEXTURAS
     * ============================================================
     */

    private static final ResourceLocation CARD_001_FRONT =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/fronts/001_front.png"
            );

    private static final ResourceLocation CARD_002_FRONT =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/fronts/002_front.png"
            );

    private static final ResourceLocation CARD_003_FRONT =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/fronts/003_front.png"
            );

    private static final ResourceLocation CARD_BACK =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/card_back.png"
            );


    /*
     * ============================================================
     * TAMAÑO REAL DEL PNG
     * ============================================================
     */

    private static final int TEXTURE_WIDTH = 330;
    private static final int TEXTURE_HEIGHT = 460;


    /*
     * ============================================================
     * TAMAÑO EN PANTALLA
     * ============================================================
     */

    private static final float MAX_SCREEN_HEIGHT_PERCENT = 0.72F;

    private static final int MAX_CARD_HEIGHT = 370;
    private static final int MIN_CARD_HEIGHT = 210;


    /*
     * ============================================================
     * ROTACIÓN MANUAL
     * ============================================================
     */

    /*
     * Rotación máxima permitida.
     *
     * Queremos movimiento elegante,
     * no poder dar vuelta completamente
     * la carta arrastrándola.
     */
    private static final float MAX_ROTATION_X = 14.0F;
    private static final float MAX_ROTATION_Y = 18.0F;


    /*
     * Sensibilidad del drag.
     *
     * Cuántos grados se modifican
     * por pixel arrastrado.
     */
    private static final float DRAG_SENSITIVITY = 0.14F;


    /*
     * Suavidad de la inclinación.
     */
    private static final float ROTATION_SMOOTHING = 0.18F;


    /*
     * Distancia mínima para considerar
     * que realmente hubo drag.
     *
     * Si no supera esto:
     * se considera un clic y hace flip.
     */
    private static final double DRAG_THRESHOLD = 4.0D;


    /*
     * ============================================================
     * FLIP
     * ============================================================
     */

    private static final float FLIP_SPEED = 0.14F;


    /*
     * ============================================================
     * APERTURA / CIERRE
     * ============================================================
     */

    private static final float OPEN_SPEED = 0.12F;
    private static final float CLOSE_SPEED = 0.16F;


    /*
     * ============================================================
     * ESTADO
     * ============================================================
     */

    private final int cardNumber;


    /*
     * Rotación actualmente renderizada.
     */
    private float rotationX = 0.0F;
    private float rotationY = 0.0F;


    /*
     * Rotación objetivo.
     *
     * Durante drag cambia con el mouse.
     *
     * Al soltar vuelve a 0.
     */
    private float targetRotationX = 0.0F;
    private float targetRotationY = 0.0F;


    /*
     * Flip.
     *
     * 0 = frente.
     * 1 = reverso.
     */
    private float flipProgress = 0.0F;

    private boolean showingBack = false;


    /*
     * Animación apertura.
     */
    private float openScale = 0.82F;

    private boolean closing = false;


    /*
     * ============================================================
     * DRAG
     * ============================================================
     */

    private boolean draggingCard = false;

    private double dragStartMouseX = 0.0D;
    private double dragStartMouseY = 0.0D;

    private double lastMouseX = 0.0D;
    private double lastMouseY = 0.0D;

    private boolean actuallyDragged = false;

    /*
     * Estado del holofoil: tiempo suave y energía de arrastre.
     * El efecto se aplica únicamente al frente de la 003.
     */
    private float holoTime = 0.0F;
    private float holoDragEnergy = 0.0F;
    private long lastHoloFrameNanos = 0L;


    public CardViewerScreen(
            int cardNumber
    ) {

        super(
                Component.literal(
                        "Card Viewer"
                )
        );

        this.cardNumber = cardNumber;
    }


    /*
     * ============================================================
     * RENDER
     * ============================================================
     */

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        /*
         * --------------------------------------------------------
         * APERTURA / CIERRE
         * --------------------------------------------------------
         */

        if (closing) {

            openScale =
                    lerp(
                            openScale,
                            0.0F,
                            CLOSE_SPEED
                    );

        } else {

            openScale =
                    lerp(
                            openScale,
                            1.0F,
                            OPEN_SPEED
                    );
        }


        if (closing
                && openScale < 0.03F) {

            Minecraft.getInstance()
                    .setScreen(null);

            return;
        }


        /*
         * --------------------------------------------------------
         * FONDO OSCURO
         * --------------------------------------------------------
         */

        int backgroundAlpha =
                Math.round(
                        175.0F
                                * openScale
                );

        backgroundAlpha =
                Math.max(
                        0,
                        Math.min(
                                255,
                                backgroundAlpha
                        )
                );

        graphics.fill(
                0,
                0,
                width,
                height,
                backgroundAlpha << 24
        );


        /*
         * --------------------------------------------------------
         * ROTACIÓN SUAVE
         * --------------------------------------------------------
         *
         * Cuando soltamos la carta,
         * targetRotation vuelve a 0.
         */

        rotationX =
                lerp(
                        rotationX,
                        targetRotationX,
                        ROTATION_SMOOTHING
                );

        rotationY =
                lerp(
                        rotationY,
                        targetRotationY,
                        ROTATION_SMOOTHING
                );


        /*
         * Avance independiente de los FPS del efecto holográfico.
         */
        long nowNanos = System.nanoTime();
        float elapsedSeconds = lastHoloFrameNanos == 0L
                ? 0.016F
                : Math.min(0.05F, Math.max(0.0F,
                        (nowNanos - lastHoloFrameNanos) / 1_000_000_000.0F));
        lastHoloFrameNanos = nowNanos;

        holoDragEnergy = Math.max(0.0F, holoDragEnergy - elapsedSeconds * 1.1F);
        holoTime += elapsedSeconds * (0.28F + holoDragEnergy * 0.55F);

        /*
         * --------------------------------------------------------
         * FLIP
         * --------------------------------------------------------
         */

        float targetFlip =
                showingBack
                        ? 1.0F
                        : 0.0F;

        flipProgress =
                lerp(
                        flipProgress,
                        targetFlip,
                        FLIP_SPEED
                );

        if (Math.abs(
                flipProgress - targetFlip
        ) < 0.001F) {

            flipProgress = targetFlip;
        }


        /*
         * --------------------------------------------------------
         * DIMENSIONES DE CARTA
         * --------------------------------------------------------
         */

        int cardHeight =
                getCardHeight();

        int cardWidth =
                getCardWidth(cardHeight);


        float centerX =
                width / 2.0F;

        float centerY =
                height / 2.0F - 6.0F;


        /*
         * --------------------------------------------------------
         * FLIP VISUAL
         * --------------------------------------------------------
         */

        float flipAngle =
                flipProgress * 180.0F;

        float radians =
                (float) Math.toRadians(
                        flipAngle
                );

        float flipScaleX =
                Math.abs(
                        (float) Math.cos(
                                radians
                        )
                );

        flipScaleX =
                Math.max(
                        0.015F,
                        flipScaleX
                );


        boolean renderBack =
                flipProgress >= 0.5F;


        ResourceLocation texture =
                renderBack
                        ? CARD_BACK
                        : getFrontTexture();


        /*
         * ========================================================
         * CARTA + SOMBRA
         * ========================================================
         *
         * La sombra utiliza exactamente las mismas transformaciones
         * que la carta.
         *
         * De esta forma, cuando agarramos la carta y la inclinamos,
         * la sombra gira junto con ella en X / Y.
         */

        graphics.pose().pushPose();


        /*
         * Nos colocamos en el centro.
         */
        graphics.pose().translate(
                centerX,
                centerY,
                100
        );


        /*
         * Animación de entrada / salida.
         */
        graphics.pose().scale(
                openScale,
                openScale,
                1.0F
        );


        /*
         * Rotación manual.
         *
         * IMPORTANTE:
         * estas rotaciones afectan tanto a la sombra
         * como a la carta.
         */
        graphics.pose().mulPose(
                Axis.XP.rotationDegrees(
                        rotationX
                )
        );

        graphics.pose().mulPose(
                Axis.YP.rotationDegrees(
                        rotationY
                )
        );


        /*
         * Flip frente / reverso.
         */
        graphics.pose().scale(
                flipScaleX,
                1.0F,
                1.0F
        );


        /*
         * Coordenadas locales de la carta.
         */
        int renderX =
                -cardWidth / 2;

        int renderY =
                -cardHeight / 2;


        /*
         * ========================================================
         * SOMBRA
         * ========================================================
         *
         * Ahora vive dentro del mismo espacio transformado
         * de la carta.
         *
         * Apenas 2 píxeles de desplazamiento.
         */

        graphics.pose().pushPose();


        /*
         * La movemos muy ligeramente.
         *
         * Z negativo relativo para mantenerla
         * visualmente detrás de la carta.
         */
        graphics.pose().translate(
                2.0F,
                2.0F,
                -1.0F
        );


        /*
         * Sombra muy suave.
         */
        graphics.fill(
                renderX,
                renderY,
                renderX + cardWidth,
                renderY + cardHeight,
                0x24000000
        );


        graphics.pose().popPose();


        /*
         * ========================================================
         * CARTA
         * ========================================================
         */

        graphics.blit(
                texture,

                renderX,
                renderY,

                cardWidth,
                cardHeight,

                0.0F,
                0.0F,

                TEXTURE_WIDTH,
                TEXTURE_HEIGHT,

                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );


        /*
         * Renderizamos el holofoil en la MISMA matriz que la carta.
         * Así acompaña inclinación y flip y NO invade el reverso.
         *
         * Primero vaciamos los buffers del PNG, luego el foil por encima.
         */
        if (HolofoilEffect.isHolofoil(cardNumber) && !renderBack) {
            graphics.flush();

            float tilt = Math.min(1.0F,
                    Math.abs(rotationX) / MAX_ROTATION_X * 0.48F
                            + Math.abs(rotationY) / MAX_ROTATION_Y * 0.65F);
            float activity = Math.min(1.0F, Math.max(tilt, holoDragEnergy));

            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 2.0F);
            HolofoilEffect.render(
                    graphics, renderX, renderY, cardWidth, cardHeight,
                    holoTime, activity, rotationX, rotationY
            );
            graphics.flush();
            graphics.pose().popPose();
        }

        graphics.pose().popPose();


        /*
         * ========================================================
         * INSTRUCCIONES
         * ========================================================
         */

        Component instructions =
                Component.literal(
                        "Arrastrar: Rotar   •   Clic: Voltear   •   Clic derecho / ESC: Cerrar"
                );

        graphics.drawCenteredString(
                this.font,
                instructions,
                width / 2,
                height - 22,
                0xFFFFFFFF
        );
    }


    /*
     * ============================================================
     * CLIC INICIAL
     * ============================================================
     */

    @Override
    public boolean mouseClicked(
            double mouseX,
            double mouseY,
            int button
    ) {

        if (closing) {
            return true;
        }


        /*
         * --------------------------------------------------------
         * CLIC IZQUIERDO
         * --------------------------------------------------------
         *
         * Si clicamos sobre la carta,
         * empezamos a "agarrarla".
         */

        if (button == 0
                && isMouseOverCard(
                mouseX,
                mouseY
        )) {

            draggingCard = true;

            actuallyDragged = false;

            dragStartMouseX = mouseX;
            dragStartMouseY = mouseY;

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            return true;
        }


        /*
         * --------------------------------------------------------
         * CLIC DERECHO
         * --------------------------------------------------------
         */

        if (button == 1) {

            onClose();

            return true;
        }


        return super.mouseClicked(
                mouseX,
                mouseY,
                button
        );
    }


    /*
     * ============================================================
     * DRAG
     * ============================================================
     */

    @Override
    public boolean mouseDragged(
            double mouseX,
            double mouseY,
            int button,
            double dragX,
            double dragY
    ) {

        if (button != 0
                || !draggingCard) {

            return super.mouseDragged(
                    mouseX,
                    mouseY,
                    button,
                    dragX,
                    dragY
            );
        }


        /*
         * Distancia total desde donde
         * empezamos a agarrar la carta.
         */
        double totalX =
                mouseX - dragStartMouseX;

        double totalY =
                mouseY - dragStartMouseY;


        double distance =
                Math.sqrt(
                        totalX * totalX
                                + totalY * totalY
                );


        if (distance >= DRAG_THRESHOLD) {

            actuallyDragged = true;
        }


        /*
         * Diferencia desde el último frame.
         */
        double deltaX =
                mouseX - lastMouseX;

        double deltaY =
                mouseY - lastMouseY;


        /*
         * Horizontal:
         *
         * arrastrar derecha
         * -> gira Y hacia derecha.
         */
        targetRotationY -=
                (float) deltaX
                        * DRAG_SENSITIVITY;


        /*
         * Vertical:
         *
         * arrastrar arriba/abajo
         * -> gira X.
         */
        targetRotationX -=
                (float) deltaY
                        * DRAG_SENSITIVITY;


        /*
         * Límites.
         */
        targetRotationX =
                clamp(
                        targetRotationX,
                        -MAX_ROTATION_X,
                        MAX_ROTATION_X
                );

        targetRotationY =
                clamp(
                        targetRotationY,
                        -MAX_ROTATION_Y,
                        MAX_ROTATION_Y
                );


        /*
         * Un arrastre rápido intensifica el destello.
         * Si se suelta, la energía baja gradualmente.
         */
        float dragSpeed = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        holoDragEnergy = Math.max(holoDragEnergy, Math.min(1.0F, dragSpeed / 12.0F));

        lastMouseX = mouseX;
        lastMouseY = mouseY;


        return true;
    }


    /*
     * ============================================================
     * SOLTAR CLIC
     * ============================================================
     */

    @Override
    public boolean mouseReleased(
            double mouseX,
            double mouseY,
            int button
    ) {

        if (button == 0
                && draggingCard) {

            draggingCard = false;


            /*
             * Si prácticamente no nos movimos,
             * fue un clic normal.
             *
             * Entonces volteamos.
             */
            if (!actuallyDragged) {

                showingBack =
                        !showingBack;
            }


            /*
             * Después de soltar,
             * la carta regresa suavemente
             * a su posición frontal.
             */
            targetRotationX = 0.0F;
            targetRotationY = 0.0F;


            return true;
        }


        return super.mouseReleased(
                mouseX,
                mouseY,
                button
        );
    }


    /*
     * ============================================================
     * HITBOX DE LA CARTA
     * ============================================================
     */

    private boolean isMouseOverCard(
            double mouseX,
            double mouseY
    ) {

        int cardHeight =
                getCardHeight();

        int cardWidth =
                getCardWidth(
                        cardHeight
                );


        float centerX =
                width / 2.0F;

        float centerY =
                height / 2.0F - 6.0F;


        float halfWidth =
                cardWidth
                        * openScale
                        / 2.0F;

        float halfHeight =
                cardHeight
                        * openScale
                        / 2.0F;


        return mouseX >= centerX - halfWidth
                && mouseX <= centerX + halfWidth
                && mouseY >= centerY - halfHeight
                && mouseY <= centerY + halfHeight;
    }


    /*
     * ============================================================
     * DIMENSIONES
     * ============================================================
     */

    private int getCardHeight() {

        int cardHeight =
                Math.round(
                        height
                                * MAX_SCREEN_HEIGHT_PERCENT
                );

        return Math.max(
                MIN_CARD_HEIGHT,
                Math.min(
                        cardHeight,
                        MAX_CARD_HEIGHT
                )
        );
    }


    private int getCardWidth(
            int cardHeight
    ) {

        return Math.round(
                cardHeight
                        * ((float) TEXTURE_WIDTH
                        / (float) TEXTURE_HEIGHT)
        );
    }


    /*
     * ============================================================
     * CERRAR
     * ============================================================
     */

    @Override
    public void onClose() {

        closing = true;
    }


    /*
     * ============================================================
     * SIN BLUR VANILLA
     * ============================================================
     */

    @Override
    public void renderBackground(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        /*
         * Vacío intencionalmente.
         */
    }


    /*
     * ============================================================
     * TEXTURA FRONTAL
     * ============================================================
     */

    private ResourceLocation getFrontTexture() {

        return switch (cardNumber) {
            case 2 -> CARD_002_FRONT;
            case 3 -> CARD_003_FRONT;
            default -> CARD_001_FRONT;
        };
    }


    /*
     * ============================================================
     * MULTIJUGADOR
     * ============================================================
     */

    @Override
    public boolean isPauseScreen() {

        return false;
    }


    /*
     * ============================================================
     * UTILIDADES
     * ============================================================
     */

    private static float lerp(
            float current,
            float target,
            float amount
    ) {

        return current
                + (target - current)
                * amount;
    }


    private static float clamp(
            float value,
            float minimum,
            float maximum
    ) {

        return Math.max(
                minimum,
                Math.min(
                        maximum,
                        value
                )
        );
    }
}