package net.mcreator.pokecrafterstcg.cardviewer.client;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Holofoil PNG para el visor HD, sin modificar el Binder ni los ítems.
 *
 * Dos capas reutilizables:
 *  - rainbow_gradient.png: iridiscencia MUY tenue.
 *  - glitter_mask.png: puntos de luz con alfa real.
 *
 * IMPORTANTE: no usamos RenderSystem.setShaderColor para cambiar alpha.
 * En 1.21.1, el blit del GuiGraphics usa sus propios buffers: esa
 * modulación podía no afectarlos y dibujaba el arcoíris como una sábana.
 * Ambas texturas llevan ya una transparencia RGBA conservadora.
 */
public final class HolofoilEffect {

    private static final ResourceLocation RAINBOW =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/foil/rainbow_gradient.png"
            );

    private static final ResourceLocation GLITTER =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/foil/glitter_mask.png"
            );

    // Texturas atlases desplazables de 512×256.
    // En cada frame tomamos un recorte de 256×256 que se mueve
    // dentro de los 512 px de ancho, sin salirse del PNG.
    private static final int ATLAS_W = 512;
    private static final int ATLAS_H = 256;
    private static final int SAMPLE = 256;

    private HolofoilEffect() {
    }

    public static boolean isHolofoil(int cardNumber) {
        // Solo el anverso de Venusaur EX 003.
        return cardNumber == 3;
    }

    public static void render(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            float time, float activity, float rotationX, float rotationY
    ) {
        if (width <= 0 || height <= 0) return;

        activity = clamp(activity, 0.0F, 1.0F);

        // Los recortes responden a inclinación además del tiempo.
        // En reposo casi no se desplazan; al arrastrar se nota más.
        float rainbowPhase = time * 0.29F
                + rotationY * 0.070F + rotationX * 0.045F;
        float glitterPhase = time * 0.43F
                - rotationY * 0.098F + rotationX * 0.060F;

        int rainbowU = 128 + Math.round(
                (float) Math.sin(rainbowPhase) * 120.0F);
        int glitterU = 128 + Math.round(
                (float) Math.sin(glitterPhase) * 120.0F);

        // Mantener la textura COMPLETA por debajo de la carta:
        // cada PNG tiene alpha real (la mayor parte es transparente).
        // Más pases con movimiento realzan el efecto gradualmente
        // sin agregar un rectángulo opaco.
        renderLayer(graphics, RAINBOW, x, y, width, height, rainbowU);

        if (activity > 0.30F) {
            renderLayer(graphics, RAINBOW, x, y, width, height, rainbowU);
        }

        // Destellos RGB blancos con fondo 100% transparente.
        renderLayer(graphics, GLITTER, x, y, width, height, glitterU);

        if (activity > 0.20F) {
            renderLayer(graphics, GLITTER, x, y, width, height, glitterU);
        }
        if (activity > 0.65F) {
            renderLayer(graphics, GLITTER, x, y, width, height, glitterU);
        }
    }

    private static void renderLayer(
            GuiGraphics graphics,
            ResourceLocation texture,
            int x, int y, int width, int height,
            int offsetU
    ) {
        int u = Math.max(0, Math.min(ATLAS_W - SAMPLE, offsetU));

        // Se usa la sobrecarga con destino y origen independientes.
        // Esta llamada toma la región completa 256×256 y la escala
        // a la carta sin recortar o agrandar por error.
        graphics.blit(
                texture, x, y, width, height,
                (float) u, 0.0F,
                SAMPLE, SAMPLE,
                ATLAS_W, ATLAS_H
        );
        // Respetar el orden de los buffers respecto a la carta.
        graphics.flush();
    }

    private static float clamp(float v, float low, float high) {
        return Math.max(low, Math.min(high, v));
    }
}
