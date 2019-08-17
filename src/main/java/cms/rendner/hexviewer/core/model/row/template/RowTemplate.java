package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.geom.Dimension;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


/**
 * Describes the layout of a row.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed in the JHexViewer.
 * For each of the areas ({@link cms.rendner.hexviewer.core.view.areas.AreaId#OFFSET},
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} and
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII}) of the JHexViewer a separate template exists
 * which describes the exact layout of the rows rendered by these areas.
 * <p/>
 * All subclasses should stay immutable.
 *
 * @author rendner
 */
public abstract class RowTemplate implements IRowTemplate
{
    /**
     * The dimension of the row.
     */
    @NotNull
    private final Dimension dimension;

    /**
     * The font used to render the text of the rows.
     */
    @NotNull
    private final Font font;

    /**
     * The ascent to center an element vertically if painted into a {@link Graphics} object.
     */
    private final int ascent;

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param font      the font used to render the text of the rows.
     * @param dimension the dimension of the row.
     * @param ascent    the ascent to center an element vertically if painted into a {@link Graphics} object.
     */
    RowTemplate(@NotNull final Dimension dimension, @NotNull final Font font, final int ascent)
    {
        super();

        this.font = font;
        this.ascent = ascent;
        this.dimension = dimension;
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

    @Override
    public int ascent()
    {
        return ascent;
    }

    @NotNull
    @Override
    public Font font()
    {
        return font;
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
