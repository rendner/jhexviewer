package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Paints the background of an area in one single step.
 *
 * @author rendner
 */
public interface IFullBackgroundPainter extends IAreaBackgroundPainter
{
    void paint(@NotNull Graphics2D g);
}
