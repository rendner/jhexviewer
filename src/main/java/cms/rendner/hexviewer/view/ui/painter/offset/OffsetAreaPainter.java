package cms.rendner.hexviewer.view.ui.painter.offset;

import cms.rendner.hexviewer.view.components.areas.common.painter.BasicAreaPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;

/**
 * Paints the offset-area to which the painter belongs.
 * <p/>
 * To allow customizing of the paint process this class uses an {@link IAreaForegroundPainter} and
 * {@link IAreaBackgroundPainter} which can be exchanged during runtime.
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
