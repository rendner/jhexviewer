package cms.rendner.hexviewer.core.model.row.template.element;

import java.util.Objects;

/**
 * The dimension of an element.
 *
 * @author rendner
 */
public final class Dimension
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
    public Dimension(final int width, final int height)
    {
        super();
        this.width = width;
        this.height = height;
    }

    /**
     * The width.
     *
     * @return width.
     */
    public int width()
    {
        return width;
    }

    /**
     * The height.
     *
     * @return height.
     */
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
        if (!(o instanceof Dimension))
        {
            return false;
        }
        Dimension dimension = (Dimension) o;
        return width == dimension.width &&
                height == dimension.height;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(width, height);
    }
}
