package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.geom.Dimension;
import cms.rendner.hexviewer.core.model.row.template.element.Element;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Describes the layout of a row.
 * Each row contains a number of elements which are displayed in the row.
 * <p/>
 * If an element should be drawn into a {@link Graphics} object the {@link IRowTemplate#ascent()} should be
 * added to the {@link Element#y()} to center the char vertically inside the row.
 *
 * @author rendner
 */
public interface IRowTemplate
{
    /**
     * The height of the row, shorthand for <code>dimension().height()</code>.
     *
     * @return the height, &gt;=0.
     */
    int height();

    /**
     * The width of the row, shorthand for <code>dimension().width()</code>.
     *
     * @return the width, &gt;=0.
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
     * @return the font to use to render the text content of the row.
     */
    @NotNull
    Font font();

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

    /**
     * The ascent to vertical center a element in the row.
     *
     * @return the ascent.
     */
    int ascent();
}
