package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A background painter is responsible for painting the background of an area.
 *
 * @author rendner
 */
public interface IAreaBackgroundPainter
{
    /**
     * Paints the background of an area.
     *
     * @param g the Graphics2D context of the area to paint the background of the area.
     */
    void paint(@NotNull final Graphics2D g);
}
