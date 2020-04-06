package cms.rendner.hexviewer.view.components.areas.common.painter;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * A painting delegate, allowing to exchange the logic how an area is painted.
 * <p/>
 * The area painter paints the background, middleground and foreground of an area in the mentioned order using separated
 * painters. An area painter is fully responsible to paint all content which should be displayed by an area.
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
     * Sets the foreground painter.
     * <p/>
     * A foreground painter usually uses the {@link cms.rendner.hexviewer.common.rowtemplate.IRowTemplate} provided by the
     * area to paint the content of the area. To allow customized colors, the painter should use the
     * {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider} of the area.
     * <p/>
     * This painter is mandatory to render the bytes inside the rows of the area.
     * Setting a new painter doesn't results in a repaint of the area, you have to call {@link JComponent#repaint() repaint()}
     * on the area.
     *
     * @param painter the new foreground painter, if <code>null</code> no foreground will be painted.
     */
    void setForegroundPainter(@Nullable final IAreaLayerPainter painter);

    /**
     * Sets the middleground painter.
     * <p/>
     * A middleground painter can be used to paint something in between the background and foreground. To allow customized
     * colors, the painter should use the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider}
     * of the area.
     * <p/>
     * This painter is mandatory for byte-areas to render the selection and highlights.
     * Setting a new painter doesn't results in a repaint of the area, you have to call {@link JComponent#repaint() repaint()}
     * on the area.
     *
     * @param painter the new middleground painter, if <code>null</code> no middleground will be painted.
     */
    void setMiddlegroundPainter(@Nullable final IAreaLayerPainter painter);

    /**
     * Sets the background painter.
     * <p/>
     * An background painter usually paints the background and/or the border of an area. To allow customized colors, the
     * painter should use the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider} of
     * the area.
     * <p/>
     * This painter isn't mandatory.
     * Setting a new painter doesn't results in a repaint of the area, you have to call {@link JComponent#repaint() repaint()}
     * on the area.
     *
     * @param painter the new background painter, if <code>null</code> no background will be painted.
     */
    void setBackgroundPainter(@Nullable final IAreaLayerPainter painter);
}
