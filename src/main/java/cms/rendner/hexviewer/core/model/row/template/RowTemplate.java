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
 * All subclass should stay immutable.
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
     * @param source the builder used to initialize the new instance.
     */
    RowTemplate(@NotNull final Builder source)
    {
        super();

        this.font = source.font;
        this.ascent = source.ascent;
        this.dimension = source.dimension;
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

    /**
     * A builder can be used to set the desired values before creating a immutable row template instance.
     *
     * @param <B> the concrete class of the builder.
     */
    public static abstract class Builder<B extends Builder>
    {
        /**
         * The dimension of the row.
         */
        protected Dimension dimension;

        /**
         * The font used to render the text of the rows.
         */
        protected Font font;

        /**
         * The ascent to center an element vertically if painted into a {@link Graphics} object.
         */
        protected int ascent;

        /**
         * Hide the constructor.
         * Creates a new builder.
         */
        protected Builder()
        {
            super();
        }

        abstract protected B getThis();

        /**
         * Sets the font for the template (mandatory).
         *
         * @param font the font used to render the text of the row.
         * @return the builder instance, to allow method chaining.
         */
        public B setFont(@NotNull final Font font)
        {
            this.font = font;
            return getThis();
        }

        /**
         * Sets the ascent, used to vertically align the characters rendered at the position of an element.
         *
         * @param ascent the ascent.
         * @return the builder instance, to allow method chaining.
         */
        public B setAscent(final int ascent)
        {
            this.ascent = ascent;
            return getThis();
        }

        /**
         * Sets the dimension of the row.
         *
         * @param dimension the dimension for the row.
         * @return the builder instance, to allow method chaining.
         */
        public B setDimension(@NotNull final Dimension dimension)
        {
            this.dimension = dimension;
            return getThis();
        }

        /**
         * Sets the dimension of the row.
         *
         * @param width  the width for the row.
         * @param height the height for the row.
         * @return the builder instance, to allow method chaining.
         */
        public B setDimension(final int width, final int height)
        {
            return setDimension(new Dimension(width, height));
        }
    }
}
