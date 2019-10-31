package cms.rendner.hexviewer.common.data.formatter.bytes;

import cms.rendner.hexviewer.common.data.formatter.base.lookup.LookupTableFactory;
import cms.rendner.hexviewer.common.data.formatter.base.lookup.LookupValueFormatter;

/**
 * Formats bytes to ASCII strings.
 *
 * @author rendner
 */
public class AsciiByteFormatter extends LookupValueFormatter
{
    /**
     * Creates a new instance which uses an ascii lookup table to format the bytes.
     */
    public AsciiByteFormatter()
    {
        super(LookupTableFactory.createAsciiTable());
    }
}
