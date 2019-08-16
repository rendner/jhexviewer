package cms.rendner.hexviewer.core.model.row.template.element;

import java.util.Objects;

/**
 * Position of an element inside a row.
 *
 * @author rendner
 */
public final class Position
{
    /**
     * The x position inside a row.
     */
    private final int x;

    /**
     * The y position inside a row.
     */
    private final int y;

    /**
     * Creates a new instance.
     *
     * @param x the x position inside a row.
     * @param y the y position inside a row.
     */
    public Position(final int x, final int y)
    {
        super();

        this.x = x;
        this.y = y;
    }

    /**
     * @return the x position inside a row.
     */
    public int x()
    {
        return x;
    }

    /**
     * @return the y position inside a row.
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
