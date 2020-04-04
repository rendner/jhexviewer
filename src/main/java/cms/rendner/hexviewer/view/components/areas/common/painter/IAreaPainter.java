package cms.rendner.hexviewer.view.components.areas.common.painter;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * A painting delegate, allowing to exchange the logic how an area is painted.
 * <p>
 * An area painter is fully responsible to paint all content which should be displayed by an area.
 *
 * @author rendner
 */
public interface IAreaPainter
{
    /**
     * Called by the area to which this painter was installed.
     *
     * @param g         the Graphics2D context of the area to be painted.
     * @param hexViewer the JHexViewer to which the area belongs.
     * @param component a reference to the area. This instance can be casted to the specific area implementation if needed.
     */
    void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component);

    /**
     * Sets the foreground painter for the area.
     * <p/>
     * This painter is mandatory to render the bytes inside the rows of the area.
     * Setting a new painter doesn't results in a repaint of the area, you have to call {@link JComponent#repaint() repaint()}
     * on the area.
     *
     * @param foregroundPainter the new foreground painter, if <code>null</code> no foreground will be painted.
     */
    void setForegroundPainter(@Nullable final IAreaForegroundPainter foregroundPainter);

    /**
     * Sets the background painter for the area.
     * <p/>
     * This painter isn't mandatory.
     * Setting a new painter doesn't results in a repaint of the area, you have to call {@link JComponent#repaint() repaint()}
     * on the area.
     *
     * @param backgroundPainter the new background painter, if <code>null</code> no background will be painted.
     */
    void setBackgroundPainter(@Nullable final IAreaBackgroundPainter backgroundPainter);
}
