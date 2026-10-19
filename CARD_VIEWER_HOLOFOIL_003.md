# Card Viewer HD — Holofoil PNG para Venusaur EX 003

Parche de prueba para Minecraft NeoForge 1.21.1.

## Estado

**El parche está incorporado en esta rama.** Incluye código Java y los dos PNG transparentes necesarios.

| Carta | Vista frontal HD | Holofoil |
|---|---|---|
| 001 Bulbasaur | 001_front.png | No |
| 002 Ivysaur | 002_front.png | No |
| 003 Venusaur EX | 003_front.png | Sí, solo frente |

El reverso común se mantiene con \`card_back.png\`.

## Texturas de foil

Ruta: \`src/main/resources/assets/pokecrafters_tcg/textures/card_viewer/foil/\`

- \`rainbow_gradient.png\`: atlas 512×256 RGBA, degradado iridiscente con alpha **bajo e integrado en sus píxeles** (sin tapar la carta).
- \`glitter_mask.png\`: atlas 512×256 RGBA, destellos de fondo transparente basados en máximos de brillo de la imagen \`rainbow.jpg\` facilitada en la conversación.
- \`rainbow_foil.png\`: textura de la prueba anterior. **Ya NO se utiliza**; conservarla no cambia el resultado.

## Implementación

\`HolofoilEffect.java\` lee ambos PNG y los proyecta sobre el anverso con \`GuiGraphics.blit\`. Utiliza un recorte móvil de 256×256 dentro de cada atlas de 512×256, desplazado por tiempo y rotación de la carta.

En reposo: un pase suave del degradado y de los destellos.

Al girarla: un pase adicional de gradiente y hasta dos pases adicionales de destellos. La energía de arrastre baja suavemente al soltar.

Se elimina el uso de \`RenderSystem.setShaderColor\` como método para cambiar la opacidad, que había producido una capa arcoíris prácticamente sólida. **La transparencia está guardada en el canal alfa de cada PNG** y las capas se dibujan en el orden correcto.

El reverso no recibe efecto. No afecta al Binder, datos de servidor, inventarios ni modelos pequeños.

## Pruebas

1. Abre 001 y 002 y comprueba que siguen sin holofoil.
2. Abre Venusaur EX 003 con clic derecho: la imagen y el texto deberían permanecer legibles.
3. En reposo: brillo sutil.
4. Mantén clic izquierdo y arrastra: los destellos deben intensificarse sin crear una lámina arcoíris sólida.
5. Al soltar, vuelve la intensidad suave; al voltear al reverso, no se debe ver foil.
6. Confirma que el Binder no ha cambiado.

**Nota:** código y PNG están publicados, pero el efecto todavía requiere confirmación visual y compilación en el entorno NeoForge del proyecto antes de considerarlo validado.

Las licencias de cualquier recurso gráfico externo se deben comprobar antes de distribuir el mod públicamente.
