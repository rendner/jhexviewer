package cms.rendner.hexviewer.view.components.areas.common.painter;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Base class for area painters.
 * <p/>
 * An area painter is responsible for painting the background, middleground and foreground of an area. To allow customizing of the
 * paint process this class uses separated painters to paint the fore- and background which can be exchanged during
 * runtime.
 *
 * @author rendner
 */
public abstract class BasicAreaPainter implements IAreaPainter
{
    /**
     * Paints the foreground (text) of the rows displayed by the area.
     */
    @Nullable
    protected IAreaLayerPainter foregroundPainter;

    /**
     * Paints the middleground of the area.
     */
    @Nullable
    protected IAreaLayerPainter middlegroundPainter;

    /**
     * Paints the background of the area.
     */
    @Nullable
    protected IAreaLayerPainter backgroundPainter;

    @Override
    public void setForegroundPainter(@Nullable final IAreaLayerPainter painter)
    {
        foregroundPainter = painter;
    }

    @Override
    public void setMiddlegroundPainter(final @Nullable IAreaLayerPainter painter)
    {
        middlegroundPainter = painter;
    }

    @Override
    public void setBackgroundPainter(@Nullable final IAreaLayerPainter painter)
    {
        backgroundPainter = painter;
    }

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        paintBackground(g, hexViewer, component);
        paintMiddleground(g, hexViewer, component);
        paintForeground(g, hexViewer, component);
    }

    protected void paintBackground(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        if (backgroundPainter != null)
        {
            backgroundPainter.paint(g, hexViewer, component);
        }
    }

    protected void paintMiddleground(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        if (middlegroundPainter != null)
        {
            middlegroundPainter.paint(g, hexViewer, component);
        }
    }

    protected void paintForeground(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        if (foregroundPainter != null)
        {
            foregroundPainter.paint(g, hexViewer, component);
        }
    }
}
