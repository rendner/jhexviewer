package cms.rendner.hexviewer.core.formatter.offset;

import cms.rendner.hexviewer.core.formatter.StringValueFormatter;

/**
 * Formats offset addresses into a hex representation.
 * <p/>
 * An address like "123456" becomes "1E240h".
 *
 * @author rendner
 */
public class OffsetFormatter extends StringValueFormatter implements IOffsetValueFormatter
{
    /**
     * The minimum number of chars to represent a address.
     */
    private final int minNumberOfChars;

    /**
     * The digit used for padding.
     */
    protected String padDigit = "0";

    /**
     * The format identifier.
     * X means the result is formatted as a hexadecimal integer.
     *
     * @see String#format(String, Object...)
     */
    protected String valueFormat = "X";

    /**
     * The suffix which gets added to each formatted address.
     */
    protected String valueSuffix = "h";

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

    @Override
    public String format(final int numberOfChars, final int offsetAddress)
    {
        return format("%" + padDigit + numberOfChars + valueFormat + valueSuffix, offsetAddress);
    }

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
