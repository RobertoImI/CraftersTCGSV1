package net.mcreator.pokecrafterstcg.binder;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Arrays;

/**
 * Datos persistentes de un Binder.
 *
 * Cada índice representa una carta:
 *
 * índice 0   = carta 001
 * índice 1   = carta 002
 * ...
 * índice 206 = carta 207
 */
public class BinderData
        implements INBTSerializable<CompoundTag> {

    /**
     * Límite de seguridad.
     *
     * No esperamos que nadie tenga un millón
     * de copias de una carta, pero evitamos
     * cualquier overflow por paquetes maliciosos.
     */
    public static final int MAX_COPIES_PER_CARD =
            1_000_000;

    private final int[] counts =
            new int[BinderCardRegistry.TOTAL_CARDS];

    public int getCount(int cardNumber) {

        if (!isValid(cardNumber)) {
            return 0;
        }

        return counts[cardNumber - 1];
    }

    /**
     * Agrega cartas.
     *
     * Devuelve la cantidad realmente agregada.
     */
    public int add(
            int cardNumber,
            int amount
    ) {

        if (!isValid(cardNumber)
                || amount <= 0) {
            return 0;
        }

        int index = cardNumber - 1;

        int oldCount = counts[index];

        long requested =
                (long) oldCount + amount;

        int newCount =
                (int) Math.min(
                        MAX_COPIES_PER_CARD,
                        requested
                );

        counts[index] = newCount;

        return newCount - oldCount;
    }

    /**
     * Elimina cartas.
     *
     * Devuelve cuántas realmente fueron retiradas.
     */
    public int remove(
            int cardNumber,
            int amount
    ) {

        if (!isValid(cardNumber)
                || amount <= 0) {
            return 0;
        }

        int index = cardNumber - 1;

        int removed =
                Math.min(
                        amount,
                        counts[index]
                );

        counts[index] -= removed;

        return removed;
    }

    public void setCount(
            int cardNumber,
            int amount
    ) {

        if (!isValid(cardNumber)) {
            return;
        }

        counts[cardNumber - 1] =
                Math.max(
                        0,
                        Math.min(
                                MAX_COPIES_PER_CARD,
                                amount
                        )
                );
    }

    public void copyFrom(BinderData other) {

        if (other == null) {
            return;
        }

        System.arraycopy(
                other.counts,
                0,
                this.counts,
                0,
                this.counts.length
        );
    }

    private boolean isValid(int cardNumber) {

        return cardNumber >= 1
                && cardNumber
                <= BinderCardRegistry.TOTAL_CARDS;
    }

    @Override
    public CompoundTag serializeNBT(
            HolderLookup.Provider lookupProvider
    ) {

        CompoundTag tag =
                new CompoundTag();

        tag.putIntArray(
                "cards",
                counts
        );

        return tag;
    }

    @Override
    public void deserializeNBT(
            HolderLookup.Provider lookupProvider,
            CompoundTag tag
    ) {

        Arrays.fill(counts, 0);

        if (tag == null
                || !tag.contains("cards")) {
            return;
        }

        int[] saved =
                tag.getIntArray("cards");

        int length =
                Math.min(
                        saved.length,
                        counts.length
                );

        for (int i = 0;
             i < length;
             i++) {

            counts[i] =
                    Math.max(
                            0,
                            Math.min(
                                    MAX_COPIES_PER_CARD,
                                    saved[i]
                            )
                    );
        }
    }
}