package cms.rendner.hexviewer.view.components.areas.common.painter;

import cms.rendner.hexviewer.common.rowtemplate.IRowTemplate;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
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
 * @param <A> the type of the area which is painted by this class.
 * @author rendner
 */
public abstract class BasicAreaPainter<A extends Area<
        ? extends IRowTemplate,
        ? extends IAreaColorProvider>> implements IAreaPainter
{
    /**
     * The area to be painted.
     */
    @NotNull
    protected final A area;

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

    /**
     * Creates a new instance.
     *
     * @param area the area painted by this instance.
     */
    protected BasicAreaPainter(@NotNull final A area)
    {
        this.area = area;
    }

    @Override
    public void setForegroundPainter(@Nullable final IAreaForegroundPainter foregroundPainter)
    {
        if (this.foregroundPainter != foregroundPainter)
        {
            this.foregroundPainter = foregroundPainter;
            area.repaint();
        }
    }

    @Override
    public void setBackgroundPainter(@Nullable final IAreaBackgroundPainter backgroundPainter)
    {
        if (this.backgroundPainter != backgroundPainter)
        {
            this.backgroundPainter = backgroundPainter;
            area.repaint();
        }
    }

    @Override
    public void paint(@NotNull final Graphics2D g)
    {
        paintBackground(g);
        paintForeground(g);
    }

    protected void paintBackground(@NotNull final Graphics2D g)
    {
        if (backgroundPainter != null)
        {
            backgroundPainter.paint(g);
        }
    }

    protected void paintForeground(@NotNull final Graphics2D g)
    {
        if (foregroundPainter != null)
        {
            foregroundPainter.paint(g);
        }
    }
}
