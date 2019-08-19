package cms.rendner.hexviewer.core.formatter.offset;

import cms.rendner.hexviewer.core.formatter.StringValueFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * Formats offset addresses into a hex representation.
 * <p/>
 * An address like "123456" becomes "1E240h".
 *
 * @author rendner
 */
public final class OffsetFormatter extends StringValueFormatter implements IOffsetValueFormatter
{
    /**
     * The minimum number of chars to represent a address.
     */
    private final int minNumberOfChars;

    /**
     * The format identifier.
     * X means the result is formatted as a hexadecimal integer.
     *
     * @see String#format(String, Object...)
     */
    @NotNull
    private final String valueFormat = "X";

    /**
     * Creates a new instance.
     *
     * @param minNumberOfChars the minimum number of chars to represent a address.
     */
    public OffsetFormatter(final int minNumberOfChars)
    {
        super();

        this.minNumberOfChars = minNumberOfChars;
    }

    @NotNull
    @Override
    public String format(final int numberOfChars, final int offsetAddress)
    {
        final String padDigit = "0";
        final String valueSuffix = "h";
        return format("%" + padDigit + numberOfChars + valueFormat + valueSuffix, offsetAddress);
    }

    @NotNull
    @Override
    public String format(final int offsetAddress)
    {
        return format(minNumberOfChars, offsetAddress);
    }

    @Override
    public int computeNumberOfCharsForAddress(final int offsetAddress)
    {
        return format("%" + valueFormat, offsetAddress).length();
    }

    @Override
    public int minNumberOfCharsForAddress()
    {
        return minNumberOfChars;
    }
}
