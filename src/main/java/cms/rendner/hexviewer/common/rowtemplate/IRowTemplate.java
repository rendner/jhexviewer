package cms.rendner.hexviewer.common.rowtemplate;

import cms.rendner.hexviewer.common.geom.Dimension;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Describes the layout of a row.
 * Each row contains a number of elements which are displayed in a row.
 * <p/>
 * To draw a row element vertical centered into a {@link Graphics2D} object the ascent returned by {@link FontMetrics#getAscent()}
 * has to be added to the {@link Element#y()}. The font metrics object can be retrieved vai {@link IRowTemplate#fontMetrics()}.
 *
 * @author rendner
 */
public interface IRowTemplate
{
    /**
     * The height of the row, shorthand for <code>dimension().height()</code>.
     *
     * @return the height, &gt;= 0.
     */
    int height();

    /**
     * The width of the row, shorthand for <code>dimension().width()</code>.
     *
     * @return the width, &gt;= 0.
     */
    int width();

    /**
     * The dimension of the row.
     *
     * @return the dimension.
     */
    @NotNull
    Dimension dimension();

    /**
     * @return the font to render the text content of the row.
     */
    @NotNull
    FontMetrics fontMetrics();

    /**
     * Checks if the position is inside the row.
     *
     * @param position the position to check.
     * @return <code>true</code> if inside, otherwise false.
     */
    boolean contains(@NotNull Point position);

    /**
     * Checks if the x coordinate is inside the row.
     *
     * @param xPosition the x coordinate to check.
     * @return <code>true</code> if inside, otherwise <code>false</code>.
     */
    boolean containsX(int xPosition);

    /**
     * Checks if the y coordinate is inside the row.
     *
     * @param yPosition the y coordinate to check.
     * @return <code>true</code> if inside, otherwise <code>false</code>.
     */
    boolean containsY(int yPosition);
}
