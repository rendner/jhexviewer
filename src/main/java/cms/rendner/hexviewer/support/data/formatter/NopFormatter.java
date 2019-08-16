package cms.rendner.hexviewer.support.data.formatter;

import cms.rendner.hexviewer.core.formatter.IValueFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * Forwards a passed value unmodified.
 *
 * @author rendner
 */
public final class NopFormatter implements IValueFormatter
{
    @NotNull
    @Override
    public String format(final int value)
    {
        return "" + value;
    }
}
