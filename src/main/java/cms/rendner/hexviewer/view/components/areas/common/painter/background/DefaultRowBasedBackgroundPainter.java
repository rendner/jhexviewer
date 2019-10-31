package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Default implementation of an {@link IRowBasedBackgroundPainter}.
 * <p/>
 * This class uses the background colors provided by the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider#getRowBackground(int)}
 * of the area to paint the background rowwise.
 *
 * @param <A> the type of the area which is painted by this class.
 */
public class DefaultRowBasedBackgroundPainter<A extends Area<?, ?, ?>> implements IRowBasedBackgroundPainter
{
    /**
     * Is re-used for fetching the bounds of the Graphics2D object.
     */
    @NotNull
    private final Rectangle clipBounds = new Rectangle();

    /**
     * The area to be painted by this instance.
     */
    @NotNull
    protected final A area;

    /**
     * A temporary property - the color provider of the area.
     */
    protected IAreaColorProvider colorProvider;

    /**
     * Creates a new instance responsible for painting the background for this area.
     * <p/>
     * Creating an instance of this class doesn't automatically registers this instance for painting the area background.
     *
     * @param area the area to be painted.
     */
    public DefaultRowBasedBackgroundPainter(@NotNull final A area)
    {
        super();
        this.area = area;
    }

    @Override
    public void prePaint()
    {
        colorProvider = area.getColorProvider().orElse(null);
    }

    @Override
    public boolean canPaint()
    {
        return colorProvider != null;
    }

    @Override
    public void paint(@NotNull Graphics2D g, int rowIndex, boolean isLastRow)
    {
        g.setColor(colorProvider.getRowBackground(rowIndex));
        g.getClipBounds(clipBounds);
        g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
    }
}
