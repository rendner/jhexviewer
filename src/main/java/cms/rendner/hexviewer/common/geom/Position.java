package cms.rendner.hexviewer.common.geom;

import java.util.Objects;

/**
 * The immutable Position class provides a "x" and "y" property in integer precision.
 *
 * @author rendner
 */
public final class Position
{
    /**
     * The x position.
     */
    private final int x;

    /**
     * The y position.
     */
    private final int y;

    /**
     * Creates a new instance.
     *
     * @param x the x position.
     * @param y the y position.
     */
    public Position(final int x, final int y)
    {
        super();

        this.x = x;
        this.y = y;
    }

    /**
     * @return the x position.
     */
    public int x()
    {
        return x;
    }

    /**
     * @return the y position.
     */
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
        if (!(o instanceof Position))
        {
            return false;
        }
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
}
