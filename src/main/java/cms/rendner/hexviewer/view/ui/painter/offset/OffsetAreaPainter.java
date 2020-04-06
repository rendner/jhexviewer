package cms.rendner.hexviewer.view.ui.painter.offset;

import cms.rendner.hexviewer.view.components.areas.common.painter.BasicAreaPainter;

/**
 * Paints the offset-area to which the painter belongs.
 *
 * @author rendner
 */
public class OffsetAreaPainter extends BasicAreaPainter
{
    /**
     * Creates a new instance which paints an offset-area.
     */
    public OffsetAreaPainter()
    {
        setForegroundPainter(new OffsetRowForegroundPainter());
    }
}
