package cms.rendner.hexviewer.swing.separator;

import java.awt.*;

/**
 * A separator is a solid vertical or horizontal stroke which separates two components.
 * A Separator instance can be used to add separators to a {@link JSeparatedView}.
 *
 * @author rendner
 */
public class Separator
{
    /**
     * The solid color of this separator.
     */
    private final Color color;

    /**
     * The thickness of this separator.
     */
    private final int thickness;

    /**
     * Creates a new instance with the specified arguments.
     *
     * @param color     the solid color of this separator.
     * @param thickness the thickness of this separator.
     */
    public Separator(final Color color, final int thickness)
    {
        super();
        this.color = color;
        this.thickness = thickness;
    }

    /**
     * Paints the separator with a defined position and height.
     *
     * @param g      the Graphics object in which to paint.
     * @param x      the x position where to paint.
     * @param y      the x position where to paint.
     * @param height the required height for the separator.
     */
    public void paint(final Graphics g, final int x, final int y, final int height)
    {
        if (color != null)
        {
            g.setColor(color);
            g.fillRect(x, y, thickness, height);
        }
    }

    /**
     * @return the thickness of the separator.
     */
    public final int getThickness()
    {
        return thickness;
    }
}
