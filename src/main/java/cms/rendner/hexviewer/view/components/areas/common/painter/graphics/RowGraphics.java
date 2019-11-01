package cms.rendner.hexviewer.view.components.areas.common.painter.graphics;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * RowGraphics provides a Graphics2D object and the index of the row to which the RowGraphics object belongs.
 * <p/>
 * To convert/slice a Graphics2D object of an area into RowGraphics use the {@link RowGraphicsBuilder}.
 *
 * @author rendner
 */
public final class RowGraphics
{
    /**
     * The Graphics2D object to paint the row.
     */
    @NotNull
    public final Graphics2D g;

    /**
     * The index to which row the Graphics2D belongs.
     */
    public final int rowIndex;

    /**
     * Creates a new instance
     *
     * @param g        the Graphics2D object to paint the row.
     * @param rowIndex the index to which row the Graphics2D belongs.
     */
    public RowGraphics(@NotNull final Graphics2D g, final int rowIndex)
    {
        super();

        this.g = g;
        this.rowIndex = rowIndex;
    }

    /**
     * Disposes the Graphics2D object of this instance.
     * The Graphics object can't be used after dispose has been called.
     */
    public void dispose()
    {
        g.dispose();
    }
}
