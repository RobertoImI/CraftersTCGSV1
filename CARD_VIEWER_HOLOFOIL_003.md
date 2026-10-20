# CraftersTCGSV1 — Visor HD y holofoil por código

**Rama:** `feature/card-viewer-holofoil-003`  
**Versión:** NeoForge Minecraft 1.21.1.

## Qué contiene esta rama

- `001_front.png` — Bulbasaur: visor HD sin holofoil.
- `002_front.png` — Ivysaur: visor HD sin holofoil.
- `003_front.png` — Venusaur EX: visor HD con holofoil sutil.
- `card_back.png` — reverso común, **sin** holofoil.

Las cartas se abren con clic derecho al sostener el ítem; clic izquierdo y arrastrar para inclinar; clic corto izquierdo para voltear; clic derecho o ESC para cerrar.

## Holofoil 003: 100 % Java, sin PNG extras

Archivo: `src/main/java/net/mcreator/pokecrafterstcg/cardviewer/client/HolofoilEffect.java`.

El código dibuja una franja estrecha de luz arcoíris con bordes suaves y pequeños destellos. **No se dibuja una lámina opaca sobre la carta**, ya que solo se usan rectángulos de alfa bajo, localizados principalmente dentro de la zona de ilustración. Los textos y la mayor parte de la tarjeta permanecen legibles.

El brillo se mueve lentamente en reposo y se hace más visible durante el arrastre, reaccionando a `rotationX`, `rotationY` y `holoDragEnergy` calculados por `CardViewerScreen.java`. El holofoil solo aparece en el frente de la 003, nunca en el reverso.

Se conservaron el movimiento, giro frente/reverso, visor HD, sombra y la lógica del Binder.

### Texturas antiguas eliminadas de esta rama

- `textures/card_viewer/foil/rainbow_foil.png`
- `textures/card_viewer/foil/rainbow_gradient.png`
- `textures/card_viewer/foil/glitter_mask.png`

No hace falta descargarlas ni restaurarlas. La carpeta vacía `foil/` no es necesaria y GitHub la elimina automáticamente si queda sin archivos.

### Prueba

1. Compila esta rama.
2. Abre 001 y 002 y verifica que siguen sin holografía.
3. Abre 003: se debe ver bien la ilustración, sin rectángulos grandes de color.
4. Deja la carta quieta: reflejo lento, casi imperceptible.
5. Mantén clic izquierdo y arrastra a ambos lados: destaca la franja arcoíris y aparecen algunos destellos.
6. Suelta: el efecto disminuye; voltea: el reverso no debe mostrarlo.
7. Comprueba que el Binder sigue sin cambios.

**Alcance de verificación:** commits y rutas revisados en GitHub. Hace falta compilación y prueba visual en Minecraft para dar el efecto por validado. No es una reproducción exacta de los shaders de Pokémon TCG Pocket.
