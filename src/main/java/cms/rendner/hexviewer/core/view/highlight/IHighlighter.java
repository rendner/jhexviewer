package cms.rendner.hexviewer.core.view.highlight;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.geom.HorizontalDimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;


/**
 * A highlighter adds support for highlighting bytes in the JHexViewer.
 * <p/>
 * The highlights and the painting of the highlights are maintained by the highlighter.
 * Also the selected bytes in the JHexViewer are a selection and therefore managed by the highlighter.
 *
 * @author rendner
 */
public interface IHighlighter
{
    /**
     * Installs the highlighter to the JHexViewer.
     * <p/>
     * Called when the UI is being installed into the
     * JHexViewer. This can be used to gain access to the configuration that is being navigated
     * by the implementation of this interface.
     *
     * @param hexViewer the hex viewer component to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the highlighter from the JHexViewer.
     * <p/>
     * Called when the UI is being removed from the
     * JHexViewer. This is used to uninstall any listeners that were attached.
     *
     * @param hexViewer the hex viewer component to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Sets the color which is used to paint highlights if they don't have a custom painter assigned.
     *
     * @param newColor the new default color.
     */
    void setDefaultColor(@NotNull Color newColor);

    /**
     * Renders the highlights.
     *
     * @param g            the Graphics context in which to paint.
     * @param byteRowsView
     */
    void paint(@NotNull Graphics g, @NotNull ByteRowsView byteRowsView);

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
    IHighlight addHighlight(int startByteIndex, int endByteIndex, @NotNull IHighlightPainter painter);

    /**
     * Adds a highlight to the view.
     * Returns the created highlight that can be used to refer to the highlight.
     *
     * @param startByteIndex the beginning of the range &gt;= 0
     * @param endByteIndex   the end of the range &gt;= startByteIndex
     * @return an object that refers to the added highlight
     */
    @NotNull
    IHighlight addHighlight(int startByteIndex, int endByteIndex);

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
    IHighlight addHighlight(int startByteIndex, int endByteIndex, @NotNull Color color);

    /**
     * Sets the highlight which is used to paint the selection of the view.
     * Returns the created highlight that can be used to refer to the highlight.
     *
     * @param startByteIndex the beginning of the range &gt;= 0
     * @param endByteIndex   the end of the range &gt;= startByteIndex
     * @param painter        the painter to use for the actual highlighting
     * @return an object that refers to the highlight
     */
    @NotNull
    IHighlight setSelectionHighlight(int startByteIndex, int endByteIndex, @NotNull IHighlightPainter painter);

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
    void changeHighlight(@NotNull IHighlight highlight, int startByteIndex, int endByteIndex);

    /**
     * @return the number of highlights, excluding the selection highlight.
     */
    int getHighlightsCount();

    /**
     * @return all highlights, excluding the selection highlight.
     */
    @NotNull
    List<IHighlight> getHighlights();

    /**
     * @return the state of the "paint selection behind highlight"-flag.
     */
    boolean getPaintSelectionBehindHighlights();

    /**
     * Sets the "paint selection behind highlight"-flag.
     * If set to <code>true</code> the highlights are painted on top of a selection, otherwise behind the selection.
     *
     * @param value the new value.
     */
    void setPaintSelectionBehindHighlights(boolean value);

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
         * @param g                    the Graphics context in which to paint.
         * @param hexViewer            the hex viewer.
         * @param rowsView             the view in which the highlight should be painted.
         * @param rowElementsDimension the horizontal dimension which covers all chars/bytes of a single row.
         *                             Used to know the min and max possible horizontal position for a highlight.
         * @param byteStartIndex       the starting offset in the configuration &gt;= 0
         * @param byteEndIndex         the ending offset in the configuration &gt;= byteStartIndex
         */
        void paint(@NotNull Graphics g, @NotNull JHexViewer hexViewer,@NotNull  ByteRowsView rowsView,
                   @NotNull final HorizontalDimension rowElementsDimension, int byteStartIndex, int byteEndIndex);

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
        int getStartOffset();

        /**
         * Gets the ending configuration offset for the highlight.
         *
         * @return the ending offset &gt;= 0
         */
        int getEndOffset();

        /**
         * Gets the painter for the highlighter.
         *
         * @return the painter.
         */
        @Nullable
        IHighlightPainter getPainter();
    }
}
