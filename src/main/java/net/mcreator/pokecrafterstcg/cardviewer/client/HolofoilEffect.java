package net.mcreator.pokecrafterstcg.cardviewer.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Holofoil basado en la textura PNG rainbow_foil.png.
 *
 * El PNG se generó usando la máscara de destellos rainbow.jpg
 * proporcionada para esta prueba, combinada con un degradado
 * iridiscente y transparencia; no es un shader realista aún.
 *
 * El efecto es independiente de los items, del Binder y del servidor.
 */
public final class HolofoilEffect {
    private static final ResourceLocation FOIL =
            ResourceLocation.fromNamespaceAndPath(
                    PokecraftersTcgMod.MODID,
                    "textures/card_viewer/foil/rainbow_foil.png"
            );

    // La textura ocupa 1024x512, con región móvil de 512x512.
    private static final int SOURCE_WIDTH = 1024;
    private static final int SOURCE_HEIGHT = 512;
    private static final int SAMPLE_SIZE = 512;

    private HolofoilEffect() {
    }

    public static boolean isHolofoil(int cardNumber) {
        return cardNumber == 3;
    }

    public static void render(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            float time, float activity, float rotationX, float rotationY
    ) {
        if (width <= 0 || height <= 0) {
            return;
        }

        // Incluso sin mover la carta, la textura deriva muy lentamente.
        // Al arrastrar, se desplaza mucho más según la inclinación.
        float position = time * (0.7F + activity * 1.8F)
                + rotationY * 5.3F + rotationX * 2.9F;

        int u = Math.floorMod((int) Math.floor(position * 10.0F),
                SOURCE_WIDTH - SAMPLE_SIZE + 1);

        // Baja intensidad en reposo; mayor presencia al arrastrar.
        float alpha = clamp(0.18F + activity * 0.72F, 0.0F, 0.90F);

        // La propia imagen lleva alpha por píxel: sus zonas negras se
        // convirtieron en transparencia, no tapan la carta.
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        try {
            graphics.blit(
                    FOIL,
                    x, y, width, height,
                    (float) u, 0.0F,
                    SAMPLE_SIZE, SAMPLE_SIZE,
                    SOURCE_WIDTH, SOURCE_HEIGHT
            );
            graphics.flush();
        } finally {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
