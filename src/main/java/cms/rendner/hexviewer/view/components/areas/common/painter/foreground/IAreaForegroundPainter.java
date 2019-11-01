package cms.rendner.hexviewer.view.components.areas.common.painter.foreground;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A foreground painter is responsible for painting the foreground of an area.
 *
 * @author rendner
 */
public interface IAreaForegroundPainter
{
    /**
     * Paints the foreground of an area.
     *
     * @param g the Graphics2D context of the area to paint the foreground of the area.
     */
    void paint(@NotNull final Graphics2D g);
}
