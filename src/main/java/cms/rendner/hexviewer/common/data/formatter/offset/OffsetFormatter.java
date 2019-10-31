package cms.rendner.hexviewer.common.data.formatter.offset;

import org.jetbrains.annotations.NotNull;

/**
 * Formats offset addresses into a hex representation.
 * <p/>
 * An address like "123456" becomes "1E240h".
 *
 * @author rendner
 */
public class OffsetFormatter implements IOffsetFormatter
{
    /**
     * The format used to format the values.
     */
    private String format;

    /**
     * Indicates if the offset should be uppercased or not.
     */
    private final boolean uppercasedOffset;

    /**
     * The suffix to add to the formatted value.
     */
    private final String suffix;

    /**
     * Creates a new instance with the provided values.
     *
     * @param uppercasedOffset indicates if the offset should be uppercased or not.
     * @param suffix           the suffix to add to the formatted value.
     */
    public OffsetFormatter(final boolean uppercasedOffset, @NotNull final String suffix)
    {
        this.uppercasedOffset = uppercasedOffset;
        this.suffix = suffix;
        adjustFormatString(0);
    }

    @Override
    public void adjustFormatString(final int leadingZeros)
    {
        if (leadingZeros > 0)
        {
            this.format = "%0" + leadingZeros + (uppercasedOffset ? "X" : "x") + suffix;
        }
        else
        {
            this.format = "%" + (uppercasedOffset ? "X" : "x") + suffix;
        }
    }

    @Override
    public @NotNull String format(final int value)
    {
        return String.format(format, value);
    }
}
