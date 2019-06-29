package cms.rendner.hexviewer.core.model.row.template.elements;

import cms.rendner.hexviewer.utils.CheckUtils;

import java.awt.*;

/**
 * Represents an element with a position and dimension.
 *
 * @author rendner
 */
public class Element implements IElement
{
    /**
     * The dimension of the element.
     */
    private final IDimension dimension;
    /**
     * The position of the element.
     */
    private final IPosition position;

    /**
     * Creates a new element.
     *
     * @param dimension the dimension of the element.
     * @param position  the position of the element.
     */
    public Element(final IDimension dimension, final IPosition position)
    {
        super();

        CheckUtils.checkNotNull(dimension);
        CheckUtils.checkNotNull(position);

        this.dimension = dimension;
        this.position = position;
    }

    @Override
    public Rectangle toRectangle(final Rectangle returnValue)
    {
        returnValue.x = position.x();
        returnValue.y = position.y();

        returnValue.width = dimension.width();
        returnValue.height = dimension.height();

        return returnValue;
    }

    @Override
    public IPosition position()
    {
        return position;
    }

    @Override
    public IDimension dimension()
    {
        return dimension;
    }

    @Override
    public int x()
    {
        return position.x();
    }

    @Override
    public int width()
    {
        return dimension.width();
    }

    @Override
    public int right()
    {
        return position.x() + dimension.width();
    }

    @Override
    public int bottom()
    {
        return position.y() + dimension.height();
    }

    @Override
    public int y()
    {
        return position.y();
    }

    @Override
    public int height()
    {
        return dimension.height();
    }

    @Override
    public boolean contains(final Point position)
    {
        CheckUtils.checkNotNull(position);
        return containsX(position.x) && containsY(position.y);
    }

    @Override
    public boolean containsX(final int xPosition)
    {
        return xPosition >= position.x() && xPosition < right();
    }

    @Override
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

        if (!dimension.equals(element.dimension))
        {
            return false;
        }
        return position.equals(element.position);

    }

    @Override
    public int hashCode()
    {
        int result = dimension.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }
}
