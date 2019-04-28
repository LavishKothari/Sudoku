package sudoku.utils;

import java.util.function.IntConsumer;

/**
 * This class assumes that you want to set the the bits
 * strictly greater than 0 and strictly less than 32
 */
public class SingleIntBitSet {
    private static final int INT_MAX_BITS = 32;
    private final int maxBits;
    private int value;

    public SingleIntBitSet() {
        this.maxBits = INT_MAX_BITS;
    }

    public SingleIntBitSet(int maxBits) {
        if (maxBits >= INT_MAX_BITS)
            throw new IllegalArgumentException("the bit that you " +
                    "want to set should be less than " + INT_MAX_BITS);
        this.maxBits = maxBits;
    }

    /**
     * The maxBits of all the sets should be same
     */
    public static void setBitsConsumer(IntConsumer consumer,
                                       SingleIntBitSet set1,
                                       SingleIntBitSet set2,
                                       SingleIntBitSet set3) {
        for (int i = 0; i < set1.maxBits; i++) {
            if (set1.isSet(i) || set2.isSet(i) || set3.isSet(i))
                consumer.accept(i);
        }
    }

    public void set(int i) {
        value = value | (1 << i);
    }

    public boolean isSet(int i) {
        return (value & (1 << i)) != 0;
    }

    public void clear(int i) {
        value = (value & (~(1 << i)));
    }

    public void setBitsConsumer(IntConsumer consumer) {
        for (int i = 1; i < maxBits; i++) {
            if (isSet(i)) consumer.accept(i);
        }
    }
}
