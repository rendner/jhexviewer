package cms.rendner.hexviewer.common.data.formatter.base.lookup;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Contains precalculated strings mapped by an index.
 *
 * @author rendner
 */
public final class LookupTable implements ILookupTable
{
    /**
     * Array of precalculated strings.
     */
    @NotNull
    private final String[] table;

    /**
     * Used to "calculate" the index in the char-table.
     */
    private final int bitMask;

    /**
     * Creates a new instance.
     *
     * @param table   array of precalculated strings.
     * @param bitMask used to map an value to an index in the table.
     *                Therefore the bitMask must fulfill <code>(table.length - 1) == (Integer.MAX_VALUE & bitMask)</code>
     */
    public LookupTable(@NotNull final String[] table, final int bitMask)
    {
        super();

        checkValidBitMask(table.length, bitMask);

        this.table = Arrays.copyOf(table, table.length);
        this.bitMask = bitMask;
    }

    @NotNull
    @Override
    public String mappedValue(final int value)
    {
        return table[(value & bitMask)];
    }

    @Override
    public int size()
    {
        return table.length;
    }

    /**
     * Checks if a bit mask maps valid to an index in a table.
     *
     * @param tableSize the size of the table.
     * @param bitMask   the bit mask for the table.
     * @throws IllegalArgumentException if the bit mask doesn't maps correctly (can produce an index which is out of bounds).
     */
    private void checkValidBitMask(final int tableSize, final int bitMask)
    {
        final int lastPossibleIndex = Integer.MAX_VALUE & bitMask;
        if (lastPossibleIndex >= tableSize)
        {
            throw new IllegalArgumentException("Invalid bitMask '" + bitMask +
                    "' (" + Integer.toBinaryString(bitMask) + ") for table specified, this can lead to an IndexOutOfBoundsException." +
                    "The table contains only '" + tableSize + "' entries." +
                    "But the max value produced by the bitMask is '" + lastPossibleIndex + "'.");
        }
    }
}
