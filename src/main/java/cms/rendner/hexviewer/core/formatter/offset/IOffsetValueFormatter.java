package cms.rendner.hexviewer.core.formatter.offset;

import cms.rendner.hexviewer.core.formatter.IValueFormatter;

/**
 * Formatter to format offset addresses.
 *
 * @author rendner
 */
public interface IOffsetValueFormatter extends IValueFormatter
{
    /**
     * Formats an offset address.
     *
     * @param numberOfChars the number of chars used in the result.
     *                      The converted value will be prefixed with <code>0</code> if the converted
     *                      <code>byteIndex</code> doesn't match the specified length.
     * @param offsetAddress the offset address to format.
     * @return the formatted value.
     */
    String format(int numberOfChars, int offsetAddress);

    /**
     * Computes for a given offset address the number of required chars to display only the digits of the
     * formatted offset value. This number should include separators between the digits but no additional prefix
     * and suffix.
     * <p/>
     * The number of chars to display an address depends on the internal used formatting.
     * For example the address 123456 (dec system):
     * <ul>
     * <li>formatted value in hex: "1E240", requires 5 chars</li>
     * <li>formatted value in hex: "1E240:", requires 5 chars - ":" is a suffix and doesn't count</li>
     * <li>formatted value in hex: "1E240h:", requires 5 chars - "h:" is a suffix and doesn't count</li>
     * <li>formatted value in dec: "123456", requires 6 chars</li>
     * <li>formatted value in dec: "123 456", requires 7 chars</li>
     * <li>formatted value in bin: "11110001001000000", requires 17 chars</li>
     * </ul>
     *
     * @param offsetAddress the offset address to be used to calculate the number of required chars to display
     *                      the address.
     * @return the number of chars to display the offset address without any prefix and suffix.
     */
    int computeNumberOfCharsForAddress(int offsetAddress);

    /**
     * @return the minimal number of chars to display an offset address.
     */
    int minNumberOfCharsForAddress();
}
