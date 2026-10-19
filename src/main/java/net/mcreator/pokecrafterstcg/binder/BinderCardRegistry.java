package net.mcreator.pokecrafterstcg.binder;

import net.mcreator.pokecrafterstcg.PokecraftersTcgMod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Catálogo interno del Binder.
 *
 * Esta clase NO registra nuevos items.
 *
 * Localiza automáticamente las cartas ya existentes:
 *
 * xxx_001
 * xxx_002
 * ...
 * xxx_207
 */
public final class BinderCardRegistry {

    public static final int TOTAL_CARDS = 207;

    /**
     * 9 izquierda + 9 derecha.
     */
    public static final int CARDS_PER_PAGE = 18;

    public static final int TOTAL_PAGES =
            (TOTAL_CARDS + CARDS_PER_PAGE - 1)
                    / CARDS_PER_PAGE;

    private static final Pattern CARD_NUMBER_PATTERN =
            Pattern.compile(".*_(\\d{3})$");

    private static List<ItemStack> cachedCards;

    /**
     * Permite obtener rápidamente:
     *
     * Item -> número de carta
     */
    private static Map<Item, Integer> cachedNumbers;

    private BinderCardRegistry() {
    }

    public static List<ItemStack> getCards() {

        ensureCache();

        return cachedCards;
    }

    public static ItemStack getCard(int cardNumber) {

        if (cardNumber < 1
                || cardNumber > TOTAL_CARDS) {
            return ItemStack.EMPTY;
        }

        ensureCache();

        ItemStack template =
                cachedCards.get(cardNumber - 1);

        return template.isEmpty()
                ? ItemStack.EMPTY
                : template.copy();
    }

    /**
     * Devuelve el número de colección correspondiente
     * a un ItemStack.
     *
     * Si no es una carta devuelve -1.
     */
    public static int getCardNumber(ItemStack stack) {

        if (stack == null || stack.isEmpty()) {
            return -1;
        }

        ensureCache();

        return cachedNumbers.getOrDefault(
                stack.getItem(),
                -1
        );
    }

    public static boolean isCard(ItemStack stack) {
        return getCardNumber(stack) != -1;
    }

    /**
     * Comprueba que el item corresponde exactamente
     * con la posición indicada.
     */
    public static boolean matches(
            int cardNumber,
            ItemStack stack
    ) {

        if (stack == null || stack.isEmpty()) {
            return false;
        }

        return getCardNumber(stack)
                == cardNumber;
    }

    private static void ensureCache() {

        if (cachedCards == null
                || cachedNumbers == null) {
            buildCache();
        }
    }

    private static void buildCache() {

        Map<Integer, Item> cardsByNumber =
                new HashMap<>();

        Map<Item, Integer> numbersByItem =
                new IdentityHashMap<>();

        for (ResourceLocation id :
                BuiltInRegistries.ITEM.keySet()) {

            if (!PokecraftersTcgMod.MODID.equals(
                    id.getNamespace()
            )) {
                continue;
            }

            String path = id.getPath();

            /*
             * Evitamos booster_151 y similares.
             */
            if (path.startsWith("booster")) {
                continue;
            }

            Matcher matcher =
                    CARD_NUMBER_PATTERN.matcher(path);

            if (!matcher.matches()) {
                continue;
            }

            int number;

            try {

                number =
                        Integer.parseInt(
                                matcher.group(1)
                        );

            } catch (NumberFormatException ignored) {

                continue;
            }

            if (number < 1
                    || number > TOTAL_CARDS) {
                continue;
            }

            Item item =
                    BuiltInRegistries.ITEM.get(id);

            cardsByNumber.putIfAbsent(
                    number,
                    item
            );
        }

        List<ItemStack> cards =
                new ArrayList<>(TOTAL_CARDS);

        for (int number = 1;
             number <= TOTAL_CARDS;
             number++) {

            Item item =
                    cardsByNumber.get(number);

            if (item == null) {

                cards.add(ItemStack.EMPTY);

                PokecraftersTcgMod.LOGGER.warn(
                        "[Binder] No se encontró carta {}",
                        String.format("%03d", number)
                );

                continue;
            }

            cards.add(new ItemStack(item));

            numbersByItem.put(
                    item,
                    number
            );
        }

        cachedCards = List.copyOf(cards);
        cachedNumbers = numbersByItem;

        PokecraftersTcgMod.LOGGER.info(
                "[Binder] {} / {} cartas cargadas.",
                cardsByNumber.size(),
                TOTAL_CARDS
        );
    }
}