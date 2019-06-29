package cms.rendner.hexviewer.core.model.row.template.elements;

/**
 * Position of an element.
 *
 * @author rendner
 */
public class ElementPosition implements IElement.IPosition
{
    /**
     * The x.
     */
    private final int x;
    /**
     * The y.
     */
    private final int y;

    /**
     * Creates a new instance.
     *
     * @param x the x.
     * @param y the y.
     */
    public ElementPosition(final int x, final int y)
    {
        super();

        this.x = x;
        this.y = y;
    }

    @Override
    public int x()
    {
        return x;
    }

    @Override
    public int y()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
                "[x=" + x + ", y=" + y + "]";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ElementPosition))
        {
            return false;
        }

        ElementPosition that = (ElementPosition) o;

        if (x != that.x)
        {
            return false;
        }
        return y == that.y;

    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
