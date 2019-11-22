package cms.rendner.hexviewer.view.components.highlighter;

import cms.rendner.hexviewer.common.geom.HDimension;
import cms.rendner.hexviewer.common.ranges.ByteRange;
import cms.rendner.hexviewer.common.ranges.RowRange;
import cms.rendner.hexviewer.common.rowtemplate.bytes.IByteRowTemplate;
import cms.rendner.hexviewer.common.utils.ObjectUtils;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Default implementation of a highlighter.
 * <p>
 * Highlight which doesn't specify a own painter are painted by an internal default one. The paint color for this default
 * painter are retrieved from the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider IAreaColorProvider}
 * of the byte-area in which the highlights are painted. If the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider IAreaColorProvider}
 * returns {@code null} then the color {@code Color.yellow} will be used to paint such highlights.
 *
 * @author rendner
 */
public class DefaultHighlighter extends AbstractHighlighter
{
    /**
     * Default painter which is used if no custom painter has been defined for a highlight.
     */
    @NotNull
    private final IHighlightPainter defaultPainter = new DefaultHighlightPainter();

    @NotNull
    @Override
    public IHighlight addHighlight(final long startByteIndex, final long endByteIndex)
    {
        final HighlightInfo info = new HighlightInfo(startByteIndex, endByteIndex, null);
        highlights.add(info);
        damageBytes(startByteIndex, endByteIndex);
        return info;
    }

    @NotNull
    @Override
    public IHighlight addHighlight(final long startByteIndex, final long endByteIndex, @NotNull final Color color)
    {
        return addHighlight(startByteIndex, endByteIndex, new DefaultHighlightPainter(color));
    }

    @NotNull
    @Override
    public IHighlight addHighlight(final long startByteIndex, final long endByteIndex, @NotNull final IHighlightPainter painter)
    {
        final HighlightInfo info = new HighlightInfo(startByteIndex, endByteIndex, painter);
        highlights.add(info);
        damageBytes(startByteIndex, endByteIndex);
        return info;
    }

    @Override
    public void changeHighlight(@NotNull final IHighlight highlight, final long startByteIndex, final long endByteIndex)
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
    public void paint(@NotNull final Graphics2D g, @NotNull final ByteArea area)
    {
        if (!highlights.isEmpty())
        {
            final ByteRange visibleBytes = computeVisibleBytes(area);

            if (visibleBytes.isValid())
            {
                area.getRowTemplate().ifPresent(rowTemplate -> {
                    final HDimension rowElementsHDimension = computeRowElementsHDimension(rowTemplate);
                    highlights.forEach(entry -> paintHighlight(g, entry, area, visibleBytes, rowElementsHDimension));
                });
            }
        }
    }

    /**
     * Paints a highlight.
     *
     * @param g                     the Graphics2D context in which to paint.
     * @param highlight             the highlight to paint.
     * @param area                  the component to paint into.
     * @param visibleBytes          the range of visible bytes.
     * @param rowElementsHDimension the bounds of all elements in the row.
     */
    protected void paintHighlight(@NotNull final Graphics2D g, @NotNull final IHighlight highlight, @NotNull final ByteArea area, @NotNull final ByteRange visibleBytes, @NotNull final HDimension rowElementsHDimension)
    {
        final long start = highlight.getStartOffset();
        final long end = highlight.getEndOffset();

        final ByteRange visibleHighlightedBytes = visibleBytes.computeIntersection(start, end);

        if (visibleHighlightedBytes.isValid())
        {
            final IHighlightPainter highlightPainter = highlight.getPainter();
            final IHighlightPainter painter = highlightPainter == null ? defaultPainter : highlightPainter;
            painter.paint(g, area, rowElementsHDimension, start, end);
        }
    }

    /**
     * Computes the horizontal bounds of all elements of a area row .
     *
     * @param rowTemplate the template describing the layout of the rows of the area component.
     * @return the horizontal bounds of the row elements
     */
    @NotNull
    protected HDimension computeRowElementsHDimension(@NotNull IByteRowTemplate rowTemplate)
    {
        final Rectangle elementBounds = rowTemplate.elementBounds(0, rowTemplate.elementCount() - 1);
        return new HDimension(elementBounds.x, elementBounds.width);
    }

    /**
     * Computes the number of potentially visible bytes inside a rows-view.
     * <p/>
     * This method doesn't check which bytes are fully visibly and if the number
     * of bytes is really available. Instead the number of bytes which can be potentially displayed starting from
     * the first byte of the leading row until the last byte of the trailing row (inclusive) are returned.
     *
     * @param area the area component for which the "visible" bytes should be computed.
     * @return the number of potentially visible bytes inside the rows-view
     */
    @NotNull
    protected ByteRange computeVisibleBytes(@NotNull final ByteArea area)
    {
        final Rectangle rectangle = area.getVisibleRect();
        final RowRange rowRange = area.getIntersectingRows(rectangle);

        if (rowRange.isValid())
        {
            final long firstByte = hexViewer.rowIndexToByteIndex(rowRange.getStart());
            final long lastByte = hexViewer.rowIndexToByteIndex(rowRange.getEnd() + hexViewer.getBytesPerRow() - 1);
            return new ByteRange(firstByte, lastByte);
        }

        return ByteRange.INVALID;
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
    protected void damageChangedHighlight(final long oldStart, final long oldEnd, final long newStart, final long newEnd)
    {
        hexViewer.getDamager().ifPresent(damager -> damager.damageChangedHighlight(
                Math.min(oldStart, oldEnd),
                Math.max(oldStart, oldEnd),
                Math.min(newStart, newEnd),
                Math.max(newStart, newEnd)));
    }

    /**
     * Simple highlight painter that fills a highlighted area with
     * a solid color.
     */
    public static class DefaultHighlightPainter implements IHighlightPainter
    {
        /**
         * Fallback color.
         */
        @NotNull
        protected final Color fallbackColor;

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
         * @param fallbackColor the fallback color, used when {@link DefaultHighlightPainter#getColor(ByteArea)} returns <code>null</code>.
         */
        public DefaultHighlightPainter(@NotNull final Color fallbackColor)
        {
            super();
            this.fallbackColor = fallbackColor;
        }

        /**
         * Returns the highlight color to use to paint the highlight into a specified area.
         *
         * @param area the area to paint into.
         * @return the color to use to paint the highlight, can be <code>null</code>.
         */
        @Nullable
        protected Color getColor(@NotNull final ByteArea area)
        {
            return area.getColorProvider()
                    .map(IByteColorProvider::getDefaultHighlight)
                    .orElse(fallbackColor);
        }

        @Override
        public void paint(@NotNull final Graphics2D g, @NotNull final ByteArea area,
                          @NotNull final HDimension rowElementsHDimension, final long byteStartIndex, final long byteEndIndex)
        {
            g.setColor(ObjectUtils.ifNotNullOtherwise(getColor(area), fallbackColor));

            final Rectangle startByteRect = area.getByteRect(byteStartIndex);
            final Rectangle endByteRect = area.getByteRect(byteEndIndex);

            if (startByteRect.y == endByteRect.y)
            {
                // same line
                final Rectangle r = startByteRect.union(endByteRect);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
            else
            {
                // different lines
                final int widthOfStart = rowElementsHDimension.getX() + rowElementsHDimension.getWidth() - startByteRect.x;
                g.fillRect(startByteRect.x, startByteRect.y, widthOfStart, startByteRect.height);

                if ((startByteRect.y + startByteRect.height) != endByteRect.y)
                {
                    g.fillRect(rowElementsHDimension.getX(),
                            startByteRect.y + startByteRect.height,
                            rowElementsHDimension.getWidth(),
                            endByteRect.y - (startByteRect.y + startByteRect.height));
                }

                final int widthOfEnd = (endByteRect.x - rowElementsHDimension.getX()) + endByteRect.width;
                g.fillRect(rowElementsHDimension.getX(), endByteRect.y, widthOfEnd, endByteRect.height);
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
        long start;

        /**
         * The end offset for this highlight.
         */
        long end;

        /**
         * Creates a new instance with a custom painter.
         *
         * @param start   the start offset for the highlight.
         * @param end     the end offset for the highlight.
         * @param painter painter to use to paint this highlight.
         */
        HighlightInfo(final long start, final long end, @Nullable final IHighlighter.IHighlightPainter painter)
        {
            super();
            this.start = start;
            this.end = end;
            this.painter = painter;
        }

        public long getStartOffset()
        {
            return start;
        }

        public long getEndOffset()
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
