package cms.rendner.hexviewer.core.formatter;

import cms.rendner.hexviewer.core.formatter.lookup.ILookupTable;
import cms.rendner.hexviewer.utils.CheckUtils;

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
    private final ILookupTable table;

    /**
     * Creates a new instance.
     *
     * @param table the lookup table to be used (can't be null).
     */
    public LookupValueFormatter(final ILookupTable table)
    {
        super();

        CheckUtils.checkNotNull(table);
        this.table = table;
    }

    @Override
    public String format(final int value)
    {
        return table.mappedValue(value);
    }
}
