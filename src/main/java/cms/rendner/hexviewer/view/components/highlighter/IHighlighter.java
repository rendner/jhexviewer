package cms.rendner.hexviewer.view.components.highlighter;

import cms.rendner.hexviewer.common.geom.HDimension;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * A highlighter adds support for highlighting bytes in a {@link ByteArea}.
 * <p/>
 * The highlights and the painting of the highlights are maintained by the highlighter.
 *
 * @author rendner
 */
public interface IHighlighter
{
    /**
     * Installs the highlighter to the {@link JHexViewer}.
     *
     * @param hexViewer the component to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the highlighter from the {@link JHexViewer}.
     *
     * @param hexViewer the component to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Renders the highlights.
     *
     * @param g    the Graphics2D context of the area in which the highlights should be painted.
     * @param area the area in which the highlights should be painted.
     */
    void paint(@NotNull Graphics2D g, @NotNull ByteArea area);

    /**
     * Adds a highlight to the view.
     * Returns the created highlight that can be used to refer to the highlight.
     *
     * @param startByteIndex the beginning of the range &gt;= 0
     * @param endByteIndex   the end of the range &gt;= startByteIndex
     * @param painter        the painter to use for the highlighting
     * @return an object that refers to the added highlight
     */
    @NotNull
    IHighlight addHighlight(long startByteIndex, long endByteIndex, @NotNull IHighlightPainter painter);

    /**
     * Adds a highlight to the view.
     * Returns the created highlight that can be used to refer to the highlight.
     *
     * @param startByteIndex the beginning of the range &gt;= 0
     * @param endByteIndex   the end of the range &gt;= startByteIndex
     * @return an object that refers to the added highlight
     */
    @NotNull
    IHighlight addHighlight(long startByteIndex, long endByteIndex);

    /**
     * Adds a highlight to the view.
     * Returns the created highlight that can be used to refer to the highlight.
     *
     * @param startByteIndex the beginning of the range &gt;= 0
     * @param endByteIndex   the end of the range &gt;= startByteIndex
     * @param color          the color to use to paint the highlight
     * @return an object that refers to the added highlight
     */
    @NotNull
    IHighlight addHighlight(long startByteIndex, long endByteIndex, @NotNull Color color);

    /**
     * Removes a highlight from the view.
     *
     * @param highlight which highlight to remove
     */
    void removeHighlight(@NotNull IHighlight highlight);

    /**
     * Removes a list of highlights from the view.
     *
     * @param highlights list of highlights to be removed
     */
    void removeHighlights(@NotNull List<IHighlight> highlights);

    /**
     * Removes all highlights this highlighter is responsible for.
     */
    void removeAllHighlights();

    /**
     * Changes the given highlight to span a different range of bytes. This may be more efficient than a remove/add
     * when a selection is expanding/shrinking (such as a sweep with a mouse) by damaging only what changed.
     *
     * @param highlight      which highlight to change
     * @param startByteIndex the beginning of the range &gt;= 0
     * @param endByteIndex   the end of the range &gt;= startByteIndex
     */
    void changeHighlight(@NotNull IHighlight highlight, long startByteIndex, long endByteIndex);

    /**
     * @return the number of highlights, excluding the selection highlight.
     */
    int getHighlightsCount();

    /**
     * @return <code>true</code> if at least one highlight is available otherwise <code>false</code>.
     */
    boolean hasHighlights();

    /**
     * @return all highlights, excluding the selection highlight.
     */
    @NotNull
    List<IHighlight> getHighlights();

    /**
     * Highlight painter.
     * <p/>
     * A highlight painter can be used to customize the drawing of highlights e.g. special colors or only draw the outline.
     */
    interface IHighlightPainter
    {
        /**
         * Renders the highlight.
         *
         * @param g                     the Graphics2D context in which to paint.
         * @param area                  the area in which the highlight should be painted.
         * @param rowElementsHDimension the horizontal dimension which covers all chars/bytes of a single row.
         *                              Used to know the min and max possible horizontal position for a highlight.
         * @param byteStartIndex        the starting offset in the configuration &gt;= 0
         * @param byteEndIndex          the ending offset in the configuration &gt;= byteStartIndex
         */
        void paint(@NotNull Graphics2D g, @NotNull ByteArea area,
                   @NotNull final HDimension rowElementsHDimension, long byteStartIndex, long byteEndIndex);

    }

    /**
     * A range of bytes which defines a highlight.
     * Each highlight can specify a painter which is used to draw the highlight.
     */
    interface IHighlight
    {
        /**
         * Gets the starting configuration offset for the highlight.
         *
         * @return the starting offset &gt;= 0
         */
        long getStartOffset();

        /**
         * Gets the ending configuration offset for the highlight.
         *
         * @return the ending offset &gt;= 0
         */
        long getEndOffset();

        /**
         * Gets the painter for the highlighter.
         *
         * @return the painter.
         */
        @Nullable
        IHighlightPainter getPainter();
    }
}
