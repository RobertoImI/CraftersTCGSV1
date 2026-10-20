package net.mcreator.pokecrafterstcg.cardviewer.client;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Holofoil generado totalmente por código para el visor de cartas HD.
 *
 * No necesita PNG, shader adicional ni cambios en el Binder.
 * La pantalla ya dibuja el frente de la carta y después invoca
 * este render con su misma matriz de inclinación (X/Y) y flip.
 *
 * Diseño:
 * - Un reflejo irisado DIAGONAL concentrado en la ilustración.
 * - Una línea de luz blanca muy sutil dentro del reflejo.
 * - Destellos puntuales en la ilustración, no una capa opaca.
 * - Animación lenta en reposo y respuesta intensa al arrastrar.
 *
 * Ningún píxel del efecto es opaco: los colores se mezclan
 * mediante alfa de GuiGraphics.fill (ARGB), sin cambiar estados
 * globales de RenderSystem.
 */
public final class HolofoilEffect {

    private static final int ROW_STEP = 5;
    private static final int STRIPE_SEGMENTS = 15;
    private static final int SPARKLE_COUNT = 23;

    // Gama suave: perla, cian, lavanda, rosa, oro y menta.
    private static final int[] SPECTRUM = {
            0xB3E6FF, 0xA9DBFF, 0xB6B5FF,
            0xD8ACFF, 0xFFC1E8, 0xFFE6AE,
            0xBDF8DF
    };

    private HolofoilEffect() {
    }

    /** Solo Venusaur EX 003. Otras rarezas se elegirán más adelante. */
    public static boolean isHolofoil(int cardNumber) {
        return cardNumber == 3;
    }

    /**
     * @param time segundos acumulados de animación del visor
     * @param activity intensidad [0,1] de inclinación y arrastre
     * @param rotationX rotación vertical de la carta
     * @param rotationY rotación horizontal de la carta
     */
    public static void render(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            float time, float activity, float rotationX, float rotationY
    ) {
        if (width < 32 || height < 32) {
            return;
        }

        activity = clamp(activity, 0.0F, 1.0F);

        // En reposo la carta respira lentamente; al inclinarse se ilumina.
        float power = 0.17F + activity * 0.83F;

        // El brillo se mueve SOBRE la carta y no junto a la textura.
        // X/Y cambian la posición de la franja con gran sensibilidad,
        // mientras el movimiento de reposo es mucho más lento.
        float lightX = 0.50F
                + 0.39F * (float) Math.sin(time * 0.34F
                        + rotationY * 0.072F
                        - rotationX * 0.048F);

        // Máscara implícita: zona de la ilustración (evita tapar texto).
        int artLeft   = x + Math.round(width  * 0.075F);
        int artRight  = x + Math.round(width  * 0.925F);
        int artTop    = y + Math.round(height * 0.095F);
        int artBottom = y + Math.round(height * 0.555F);

        renderRainbowSheen(graphics, artLeft, artTop, artRight, artBottom,
                width, lightX, power);
        renderSparkles(graphics, artLeft, artTop, artRight, artBottom,
                time, activity, rotationX, rotationY);
    }

    /**
     * Banda espectral diagonal: muchas franjas finas, con alfa bajo,
     * concentradas en el reflejo. El resto de la carta queda intacto.
     */
    private static void renderRainbowSheen(
            GuiGraphics graphics,
            int left, int top, int right, int bottom,
            int cardWidth, float lightX, float power
    ) {
        int artWidth = right - left;
        int artHeight = bottom - top;
        if (artWidth <= 0 || artHeight <= 0) return;

        // Anchura global del reflejo, proporcional al tamaño del visor.
        float beamWidth = Math.max(12.0F, cardWidth * 0.205F);
        float segmentWidth = beamWidth * 2.0F / STRIPE_SEGMENTS;

        for (int yy = top; yy < bottom; yy += ROW_STEP) {
            int yEnd = Math.min(bottom, yy + ROW_STEP);
            float v = (yy - top + (yEnd - yy) * 0.5F) / artHeight;

            // Pendiente ligera, como el reflejo de una lámina física.
            float beamCenter = left + artWidth * lightX
                    + (0.5F - v) * cardWidth * 0.45F;

            // La intensidad varía suavemente con la altura.
            float verticalSoftness = 0.78F
                    + 0.22F * (float) Math.sin(v * Math.PI);

            for (int lane = 0; lane < STRIPE_SEGMENTS; lane++) {
                float t = (lane + 0.5F) / STRIPE_SEGMENTS; // 0..1
                float offset = (t - 0.5F) * beamWidth * 2.0F;
                int startX = Math.round(beamCenter + offset - segmentWidth * 0.5F);
                int endX = Math.round(beamCenter + offset + segmentWidth * 0.5F);
                startX = Math.max(left, startX);
                endX = Math.min(right, endX);
                if (startX >= endX) continue;

                float centerWeight = Math.max(0.0F, 1.0F - Math.abs(t - 0.5F) * 2.0F);
                centerWeight *= centerWeight; // difuminar bordes, no bloque sólido

                // Alfa máximo deliberadamente conservador: <= 35 / 255.
                int alpha = Math.round((3.0F + 32.0F * centerWeight)
                        * power * verticalSoftness);
                if (alpha <= 0) continue;

                int hueIndex = Math.min(SPECTRUM.length - 1,
                        (lane * SPECTRUM.length) / STRIPE_SEGMENTS);
                graphics.fill(startX, yy, endX, yEnd,
                        (alpha << 24) | SPECTRUM[hueIndex]);
            }

            // Una línea perla estrecha produce el destello que cruza
            // la imagen sin blanquear el Pokémon ni las letras.
            int whiteAlpha = Math.round((5.0F + 25.0F * power) * verticalSoftness);
            if (whiteAlpha > 0) {
                int coreX = Math.round(beamCenter);
                if (coreX >= left && coreX + 1 < right) {
                    graphics.fill(coreX, yy, coreX + 2, yEnd,
                            (whiteAlpha << 24) | 0xF8F9FF);
                }
            }
        }
    }

    /**
     * Destellos deterministas: no usamos random() en cada frame, así
     * que no saltan de posición. Un pulso seno modula su intensidad.
     */
    private static void renderSparkles(
            GuiGraphics graphics,
            int left, int top, int right, int bottom,
            float time, float activity, float rotationX, float rotationY
    ) {
        int width = right - left;
        int height = bottom - top;
        if (width < 10 || height < 10) return;

        for (int i = 0; i < SPARKLE_COUNT; i++) {
            // Hash ligero y estable que distribuye las estrellas.
            float u = hash01(i * 79 + 17);
            float v = hash01(i * 131 + 83);

            int sx = left + 4 + Math.round(u * (width - 9));
            int sy = top + 4 + Math.round(v * (height - 9));

            float phase = time * (0.8F + hash01(i + 4) * 1.15F)
                    + i * 2.39996F
                    + rotationY * 0.11F
                    - rotationX * 0.07F;

            float pulse = 0.5F + 0.5F * (float) Math.sin(phase);
            pulse = pulse * pulse * pulse;

            // Apenas visibles quieta; nítidos cuando se inclina.
            int alpha = Math.round((2.0F + 73.0F * pulse)
                    * (0.20F + activity * 0.80F));
            if (alpha < 6) continue;

            int color = (alpha << 24) | 0xFFFFFF;
            graphics.fill(sx, sy, sx + 1, sy + 1, color);

            if (alpha > 17) {
                int armAlpha = Math.min(72, Math.round(alpha * 0.43F));
                int armColor = (armAlpha << 24) | 0xE6F5FF;
                graphics.fill(sx - 2, sy, sx, sy + 1, armColor);
                graphics.fill(sx + 1, sy, sx + 3, sy + 1, armColor);
                graphics.fill(sx, sy - 2, sx + 1, sy, armColor);
                graphics.fill(sx, sy + 1, sx + 1, sy + 3, armColor);
            }
        }
    }

    private static float hash01(int x) {
        int n = x ^ (x >>> 16);
        n *= 0x7feb352d;
        n ^= n >>> 15;
        n *= 0x846ca68b;
        n ^= n >>> 16;
        return (n & 0x7fffffff) / (float) Integer.MAX_VALUE;
    }

    private static float clamp(float v, float lo, float hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
