package cms.rendner.hexviewer.core.model.row.template.elements;

/**
 * The dimension of an element.
 *
 * @author rendner
 */
public class ElementDimension implements IElement.IDimension
{
    /**
     * The width.
     */
    private final int width;
    /**
     * The height.
     */
    private final int height;

    /**
     * Creates a new instance.
     *
     * @param width  the width.
     * @param height the height.
     */
    public ElementDimension(final int width, final int height)
    {
        super();
        this.width = width;
        this.height = height;
    }

    @Override
    public int width()
    {
        return width;
    }

    @Override
    public int height()
    {
        return height;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
                "[width=" + width + ", height=" + height + "]";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ElementDimension))
        {
            return false;
        }

        ElementDimension that = (ElementDimension) o;

        if (width != that.width)
        {
            return false;
        }
        return height == that.height;

    }

    @Override
    public int hashCode()
    {
        int result = width;
        result = 31 * result + height;
        return result;
    }
}
