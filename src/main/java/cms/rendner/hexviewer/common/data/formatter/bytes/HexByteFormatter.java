package cms.rendner.hexviewer.common.data.formatter.bytes;

import cms.rendner.hexviewer.common.data.formatter.base.lookup.LookupTableFactory;
import cms.rendner.hexviewer.common.data.formatter.base.lookup.LookupValueFormatter;

/**
 * Formats bytes to hex characters.
 *
 * @author rendner
 */
public class HexByteFormatter extends LookupValueFormatter
{
    /**
     * Creates a new instance which uses a lookup table to format the bytes.
     */
    public HexByteFormatter()
    {
        super(LookupTableFactory.createHexTable());
    }
}
