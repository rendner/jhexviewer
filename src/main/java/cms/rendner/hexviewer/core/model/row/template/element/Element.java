package cms.rendner.hexviewer.core.model.row.template.element;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

/**
 * An element is a precalculated area inside a row where a character or a string should be rendered.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.core.model.row.template.IRowTemplate
 */
public final class Element
{
    /**
     * The dimension of the element.
     */
    @NotNull
    private final Dimension dimension;

    /**
     * The position of the element.
     */
    @NotNull
    private final Position position;

    /**
     * Creates a new element.
     *
     * @param dimension the dimension of the element.
     * @param position  the position of the element.
     */
    public Element(@NotNull final Dimension dimension, @NotNull final Position position)
    {
        super();
        this.dimension = dimension;
        this.position = position;
    }

    /**
     * The position of the element.
     *
     * @return the position.
     */
    @NotNull
    public Position position()
    {
        return position;
    }

    /**
     * The dimension of the element.
     *
     * @return the dimension.
     */
    @NotNull
    public Dimension dimension()
    {
        return dimension;
    }

    /**
     * Returns the x-position of the element, shorthand for <code>position().x()</code>.
     *
     * @return the x-position.
     */
    public int x()
    {
        return position.x();
    }

    /**
     * Returns the y-position of the element, shorthand for <code>position().y()</code>.
     *
     * @return the y-position.
     */
    public int y()
    {
        return position.y();
    }

    /**
     * Returns the width of the element, shorthand for <code>dimension().width()</code>.
     *
     * @return the width.
     */
    public int width()
    {
        return dimension.width();
    }

    /**
     * Returns the height of the element, shorthand for <code>dimension().height()</code>.
     *
     * @return the height.
     */
    public int height()
    {
        return dimension.height();
    }

    /**
     * Returns the right of the element, shorthand for <code>x() + width()</code>.
     *
     * @return the right position.
     */
    public int right()
    {
        return position.x() + dimension.width();
    }

    /**
     * Returns the right of the element, shorthand for <code>y() + height()</code>.
     *
     * @return the bottom position.
     */
    public int bottom()
    {
        return position.y() + dimension.height();
    }

    /**
     * Checks if the position is inside the element.
     *
     * @param position the position to check.
     * @return <code>true</code> if inside otherwise <code>false</code>.
     */
    public boolean contains(@NotNull final Point position)
    {
        return containsX(position.x) && containsY(position.y);
    }

    /**
     * Checks if the x-position is horizontal inside the element.
     *
     * @param xPosition the x-position to check.
     * @return <code>true</code> if horizontal inside otherwise <code>false</code>.
     */
    public boolean containsX(final int xPosition)
    {
        return xPosition >= position.x() && xPosition < right();
    }

    /**
     * Checks if the y-position is vertical inside the element.
     *
     * @param yPosition the y-position to check.
     * @return <code>true</code> if vertical inside otherwise <code>false</code>.
     */
    public boolean containsY(final int yPosition)
    {
        return yPosition >= position.y() && yPosition < bottom();
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
                "[dimension=" + dimension + ", position=" + position + "]";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Element))
        {
            return false;
        }
        Element element = (Element) o;
        return dimension.equals(element.dimension) &&
                position.equals(element.position);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(dimension, position);
    }
}
