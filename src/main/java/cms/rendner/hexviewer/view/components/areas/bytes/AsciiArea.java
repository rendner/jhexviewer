package cms.rendner.hexviewer.view.components.areas.bytes;

import cms.rendner.hexviewer.common.data.formatter.bytes.AsciiByteFormatter;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;

/**
 * Component which displays the content of the {@link cms.rendner.hexviewer.view.JHexViewer} in ascii.
 *
 * @author rendner
 */
public class AsciiArea extends ByteArea
{
    /**
     * Creates a new instance.
     */
    public AsciiArea()
    {
        super(AreaId.ASCII, new AsciiByteFormatter());
    }
}
