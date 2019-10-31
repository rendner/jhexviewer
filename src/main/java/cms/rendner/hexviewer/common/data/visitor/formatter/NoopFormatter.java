package cms.rendner.hexviewer.common.data.visitor.formatter;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * Forwards a passed value unmodified.
 *
 * @author rendner
 */
public final class NoopFormatter implements IValueFormatter
{
    @NotNull
    @Override
    public String format(final int value)
    {
        return "" + value;
    }
}
