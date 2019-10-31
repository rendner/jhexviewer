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
     * Adjusts the internal used pattern used to format the value passed to {@link IValueFormatter#format(int)}.
     *
     * @param leadingZeros the new number of leading zeros to use when formatting a value.
     */
    void adjustFormatString(int leadingZeros);
}
