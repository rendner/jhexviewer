package cms.rendner.hexviewer.common.geom;

import cms.rendner.hexviewer.common.utils.CheckUtils;

import java.util.Objects;

/**
 * The immutable Dimension class provides a "width" and "height" property in integer precision.
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
     * @param width  the width, &gt;= 0.
     * @param height the height, &gt;= 0.
     */
    public Dimension(final int width, final int height)
    {
        super();
        CheckUtils.checkMinValue(width, 0);
        CheckUtils.checkMinValue(height, 0);
        this.width = width;
        this.height = height;
    }

    /**
     * The width.
     *
     * @return width, &gt;= 0.
     */
    public int width()
    {
        return width;
    }

    /**
     * The height.
     *
     * @return height, &gt;= 0.
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
