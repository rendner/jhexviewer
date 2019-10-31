package cms.rendner.hexviewer.view.components.areas.bytes;

import cms.rendner.hexviewer.common.data.formatter.bytes.HexByteFormatter;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;

/**
 * Component which displays the content of the data model of the {@link cms.rendner.hexviewer.view.JHexViewer} as hex values.
 *
 * @author rendner
 */
public class HexArea extends ByteArea
{
    /**
     * Creates a new instance.
     */
    public HexArea()
    {
        super(AreaId.HEX, new HexByteFormatter());
    }
}
