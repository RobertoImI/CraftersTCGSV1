# Visor de cartas HD + holofoil 003

Versión de prueba del visor para Minecraft NeoForge 1.21.1.

## Cartas habilitadas

- **001 Bulbasaur** — imagen HD, sin holofoil.
- **002 Ivysaur** — imagen HD, sin holofoil.
- **003 Venusaur ex** — imagen HD con holofoil procedimental **solo en el frente**.
- Todos usan el mismo `card_back.png` del proyecto.

## Recursos HD requeridos

En `src/main/resources/assets/pokecrafters_tcg/textures/card_viewer/fronts/`:

- `001_front.png` (ya estaba en master)
- **`002_front.png`** (archivo proporcionado en el chat, 330×460)
- **`003_front.png`** (archivo proporcionado en el chat, 330×460)

**Importante:** los dos PNG nuevos deben añadirse como archivos binarios. El código Java está preparado, pero el archivo PNG no se encuentra en el repositorio hasta que se suba. No reemplazarlo por una imagen vacía ni cambiar su proporción.

## Uso

Sostener el ítem `bulbasaur_001`, `ivysaur_002` o `venusaur_003` y hacer clic derecho. Mantener clic izquierdo y arrastrar para inclinar; clic izquierdo sin arrastrar para voltear; clic derecho o ESC para cerrar.

## Configuración del holofoil

La lista de cartas holofoil está centralizada en `HolofoilEffect.isHolofoil(int)`, inicialmente solo la **003**. En reposo el brillo se anima suavemente; durante el arrastre la intensidad crece con la velocidad/inclinación y se desvanece al soltar.

El brillo no se aplica al reverso, ni modifica el render pequeño de ítems, el Binder o los datos del servidor. No se añaden shaders ni texturas auxiliares de holofoil: se dibuja procedimentalmente en la matriz del visor.

## Pruebas esperadas

1. Abrir 001, 002 y 003 con clic derecho.
2. Confirmar las imágenes completas en HD (330×460 de origen).
3. Comprobar que solo 003 tiene destellos suaves quieta y más notorios arrastrándola.
4. Voltear 003: el reverso no debe mostrar holofoil.
5. Soltar la carta: inclinación y reflejo disminuyen suavemente.
6. Confirmar que la interfaz Binder permanece igual.

## Prueba PNG rainbow foil (19 oct)

La clase `HolofoilEffect.java` ahora usa una textura externa: `textures/card_viewer/foil/rainbow_foil.png`.

El archivo recibido como `rainbow.jpg` es una textura de destellos blanco/negro (no un degradado arcoíris). Por eso se generó `rainbow_foil.png` (1024×512, RGBA) combinando los destellos como canal alfa con color iridiscente. El visor desplaza una región de la textura y modula su intensidad con la actividad/rotación. Solo la carta 003 y solo por delante.

**PASO PENDIENTE PARA EJECUTAR:** subir el PNG generado a
`src/main/resources/assets/pokecrafters_tcg/textures/card_viewer/foil/rainbow_foil.png` en esta rama. El archivo binario no se pudo escribir por la herramienta GitHub utilizada; sin él habrá textura faltante.

La imagen de origen tiene uso/licencia por verificar antes de redistribuirla públicamente.
