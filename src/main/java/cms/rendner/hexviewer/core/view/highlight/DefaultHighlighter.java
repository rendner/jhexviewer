package cms.rendner.hexviewer.core.view.highlight;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.geom.HorizontalDimension;
import cms.rendner.hexviewer.core.view.geom.Range;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.IndexUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Default implementation of a highlighter which uses a default highlight painter that renders in a solid color.
 *
 * @author rendner
 */
public class DefaultHighlighter extends AbstractHighlighter
{
    /**
     * Default painter which is used if no custom painter has been defined for a highlight.
     */
    @NotNull
    private IHighlightPainter defaultPainter = new DefaultHighlightPainter();

    @Override
    public void setDefaultColor(@NotNull final Color newColor)
    {
        CheckUtils.checkNotNull(newColor);

        defaultPainter = new DefaultHighlightPainter(newColor);

        // damage all highlights which are painted by the default highlighter
        for (final IHighlight highlight : highlights)
        {
            if (highlight.getPainter() == null)
            {
                damageBytes(highlight.getStartOffset(), highlight.getEndOffset());
            }
        }
    }

    @NotNull
    @Override
    public IHighlight addHighlight(final int startByteIndex, final int endByteIndex)
    {
        final HighlightInfo info = new HighlightInfo(startByteIndex, endByteIndex, null);
        highlights.add(info);
        damageBytes(startByteIndex, endByteIndex);
        return info;
    }

    @NotNull
    @Override
    public IHighlight addHighlight(final int startByteIndex, final int endByteIndex, @NotNull final Color color)
    {
        return addHighlight(startByteIndex, endByteIndex, new DefaultHighlightPainter(color));
    }

    @NotNull
    @Override
    public IHighlight addHighlight(final int startByteIndex, final int endByteIndex, @NotNull final IHighlightPainter painter)
    {
        final HighlightInfo info = new HighlightInfo(startByteIndex, endByteIndex, painter);
        highlights.add(info);
        damageBytes(startByteIndex, endByteIndex);
        return info;
    }

    @NotNull
    @Override
    public IHighlight setSelectionHighlight(final int startByteIndex, final int endByteIndex, @NotNull final IHighlightPainter painter)
    {
        selectionHighlight = new HighlightInfo(startByteIndex, endByteIndex, painter);
        damageBytes(startByteIndex, endByteIndex);
        return selectionHighlight;
    }

    @Override
    public void changeHighlight(@NotNull final IHighlight highlight, final int startByteIndex, final int endByteIndex)
    {
        if (highlight instanceof HighlightInfo)
        {
            final HighlightInfo info = (HighlightInfo) highlight;
            damageChangedHighlight(info.start, info.end, startByteIndex, endByteIndex);
            info.start = startByteIndex;
            info.end = endByteIndex;
        }
    }

    @Override
    public void paint(@NotNull final Graphics g, @NotNull final ByteRowsView rowsView)
    {
        final Range visibleBytes = computeVisibleBytes(rowsView);

        if (!visibleBytes.isEmpty())
        {
            if (selectionHighlight != null || !highlights.isEmpty())
            {
                final HorizontalDimension rowElementsDimension = computeRowElementsDimension(rowsView);

                if (paintSelectionBehindHighlights && selectionHighlight != null)
                {
                    paintHighlight(g, selectionHighlight, rowsView, visibleBytes, rowElementsDimension);
                }

                for (final IHighlight highlight : highlights)
                {
                    paintHighlight(g, highlight, rowsView, visibleBytes, rowElementsDimension);
                }

                if (!paintSelectionBehindHighlights && selectionHighlight != null)
                {
                    paintHighlight(g, selectionHighlight, rowsView, visibleBytes, rowElementsDimension);
                }
            }
        }
    }

    /**
     * Paints a highlight.
     *
     * @param g                    the Graphics context in which to paint.
     * @param highlight            the highlight to paint.
     * @param byteRowsView         the component being painted.
     * @param visibleBytes the range of visible bytes.
     * @param rowElementsDimension the bounds of all elements in the row.
     */
    protected void paintHighlight(@NotNull final Graphics g, @NotNull final IHighlight highlight, @NotNull final ByteRowsView byteRowsView, @NotNull final Range visibleBytes, @NotNull final HorizontalDimension rowElementsDimension)
    {
        final int start = highlight.getStartOffset();
        final int end = highlight.getEndOffset();

        final Range visibleHighlightedBytes = visibleBytes.computeIntersection(start, end);

        if (!visibleHighlightedBytes.isEmpty())
        {
            final IHighlightPainter highlightPainter = highlight.getPainter();
            final IHighlightPainter painter = highlightPainter == null ? defaultPainter : highlightPainter;
            painter.paint(g, hexViewer, byteRowsView, rowElementsDimension, start, end);
        }
    }

    /**
     * Computes the bounds of all elements of a row displayed in the specified view.
     *
     * @param rowsView    the view to calculate the bounds for.
     * @return the horizontal dimension
     */
    @NotNull
    protected HorizontalDimension computeRowElementsDimension(@NotNull final ByteRowsView rowsView)
    {
        return rowsView.template().map(rowTemplate -> {
            final Rectangle elementBounds = rowTemplate.elementBounds(0, rowTemplate.elementCount() - 1);
            return new HorizontalDimension(elementBounds.x, elementBounds.width);
        }).orElse(HorizontalDimension.EMPTY);
    }

    /**
     * Computes the number of potentially visible bytes inside a rows-view.
     * <p/>
     * This method doesn't check which bytes are fully visibly and if the number
     * of bytes is really available. Instead the number of bytes which can be potentially displayed starting from
     * the first byte of the leading row until the last byte of the trailing row (inclusive) are returned.
     *
     * @param rowsView    the view for which the "visible" bytes should be computed.
     * @return the number of potentially visible bytes inside the rows-view
     */
    @NotNull
    protected Range computeVisibleBytes(@NotNull final ByteRowsView rowsView)
    {
        final Rectangle rectangle = rowsView.getVisibleRect();
        final Range rowRange = rowsView.getRowRange(rectangle);

        if (!rowRange.isEmpty())
        {
            final int bytesPerRow = rowsView.bytesPerRow();
            final int firstByte = IndexUtils.rowIndexToByteIndex(rowRange.getStart(), bytesPerRow);
            final int lastByte = IndexUtils.rowIndexToByteIndex(rowRange.getEnd() + bytesPerRow - 1, bytesPerRow);
            return new Range(firstByte, lastByte);
        }

        return rowRange;
    }

    /**
     * Marks the regions for an updated highlight for repaint.
     * The IDamager bound to the associated JHexViewer is used to damage the areas.
     *
     * @param oldStart the old start offset of the updated highlight.
     * @param oldEnd   the old end offset of the updated highlight.
     * @param newStart the new start offset of the updated highlight.
     * @param newEnd   the new end offset of the updated highlight.
     */
    protected void damageChangedHighlight(final int oldStart, final int oldEnd, final int newStart, final int newEnd)
    {
        hexViewer.getDamager().ifPresent(damager -> damager.damageChangedHighlight(oldStart, oldEnd, newStart, newEnd));
    }

    /**
     * Simple highlight painter that fills a highlighted area with
     * a solid color.
     */
    public static class DefaultHighlightPainter implements IHighlightPainter
    {
        /**
         * Color used to fill the highlighted region.
         */
        @NotNull
        private final Color color;

        /**
         * Creates a new instance and sets the used color to yellow.
         */
        public DefaultHighlightPainter()
        {
            this(Color.yellow);
        }

        /**
         * Creates a new instance with the specified color.
         *
         * @param color the color to fill a highlighted region.
         */
        public DefaultHighlightPainter(@NotNull final Color color)
        {
            super();
            this.color = color;
        }

        /**
         * Returns the highlight color to use to paint the highlight into a specified area.
         *
         * @param hexViewer reference to the {@link JHexViewer} component.
         * @param id        the id of the area to paint into.
         * @return the color to use to paint the highlight, can be <code>null</code>.
         */
        @Nullable
        protected Color getColor(@NotNull final JHexViewer hexViewer, @NotNull final AreaId id)
        {
            return color;
        }

        @Override
        public void paint(@NotNull final Graphics g, @NotNull final JHexViewer hexViewer, @NotNull final ByteRowsView rowsView,
                          @NotNull final HorizontalDimension rowElementsDimension, final int byteStartIndex, final int byteEndIndex)
        {
            final Color color = getColor(hexViewer, rowsView.getId());

            if (color != null)
            {
                g.setColor(color);

                final Rectangle startByteRect = rowsView.getByteRect(byteStartIndex);
                final Rectangle endByteRect = rowsView.getByteRect(byteEndIndex);

                if (startByteRect.y == endByteRect.y)
                {
                    // same line
                    final Rectangle r = startByteRect.union(endByteRect);
                    g.fillRect(r.x, r.y, r.width, r.height);
                }
                else
                {
                    // different lines
                    final int widthOfStart = rowElementsDimension.getX() + rowElementsDimension.getWidth() - startByteRect.x;
                    g.fillRect(startByteRect.x, startByteRect.y, widthOfStart, startByteRect.height);

                    if ((startByteRect.y + startByteRect.height) != endByteRect.y)
                    {
                        g.fillRect(rowElementsDimension.getX(),
                                startByteRect.y + startByteRect.height,
                                rowElementsDimension.getWidth(),
                                endByteRect.y - (startByteRect.y + startByteRect.height));
                    }

                    final int widthOfEnd = (endByteRect.x - rowElementsDimension.getX()) + endByteRect.width;
                    g.fillRect(rowElementsDimension.getX(), endByteRect.y, widthOfEnd, endByteRect.height);
                }
            }
        }
    }

    /**
     * Internal class to keep track of the highlighted regions and the specified painter.
     */
    static final class HighlightInfo implements IHighlighter.IHighlight
    {
        /**
         * Painter to use to paint this highlight.
         */
        @Nullable
        final IHighlighter.IHighlightPainter painter;

        /**
         * The start offset for this highlight.
         */
        int start;

        /**
         * The end offset for this highlight.
         */
        int end;

        /**
         * Creates a new instance with a custom painter.
         *
         * @param start   the start offset for the highlight.
         * @param end     the end offset for the highlight.
         * @param painter painter to use to paint this highlight.
         */
        HighlightInfo(final int start, final int end, @Nullable final IHighlighter.IHighlightPainter painter)
        {
            super();
            this.start = start;
            this.end = end;
            this.painter = painter;
        }

        public int getStartOffset()
        {
            return start;
        }

        public int getEndOffset()
        {
            return end;
        }

        @Nullable
        public IHighlighter.IHighlightPainter getPainter()
        {
            return painter;
        }
    }
}
