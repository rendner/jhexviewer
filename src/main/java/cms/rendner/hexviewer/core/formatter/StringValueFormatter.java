package cms.rendner.hexviewer.core.formatter;

import org.jetbrains.annotations.NotNull;

/**
 * Formatter which uses a format-string to format a value.
 * <p/>
 * The format-string is passed to <code>String.format</code> to format a value.
 *
 * @author rendner
 * @see String#format(String, Object...)
 */
public class StringValueFormatter implements IValueFormatter
{
    /**
     * The format string used to format a value.
     */
    @NotNull
    private final String format;

    /**
     * Creates a new instance with an empty string as format-string (no formatting will be applied).
     */
    public StringValueFormatter()
    {
        this("");
    }

    /**
     * Creates a new instance with a specified format-string.
     *
     * @param format the format-string to be used to format the values.
     */
    public StringValueFormatter(@NotNull final String format)
    {
        super();
        this.format = format;
    }

    @NotNull
    @Override
    public String format(final int value)
    {
        return format(format, value);
    }

    /**
     * Formats a value.
     *
     * @param format format-string used to format a value.
     * @param value  the value to be formatted.
     * @return the formatted value.
     */
    @NotNull
    protected String format(@NotNull final String format, final int value)
    {
        return String.format(format, value);
    }
}
