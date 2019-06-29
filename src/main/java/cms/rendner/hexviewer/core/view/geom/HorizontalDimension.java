package cms.rendner.hexviewer.core.view.geom;

import cms.rendner.hexviewer.utils.CheckUtils;

/**
 * The HorizontalDimension class encapsulates the x and width of a range of bytes (in integer precision) in a single object.
 *
 * @author rendner
 */
public class HorizontalDimension
{
    /**
     * The start position.
     */
    private int x;

    /**
     * The width.
     */
    private int width;

    /**
     * Creates a new instance.
     * All properties are initialized with <code>0</code>.
     */
    public HorizontalDimension()
    {
        this(0, 0);
    }

    /**
     * Creates a new instance.
     *
     * @param x     the start position.
     * @param width the width (has to be positive).
     * @throws IllegalArgumentException if width is &lt;0.
     */
    public HorizontalDimension(final int x, final int width)
    {
        super();
        setX(x);
        setWidth(width);
    }

    /**
     * Checks if a given "position" lies inside the dimension.
     *
     * @param x the "position" to check.
     * @return if the "position" is contained.
     */
    public boolean contains(final int x)
    {
        return x >= this.x && x < (this.x + width);
    }

    /**
     * Sets the new x.
     *
     * @param x the new start position.
     */
    public void setX(final int x)
    {
        this.x = x;
    }

    /**
     * @return the start position.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Sets the new width.
     *
     * @param width the new width (has to be positive)
     * @throws IllegalArgumentException if width is &lt;0.
     */
    public void setWidth(final int width)
    {
        CheckUtils.checkMinValue(width, 0);
        this.width = width;
    }

    /**
     * @return the width.
     */
    public int getWidth()
    {
        return width;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof HorizontalDimension))
        {
            return false;
        }

        HorizontalDimension that = (HorizontalDimension) o;

        if (x != that.x)
        {
            return false;
        }
        return width == that.width;

    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + width;
        return result;
    }

    /**
     * @return the x and width of the instance prefixed with the name of the class.
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[x:" + x + ",width:" + width + "]";
    }
}