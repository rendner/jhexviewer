package cms.rendner.hexviewer.view.components.areas.common.painter.graphics;

import cms.rendner.hexviewer.common.geom.Range;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplifies the creation of {@link RowGraphics} instances.
 *
 * @author rendner
 */
public class RowGraphicsBuilder
{
    /**
     * Creates a list of RowGraphics objects for each row which displays text content and intersects with the dirty
     * region of a Graphics2D object.
     * <p/>
     * Each entry of this list has a separate Graphics2D object which refers to the local coordinates and shape of the
     * row to which the entry belongs. Therefore the y-position 0 of such a Graphics2D object always refers to the top
     * of the row, no matter on which y-position the row is inside the area.
     *
     * @param g    the Graphics2D object of the area to paint into.
     * @param area the area to which the Graphics2D object belongs.
     * @return a list of RowGraphics objects.
     */
    @NotNull
    public static List<RowGraphics> buildForegroundRowGraphics(@NotNull final Graphics2D g, @NotNull final Area area)
    {
        return getRowGraphics(g, area, getRangeOfDirtyForegroundRows(g, area));
    }

    /**
     * Creates a list of RowGraphics objects for each row which intersects with the dirty region of a Graphics2D object.
     * <p/>
     * Each entry of this list has a separate Graphics2D object which refers to the local coordinates and shape of the
     * row to which the entry belongs. Therefore the y-position 0 of such a Graphics2D object always refers to the top
     * of the row, no matter on which y-position the row is inside the area.
     *
     * @param g    the Graphics2D object of the area to paint into.
     * @param area the area to which the Graphics2D object belongs.
     * @return a list of RowGraphics objects.
     */
    @NotNull
    public static List<RowGraphics> buildBackgroundRowGraphics(@NotNull final Graphics2D g, @NotNull final Area area)
    {
        return getRowGraphics(g, area, getRangeOfDirtyBackgroundRows(g, area));
    }

    /**
     * Calculates the range of rows which intersects with the Graphics2D object.
     *
     * @param g    the Graphics2D object of the area to paint into.
     * @param area the area to which the Graphics2D object belongs.
     * @return a range describing the rows to be repainted.
     */
    @NotNull
    private static Range getRangeOfDirtyBackgroundRows(@NotNull final Graphics2D g, @NotNull final Area area)
    {
        final Rectangle bounds = g.getClipBounds();
        final int rowHeight = area.getRowHeight();
        final int firstRowIndex = bounds.y / rowHeight;
        final int heightInRows = bounds.height / rowHeight;
        return new Range(firstRowIndex, firstRowIndex + heightInRows);
    }

    /**
     * Calculates the range of rows which intersects with the Graphics2D object and contain displayable content.
     *
     * @param g    the Graphics2D object of the area to paint into.
     * @param area the area to which the Graphics2D object belongs.
     * @return a range describing the rows to be repainted.
     */
    @NotNull
    private static Range getRangeOfDirtyForegroundRows(@NotNull final Graphics2D g, @NotNull final Area area)
    {
        return area.getIntersectingRows(g.getClipBounds());
    }

    /**
     * Creates a list of RowGraphics objects for a specified range of rows to simplify row based drawing into a
     * Graphics2D object.
     * <p/>
     * Each entry of this list has a separate Graphics2D object which refers to the local coordinates and shape of the
     * row to which the entry belongs. Therefore the y-position 0 of such a Graphics2D object always refers to the top
     * of the row, no matter on which y-position the row is inside the area.
     *
     * @param g        the Graphics2D object of the area to paint into.
     * @param area     the area to which the Graphics2D object belongs.
     * @param rowRange a range of rows for which a RowGraphics entry should be created. Normally this range describes the
     *                 dirty rows which should be repainted.
     * @return a list of RowGraphics objects for each row which lies in the specified range, or an empty list if the range is invalid.
     */
    @NotNull
    private static List<RowGraphics> getRowGraphics(@NotNull final Graphics2D g, @NotNull final Area area, @NotNull final Range rowRange)
    {
        if (rowRange.isValid())
        {
            return createRowGraphics(g, area, rowRange);
        }
        return Collections.emptyList();
    }

    /**
     * Creates a list of RowGraphics objects for a specified range of rows.
     *
     * @param g        the Graphics2D object of the area to paint into.
     * @param area     the area to which the Graphics2D object belongs.
     * @param rowRange a range of rows for which a RowGraphics entry should be created. Normally this range describes the
     *                 dirty rows which should be repainted. The range has to be valid.
     * @return a list of RowGraphics objects for each row which lies in the specified range.
     */
    @NotNull
    private static List<RowGraphics> createRowGraphics(@NotNull final Graphics2D g, @NotNull final Area area, @NotNull final Range rowRange)
    {
        final List<RowGraphics> result = new ArrayList<>(rowRange.getLength());
        final Rectangle rowBounds = area.getRowRect(rowRange.getStart());

        for (int rowIndex = rowRange.getStart(); rowIndex <= rowRange.getEnd(); rowIndex++)
        {
            final Graphics2D rowGraphics = (Graphics2D) g.create(rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height);
            result.add(new RowGraphics(rowGraphics, rowIndex));

            rowBounds.y += rowBounds.height;
        }

        return result;
    }
}
