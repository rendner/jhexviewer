package cms.rendner.hexviewer.view.components.areas.common.painter;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Base class for area painters.
 * <p/>
 * An area painter is responsible for painting the background and foreground of an area. To allow customizing of the
 * paint process this class uses an {@link IAreaForegroundPainter} and {@link IAreaBackgroundPainter} instance which
 * can be exchanged during runtime.
 *
 * @author rendner
 */
public abstract class BasicAreaPainter implements IAreaPainter
{
    /**
     * Paints the foreground (text) of the rows displayed by the area.
     */
    @Nullable
    protected IAreaForegroundPainter foregroundPainter;

    /**
     * Paints the background of the area.
     */
    @Nullable
    protected IAreaBackgroundPainter backgroundPainter;

    @Override
    public void setForegroundPainter(@Nullable final IAreaForegroundPainter foregroundPainter)
    {
        if (this.foregroundPainter != foregroundPainter)
        {
            this.foregroundPainter = foregroundPainter;
        }
    }

    @Override
    public void setBackgroundPainter(@Nullable final IAreaBackgroundPainter backgroundPainter)
    {
        if (this.backgroundPainter != backgroundPainter)
        {
            this.backgroundPainter = backgroundPainter;
        }
    }

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        paintBackground(g, hexViewer, component);
        paintForeground(g, hexViewer, component);
    }

    protected void paintBackground(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        if (backgroundPainter != null)
        {
            backgroundPainter.paint(g, hexViewer, component);
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
