package cms.rendner.hexviewer.core.view.highlight;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.damager.IDamager;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.geom.HorizontalDimension;
import cms.rendner.hexviewer.core.view.geom.Range;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.IndexUtils;
import cms.rendner.hexviewer.utils.RectangleUtils;
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
     * A return value which is used to retrieve the horizontal dimension of all elements displayed in a row.
     * This instance be reused to minimize creation of new HorizontalDimension.
     */
    @NotNull
    private final HorizontalDimension rvRowElementsDimension = new HorizontalDimension();

    /**
     * A return value which is used to retrieve the bounds of all elements displayed in a row.
     * This instance be reused to minimize creation of new Rectangle.
     */
    @NotNull
    private final Rectangle rvRowElementsBounds = new Rectangle();

    /**
     * A return value which is used to retrieve the intersection between the current visible bytes and the region of bytes
     * defined by an highlight.
     * This instance be reused to minimize creation of new Range.
     */
    @NotNull
    private final Range rvVisibleBytesIntersection = new Range();

    /**
     * A return value which is used to retrieve the range of the currently visible bytes.
     * This instance be reused to minimize creation of new Range.
     */
    @NotNull
    private final Range rvVisibleBytes = new Range();

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
        computeVisibleBytes(rowsView, rvVisibleBytes);

        if (!rvVisibleBytes.isEmpty())
        {
            if (selectionHighlight != null || !highlights.isEmpty())
            {
                computeRowElementsDimension(rowsView, rvRowElementsDimension);

                if (paintSelectionBehindHighlights && selectionHighlight != null)
                {
                    paintHighlight(g, selectionHighlight, rowsView, rvRowElementsDimension);
                }

                for (final IHighlight highlight : highlights)
                {
                    paintHighlight(g, highlight, rowsView, rvRowElementsDimension);
                }

                if (!paintSelectionBehindHighlights && selectionHighlight != null)
                {
                    paintHighlight(g, selectionHighlight, rowsView, rvRowElementsDimension);
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
     * @param rowElementsDimension the horizontal dimension which covers all chars/bytes of a single row.
     *                             Used to know the min and max possible horizontal position for a highlight.
     */
    protected void paintHighlight(@NotNull final Graphics g, @NotNull final IHighlight highlight, @NotNull final ByteRowsView byteRowsView, final HorizontalDimension rowElementsDimension)
    {
        final int start = highlight.getStartOffset();
        final int end = highlight.getEndOffset();

        rvVisibleBytes.computeIntersection(start, end, rvVisibleBytesIntersection);

        if (!rvVisibleBytesIntersection.isEmpty())
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
     * @param returnValue the result is returned in this HorizontalDimension.
     * @return the adjusted <code>returnValue</code>
     */
    @NotNull
    protected HorizontalDimension computeRowElementsDimension(@NotNull final ByteRowsView rowsView, @NotNull final HorizontalDimension returnValue)
    {
        final IRowTemplate rowTemplate = rowsView.template();
        rowTemplate.elementBounds(0, rowTemplate.elementCount() - 1, rvRowElementsBounds);
        returnValue.setX(rvRowElementsBounds.x);
        returnValue.setWidth(rvRowElementsBounds.width);
        return returnValue;
    }

    /**
     * Computes the number of potentially visible bytes inside a rows-view.
     * <p/>
     * This method doesn't check which bytes are fully visibly and if the number
     * of bytes is really available. Instead the number of bytes which can be potentially displayed starting from
     * the first byte of the leading row until the last byte of the trailing row (inclusive) are returned.
     *
     * @param rowsView    the view for which the "visible" bytes should be computed.
     * @param returnValue the object in which the result should be stored.
     * @return the adjusted <code>returnValue</code>
     */
    @NotNull
    protected Range computeVisibleBytes(@NotNull final ByteRowsView rowsView, @NotNull final Range returnValue)
    {
        final Rectangle rectangle = rowsView.getVisibleRect();
        rowsView.getRowRange(rectangle, returnValue);

        if (!returnValue.isEmpty())
        {
            final int bytesPerRow = rowsView.bytesPerRow();
            final int firstByte = IndexUtils.rowIndexToByteIndex(returnValue.getStart(), bytesPerRow);
            final int lastByte = IndexUtils.rowIndexToByteIndex(returnValue.getEnd() + bytesPerRow - 1, bytesPerRow);
            returnValue.resize(firstByte, lastByte);
        }

        return returnValue;
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
        final IDamager damager = hexViewer.getDamager();
        if(damager != null)
        {
            damager.damageChangedHighlight(oldStart, oldEnd, newStart, newEnd);
        }
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
         * A return value which is used to retrieve the bounds of the start byte of the highlighted region.
         * This instance be reused to minimize creation of new rectangles.
         */
        @NotNull
        private final Rectangle rvStartRect = new Rectangle();

        /**
         * A return value which is used to retrieve the bounds of the end byte of the highlighted region.
         * This instance be reused to minimize creation of new rectangles.
         */
        @NotNull
        private final Rectangle rvEndRect = new Rectangle();

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

                rowsView.getByteRect(byteStartIndex, rvStartRect);
                rowsView.getByteRect(byteEndIndex, rvEndRect);

                if (rvStartRect.y == rvEndRect.y)
                {
                    // same line
                    final Rectangle r = RectangleUtils.computeUnion(rvStartRect, rvEndRect);
                    g.fillRect(r.x, r.y, r.width, r.height);
                }
                else
                {
                    // different lines
                    final int widthOfStart = rowElementsDimension.getX() + rowElementsDimension.getWidth() - rvStartRect.x;
                    g.fillRect(rvStartRect.x, rvStartRect.y, widthOfStart, rvStartRect.height);

                    if ((rvStartRect.y + rvStartRect.height) != rvEndRect.y)
                    {
                        g.fillRect(rowElementsDimension.getX(),
                                rvStartRect.y + rvStartRect.height,
                                rowElementsDimension.getWidth(),
                                rvEndRect.y - (rvStartRect.y + rvStartRect.height));
                    }

                    final int widthOfEnd = (rvEndRect.x - rowElementsDimension.getX()) + rvEndRect.width;
                    g.fillRect(rowElementsDimension.getX(), rvEndRect.y, widthOfEnd, rvEndRect.height);
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
