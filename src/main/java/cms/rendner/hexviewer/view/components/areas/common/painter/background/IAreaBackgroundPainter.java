package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A background painter is responsible for painting the background of an area.
 * <p/>
 * An background painter usually paints the background and/or the border of an area. To allow customized colors, the
 * painter should use the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider} of
 * the area.
 *
 * @author rendner
 */
public interface IAreaBackgroundPainter
{
    /**
     * Paints the background of an area.
     *
     * @param g         the Graphics2D context of the area to be painted.
     * @param hexViewer the JHexViewer to which the area belongs.
     * @param component a reference to the area. This instance can be casted to the specific area implementation if needed.
     */
    void paint(@NotNull Graphics2D g, @NotNull JHexViewer hexViewer, @NotNull AreaComponent component);
}
