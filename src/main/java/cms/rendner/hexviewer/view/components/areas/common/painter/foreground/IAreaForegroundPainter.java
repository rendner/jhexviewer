package cms.rendner.hexviewer.view.components.areas.common.painter.foreground;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A foreground painter is responsible for painting the foreground of an area.
 * <p/>
 * A foreground painter usually uses the {@link cms.rendner.hexviewer.common.rowtemplate.IRowTemplate} provided by the
 * area to paint the content of the area. To allow customized colors, the painter should use the
 * {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider} of the area.
 *
 * @author rendner
 */
public interface IAreaForegroundPainter
{
    /**
     * Paints the foreground of an area.
     *
     * @param g         the Graphics2D context of the area to be painted.
     * @param hexViewer the JHexViewer to which the area belongs.
     * @param component a reference to the area. This instance can be casted to the specific area implementation if needed.
     */
    void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component);
}
