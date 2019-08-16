package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.ElementHitInfo;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * The elements of the row.
     * The number of minimum entries of this property is <code>1</code>.
     */
    @NotNull
    protected final List<IElement> elements;

    /**
     * The dimension of the row.
     */
    @NotNull
    private final IRowTemplate.IDimension dimension;

    /**
     * The font used to render the text of the rows.
     */
    @NotNull
    private final Font font;

    /**
     * The ascent to center an element vertically if painted into a {@link Graphics} object.
     */
    private int ascent;

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
        this.elements = source.elements;
    }

    @NotNull
    @Override
    public ElementHitInfo hitTest(final int xPosition)
    {
        return hitTest(xPosition, new ElementHitInfo());
    }

    @NotNull
    @Override
    public ElementHitInfo hitTest(final int xPosition, @NotNull final ElementHitInfo returnValue)
    {
        final int elementIndex = elementIndexForXPosition(xPosition);
        final IElement element = elements.get(elementIndex);

        final int halfWidth = element.width() / 2;
        final boolean isLeadingEdge = xPosition < (element.right() - halfWidth);
        final boolean wasInside = element.containsX(xPosition);

        returnValue.fillWith(elementIndex, isLeadingEdge, wasInside);
        return returnValue;
    }

    @Override
    public int width()
    {
        return dimension.width();
    }

    @Override
    public int leftInset()
    {
        return elements.get(0).x();
    }

    @Override
    public int rightInset()
    {
        return width() - elements.get(elements.size() - 1).right();
    }

    @Override
    public int height()
    {
        return dimension.height();
    }

    @Override
    public int elementCount()
    {
        return elements.size();
    }

    @NotNull
    @Override
    public IElement element(final int index)
    {
        return elements.get(index);
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

    @NotNull
    @Override
    public Rectangle elementBounds(final int firstElementIndex, final int lastElementIndex)
    {
        final IElement firstElement = elements.get(firstElementIndex);
        final IElement lastElement = elements.get(lastElementIndex);
        return new Rectangle(
                firstElement.x(),
                firstElement.y(),
                lastElement.right() - firstElement.x(),
                lastElement.height());
    }

    /**
     * Checks which element is under the position.
     *
     * @param xPosition the x position which should be checked.
     * @return the index of the element which intersects with the position.
     */
    protected int elementIndexForXPosition(final int xPosition)
    {
        final int lastElementIndex = elements.size() - 1;

        for (int i = 0; i < lastElementIndex; i++)
        {
            final IElement nextElement = elements.get(i + 1);

            final boolean positionIsBeforeNextElement = xPosition < nextElement.x();

            if (positionIsBeforeNextElement)
            {
                return i;
            }
        }

        return lastElementIndex;
    }

    /**
     * A builder can be used to set the desired values before creating a immutable row template instance.
     *
     * @param <B> the concrete class of the builder.
     */
    public static abstract class Builder<B extends Builder>
    {
        /**
         * The elements of the row.
         * The number of minimum entries of this property is <code>1</code>.
         */
        protected List<IElement> elements;

        /**
         * The dimension of the row.
         */
        protected IRowTemplate.IDimension dimension;

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
        public B setDimension(@NotNull final IRowTemplate.IDimension dimension)
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
            return setDimension(new RowDimension(width, height));
        }

        /**
         * Sets the horizontal aligned elements for the row.
         *
         * @param elements the elements of the row, not empty - the list has to contain at least one element.
         * @return the builder instance, to allow method chaining.
         */
        public B setElements(@NotNull final List<IElement> elements)
        {
            CheckUtils.checkMinValue(elements.size(), 1);
            this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
            return getThis();
        }
    }
}
