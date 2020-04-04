package cms.rendner.hexviewer.view.ui.areas;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Forwards all paint calls to the {@link cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter areaPainter}
 * of an area.
 *
 * @author rendner
 */
public interface IPainterDelegate
{
    /**
     * Calls {@link cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter#paint(Graphics2D, JHexViewer, AreaComponent) paint(Graphics2D, JHexViewer, AreaComponent)}
     * on the area painter of the specified area component.
     *
     * @param g         the Graphics2D context of the area to be painted.
     * @param component a reference to the area. This instance can be casted to the specific area implementation if needed.
     */
    void paint(@NotNull final Graphics2D g, @NotNull final AreaComponent component);
}
