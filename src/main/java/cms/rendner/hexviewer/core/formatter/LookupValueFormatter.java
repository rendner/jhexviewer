package cms.rendner.hexviewer.core.formatter;

import cms.rendner.hexviewer.core.formatter.lookup.ILookupTable;
import org.jetbrains.annotations.NotNull;

/**
 * Formatter which uses a lookup table to convert values.
 *
 * @author rendner
 */
public class LookupValueFormatter implements IValueFormatter
{
    /**
     * The table used to convert values.
     */
    @NotNull
    private final ILookupTable table;

    /**
     * Creates a new instance.
     *
     * @param table the lookup table to be used.
     */
    public LookupValueFormatter(@NotNull final ILookupTable table)
    {
        super();
        this.table = table;
    }

    @NotNull
    @Override
    public String format(final int value)
    {
        return table.mappedValue(value);
    }
}
