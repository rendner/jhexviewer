package cms.rendner.hexviewer.support.data.formatter;

import cms.rendner.hexviewer.core.formatter.LookupValueFormatter;
import cms.rendner.hexviewer.core.formatter.lookup.LookupTableFactory;

/**
 * Formats bytes to hex characters.
 *
 * @author rendner
 */
public final class HexByteFormatter extends LookupValueFormatter
{
    /**
     * Creates a new instance which uses a lookup table to format the bytes.
     */
    public HexByteFormatter()
    {
        super(LookupTableFactory.createHexTable());
    }
}
