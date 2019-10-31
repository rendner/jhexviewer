package cms.rendner.hexviewer.common.data.formatter.base;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    String format(int value);
}
