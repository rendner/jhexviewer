package cms.rendner.hexviewer.core.formatter;

/**
 * A formatter formats a given value e.g a byte value into a string representation.
 *
 * @author rendner
 */
public interface IValueFormatter
{
    /**
     * Formats a value into a string representation.
     *
     * @param value the value to be converted.
     * @return the converted result.
     */
    String format(int value);
}
