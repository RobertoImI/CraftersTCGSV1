package net.mcreator.pokecrafterstcg.cardviewer.client;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Holofoil de GUI: brillo irisado procedimental, sin texturas auxiliares.
 *
 * Solo el anverso de la carta 003 debe invocar render().
 * El anverso se dibuja primero y este efecto encima, bajo la
 * misma pose de rotación de la carta (X, Y y flip).
 *
 * No interviene en datos, servidor, inventario ni Binder.
 */
public final class HolofoilEffect {

    // Números habilitados para holofoil. Añadir otros aquí cuando se decida.
    public static boolean isHolofoil(int cardNumber) {
        return cardNumber == 3;
    }

    private static final int CELL_SIZE = 9;
    private static final float TWO_PI = (float) (Math.PI * 2.0);

    private HolofoilEffect() {
    }

    /**
     * @param time       segundos acumulados de animación
     * @param activity   0 al reposo, hasta 1 durante el arrastre/inclinación
     * @param rotationX  inclinación vertical actual
     * @param rotationY  inclinación horizontal actual
     */
    public static void render(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            float time, float activity, float rotationX, float rotationY
    ) {
        if (width <= 0 || height <= 0) {
            return;
        }

        activity = clamp(activity, 0.0F, 1.0F);

        // La referencia de la franja cambia con la inclinación, no solo el tiempo.
        float angleShift = rotationY * 0.012F + rotationX * 0.008F;

        // Deriva muy lentamente cuando la carta está quieta.
        float bandPosition = fract(time * 0.13F + 0.22F + angleShift);

        float ambient = 0.42F + 0.10F * (float) Math.sin(time * 1.9F);
        float strength = ambient + activity * 1.65F;

        // Quads pequeños dentro de los bordes de la carta.
        // Las transparencias suaves conservan legibles texto y dibujos.
        for (int cellY = 0; cellY < height; cellY += CELL_SIZE) {
            int bottom = Math.min(height, cellY + CELL_SIZE);
            float v = (cellY + (bottom - cellY) * 0.5F) / (float) height;

            for (int cellX = 0; cellX < width; cellX += CELL_SIZE) {
                int right = Math.min(width, cellX + CELL_SIZE);
                float u = (cellX + (right - cellX) * 0.5F) / (float) width;

                // Respetamos aproximadamente las esquinas redondeadas del PNG.
                if (!insideRoundedCard(u, v, width, height)) {
                    continue;
                }

                // Espectro de color que cambia en diagonal al mover la carta.
                float spectrum = u * 1.8F + v * 0.48F
                        - time * 0.055F + angleShift * 1.7F;
                float r = 0.5F + 0.5F * (float) Math.sin(TWO_PI * spectrum);
                float g = 0.5F + 0.5F * (float) Math.sin(TWO_PI * spectrum + 2.094F);
                float b = 0.5F + 0.5F * (float) Math.sin(TWO_PI * spectrum + 4.188F);

                // Halo iridiscente y barrido suave, más marcado al arrastrar.
                float diagonal = u * 0.82F + v * 0.36F;
                float distance = diagonal - (bandPosition * 1.45F - 0.13F);
                float shine = (float) Math.exp(-(distance * distance) / 0.018F);
                float sparkle = 0.5F + 0.5F * (float) Math.sin(
                        (u * 10.0F - v * 6.0F + time * 0.5F) * TWO_PI);

                // Rango aproximado: 5-14 alpha quieta, hasta 60 al mover.
                int alpha = clamp255(Math.round(
                        (5.0F + activity * 12.0F)
                                + (9.0F + activity * 37.0F) * shine
                                + (2.0F + activity * 3.0F) * sparkle
                ));

                int red = clamp255(Math.round(110.0F + 133.0F * r * strength));
                int green = clamp255(Math.round(113.0F + 130.0F * g * strength));
                int blue = clamp255(Math.round(125.0F + 125.0F * b * strength));

                // Luz blanca en la parte más intensa del destello.
                float white = shine * (0.18F + activity * 0.32F);
                red = clamp255(Math.round(red + (255 - red) * white));
                green = clamp255(Math.round(green + (255 - green) * white));
                blue = clamp255(Math.round(blue + (255 - blue) * white));

                graphics.fill(
                        x + cellX, y + cellY,
                        x + right, y + bottom,
                        (alpha << 24) | (red << 16) | (green << 8) | blue
                );
            }
        }
    }

    private static boolean insideRoundedCard(
            float u, float v, int width, int height
    ) {
        float radiusX = Math.min(0.060F, 13.0F / width);
        float radiusY = Math.min(0.045F, 13.0F / height);

        float centerX = u < 0.5F ? radiusX : 1.0F - radiusX;
        float centerY = v < 0.5F ? radiusY : 1.0F - radiusY;

        if (u >= radiusX && u <= 1.0F - radiusX) return true;
        if (v >= radiusY && v <= 1.0F - radiusY) return true;

        float dx = (u - centerX) / radiusX;
        float dy = (v - centerY) / radiusY;
        return dx * dx + dy * dy <= 1.0F;
    }

    private static float fract(float value) {
        return value - (float) Math.floor(value);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int clamp255(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
