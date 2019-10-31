package cms.rendner.hexviewer.common.rowtemplate;

import cms.rendner.hexviewer.common.geom.Dimension;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


/**
 * Describes the layout of a row.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed by the areas (offset, hex and ascii).
 * For each of the areas a separate template exists which describes the exact layout of the rows displayed by these areas.
 * <p/>
 * All subclasses should be immutable.
 *
 * @author rendner
 */
public abstract class BaseRowTemplate implements IRowTemplate
{
    /**
     * The dimension of the row.
     */
    @NotNull
    private final Dimension dimension;

    @NotNull
    private final FontMetrics fontMetrics;

    /**
     * Hide the constructor.
     *
     * @param dimension the dimension of the row.
     * @param fontMetrics    metrics of the font used to render the text displayed by the rows.
     */
    protected BaseRowTemplate(@NotNull final Dimension dimension, @NotNull final FontMetrics fontMetrics)
    {
        super();

        this.dimension = dimension;
        this.fontMetrics = fontMetrics;
    }

    @Override
    public int width()
    {
        return dimension.width();
    }

    @Override
    public int height()
    {
        return dimension.height();
    }

    @NotNull
    public Dimension dimension()
    {
        return dimension;
    }

    @NotNull
    @Override
    public FontMetrics fontMetrics()
    {
        return fontMetrics;
    }

    @Override
    public boolean contains(@NotNull final Point position)
    {
        return containsX(position.x) && containsY(position.y);
    }

    @Override
    public boolean containsX(final int xPosition)
    {
        return xPosition >= 0 && xPosition < width();
    }

    @Override
    public boolean containsY(final int yPosition)
    {
        return yPosition >= 0 && yPosition < height();
    }
}
