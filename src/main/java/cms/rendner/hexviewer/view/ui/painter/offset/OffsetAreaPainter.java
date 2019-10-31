package cms.rendner.hexviewer.view.ui.painter.offset;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.ui.painter.BasicAreaPainter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This painter paints the background and foreground of the area to which the painter belongs.
 * <p/>
 * To allow customizing of the paint process this class uses an {@link IAreaForegroundPainter} and
 * {@link IAreaBackgroundPainter} which can be exchanged during runtime.
 *
 * @author rendner
 */
public class OffsetAreaPainter extends BasicAreaPainter<OffsetArea>
{
    /**
     * Creates a new instance which paints the offset-area.
     *
     * @param hexViewer the {@link JHexViewer} to which the area belongs. Required to query additional properties of the {@link JHexViewer}.
     * @param area      the area to be painted by this instance.
     *                  This area should be the same which calls {@link #paint(Graphics2D)}
     *                  on this painter.
     */
    public OffsetAreaPainter(@NotNull final JHexViewer hexViewer, @NotNull final OffsetArea area)
    {
        super(area);
        setForegroundPainter(new OffsetRowForegroundPainter(hexViewer));
    }
}
