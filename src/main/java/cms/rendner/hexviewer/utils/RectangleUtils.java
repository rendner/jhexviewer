package cms.rendner.hexviewer.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Utils to work with Rectangle objects.
 *
 * @author rendner
 */
public class RectangleUtils
{
    /**
     * Convenience method that calculates the union of two rectangles
     * without allocating a new rectangle.
     *
     * @param first    the coordinates of the first rectangle
     * @param rvSecond the coordinates of the second rectangle; the union
     *                 of the two rectangles is applied to this rectangle
     * @return the <code>rvSecond</code> <code>Rectangle</code>
     */
    @NotNull
    public static Rectangle computeUnion(@NotNull final Rectangle first, @NotNull final Rectangle rvSecond)
    {
        final int x1 = (first.x < rvSecond.x) ? first.x : rvSecond.x;
        final int x2 = ((first.x + first.width) > (rvSecond.x + rvSecond.width)) ? (first.x + first.width) : (rvSecond.x + rvSecond.width);
        final int y1 = (first.y < rvSecond.y) ? first.y : rvSecond.y;
        final int y2 = ((first.y + first.height) > (rvSecond.y + rvSecond.height)) ? (first.y + first.height) : (rvSecond.y + rvSecond.height);

        rvSecond.x = x1;
        rvSecond.y = y1;
        rvSecond.width = (x2 - x1);
        rvSecond.height = (y2 - y1);

        return rvSecond;
    }

    /**
     * Sets all properties (x, y width and height) of a Rectangle to zero.
     *
     * @param r the rectangle to reset.
     */
    public static void setEmpty(@NotNull final Rectangle r)
    {
        r.x = r.y = r.width = r.height = 0;
    }
}
