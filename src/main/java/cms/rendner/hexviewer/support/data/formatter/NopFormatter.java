package cms.rendner.hexviewer.support.data.formatter;

import cms.rendner.hexviewer.core.formatter.IValueFormatter;

/**
 * Forwards a passed value unmodified.
 *
 * @author rendner
 */
public class NopFormatter implements IValueFormatter
{
    @Override
    public String format(final int value)
    {
        return "" + value;
    }
}
