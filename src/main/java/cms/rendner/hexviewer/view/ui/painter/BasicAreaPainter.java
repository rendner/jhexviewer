package cms.rendner.hexviewer.view.ui.painter;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.geom.Range;
import cms.rendner.hexviewer.common.rowtemplate.IRowTemplate;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IFullBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IRowBasedBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for area painters.
 * <p/>
 * An area painter is responsible for painting the background and foreground of the area to which the painter
 * belongs. To allow customizing of the paint process this class uses an {@link IAreaForegroundPainter} and
 * {@link IAreaBackgroundPainter} which can be exchanged during runtime.
 * <p/>
 * The painting of the area is fully delegated to the {@link IAreaForegroundPainter} and {@link IAreaBackgroundPainter}. This class
 * only provides the required data to paint the area.
 *
 * @param <A> the type of the area which is painted by this class.
 * @author rendner
 */
public abstract class BasicAreaPainter<A extends Area<
        ? extends IRowTemplate,
        ? extends IAreaColorProvider,
        ? extends IValueFormatter>> implements IAreaPainter
{
    /**
     * The area to be painted.
     */
    @NotNull
    protected final A area;

    /**
     * Paints the foreground (text) of the rows displayed by the area.
     */
    @Nullable
    protected IAreaForegroundPainter foregroundPainter;

    /**
     * Paints the background of the area.
     */
    @Nullable
    protected IAreaBackgroundPainter backgroundPainter;

    /**
     * Creates a new instance.
     *
     * @param area the area painted by this instance.
     */
    protected BasicAreaPainter(@NotNull final A area)
    {
        this.area = area;
    }

    @Override
    public void setForegroundPainter(@Nullable final IAreaForegroundPainter foregroundPainter)
    {
        if (this.foregroundPainter != foregroundPainter)
        {
            this.foregroundPainter = foregroundPainter;
            area.repaint();
        }
    }

    @Override
    public void setBackgroundPainter(@Nullable final IAreaBackgroundPainter backgroundPainter)
    {
        if (this.backgroundPainter != backgroundPainter)
        {
            this.backgroundPainter = backgroundPainter;
            area.repaint();
        }
    }

    @Override
    public void paint(@NotNull final Graphics2D g)
    {
        if (backgroundPainter != null)
        {
            paintBackground(g);
        }
        if (foregroundPainter != null)
        {
            paintForeground(g);
        }
    }

    /**
     * Paints the foreground of the area by using the foregroundPainter.
     * This method is only called when the foregroundPainter isn't <code>null</code>.
     *
     * @param g the Graphics2D object to paint into.
     */
    protected void paintForeground(@NotNull final Graphics2D g)
    {
        Objects.requireNonNull(foregroundPainter);

        final List<RowGraphics> dirtyRows = getRowGraphics(g, area, getRangeOfDirtyForegroundRows(g, area));

        if (dirtyRows.isEmpty())
        {
            return;
        }

        foregroundPainter.prePaint();

        if(foregroundPainter.canPaint())
        {
            dirtyRows.forEach(entry -> foregroundPainter.paint(entry.g, entry.rowIndex));
        }

        dirtyRows.forEach(RowGraphics::dispose);
        foregroundPainter.postPaint();
    }

    /**
     * Paints the background of the area by using the backgroundPainter.
     * This method is only called when the backgroundPainter isn't <code>null</code>.
     *
     * @param g the Graphics2D object of the area to paint into.
     */
    protected void paintBackground(@NotNull final Graphics2D g)
    {
        Objects.requireNonNull(backgroundPainter);

        if (backgroundPainter instanceof IFullBackgroundPainter)
        {
            ((IFullBackgroundPainter) backgroundPainter).paint(g);
        }
        else if (backgroundPainter instanceof IRowBasedBackgroundPainter)
        {
            final IRowBasedBackgroundPainter rowBasedBackgroundPainter = (IRowBasedBackgroundPainter) backgroundPainter;
            rowBasedBackgroundPainter.prePaint();

            if (rowBasedBackgroundPainter.canPaint())
            {
                final List<RowGraphics> dirtyRows = getRowGraphics(g, area, getRangeOfDirtyBackgroundRows(g, area));

                if (!dirtyRows.isEmpty())
                {
                    final RowGraphics lastEntry = dirtyRows.get(dirtyRows.size() - 1);

                    dirtyRows.forEach(entry -> {
                        rowBasedBackgroundPainter.paint(entry.g, entry.rowIndex, lastEntry == entry);
                        entry.dispose();
                    });
                }
            }
            rowBasedBackgroundPainter.postPaint();
        }
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
    protected List<RowGraphics> getRowGraphics(@NotNull final Graphics2D g, @NotNull final A area, @NotNull final Range rowRange)
    {
        if (rowRange.isValid())
        {
            return createRowGraphics(g, area, rowRange);
        }
        return Collections.emptyList();
    }

    /**
     * Calculates the range of rows which intersects with the Graphics2D object.
     *
     * @param g    the Graphics2D object of the area to paint into.
     * @param area the area to which the Graphics2D object belongs.
     * @return a range describing the rows to be repainted.
     */
    @NotNull
    protected Range getRangeOfDirtyBackgroundRows(@NotNull final Graphics2D g, @NotNull final A area)
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
    protected Range getRangeOfDirtyForegroundRows(@NotNull final Graphics2D g, @NotNull final A area)
    {
        return area.getIntersectingRows(g.getClipBounds());
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
    private List<RowGraphics> createRowGraphics(@NotNull final Graphics2D g, @NotNull final A area, @NotNull final Range rowRange)
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

    protected static class RowGraphics
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

        RowGraphics(@NotNull final Graphics2D g, final int rowIndex)
        {
            super();

            this.g = g;
            this.rowIndex = rowIndex;
        }

        /**
         * Disposes the Graphics2D object of this instance.
         */
        public void dispose()
        {
            g.dispose();
        }
    }
}
