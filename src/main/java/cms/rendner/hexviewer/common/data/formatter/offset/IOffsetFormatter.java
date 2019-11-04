package cms.rendner.hexviewer.common.data.formatter.offset;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;

/**
 * Formats an offset value.
 *
 * @author rendner
 */
public interface IOffsetFormatter extends IValueFormatter
{
    /**
     * Adjusts the pad size used to format the value passed to {@link IValueFormatter#format(int)}.
     * <p/>
     * Using a pad size of 5 will result in the following behavior (value -> padded value):
     * <pre>
     *         1 -> 00001
     *        22 -> 00022
     *       333 -> 00333
     *      4444 -> 04444
     *     55555 -> 55555
     * </pre>
     * The final result of the formatted value depends on the implementation of the formatter.
     *
     * @param padSize the number of padding zeros to use when formatting a value.
     */
    void adjustPadSize(int padSize);

    /**
     * Calculates the length of the formatted value for a specified number of padding zeros.
     *
     * @param padSize the number of padding zeros to use when formatting the value.
     * @param value   the value to format.
     * @return the length of the formatted value.
     */
    int calculateFormattedValueLength(final int padSize, final int value);
}
