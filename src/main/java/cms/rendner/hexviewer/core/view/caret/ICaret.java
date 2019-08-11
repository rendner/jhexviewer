package cms.rendner.hexviewer.core.view.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.color.ICaretColorProvider;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import cms.rendner.hexviewer.utils.observer.IObservable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A place within a byte view that represents where a selection can be started. A caret has a position referred to as a dot.
 * The dot is where the caret is currently located in the IDataModel rendered by the JHexViewer. There is a second position
 * maintained by the caret that represents the other end of a selection called mark. If there is no selection the dot
 * and mark will point to the same position. If a selection exists, the two values will be different.
 * <p/>
 * The dot can be placed by either calling setDot or moveDot. Setting the dot has the effect of removing any selection
 * that may have previously existed. The dot and mark will point to the same position. Moving the dot has the effect of
 * creating a selection as the mark is left at whatever position it previously had.
 *
 * @author rendner
 * @see JHexViewer#lastPossibleCaretIndex()
 * @see cms.rendner.hexviewer.core.model.data.IDataModel
 */
public interface ICaret extends IObservable<Void>
{
    /**
     * Installs the caret to the JHexViewer.
     * <p/>
     * Automatically called when the caret is being installed into the JHexViewer component.
     * This can be used to gain access to properties of the JHexViewer component.
     *
     * @param hexViewer the JHexViewer component to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the caret from the JHexViewer.
     * <p/>
     * Automatically called when the caret is being uninstalled from the JHexViewer component.
     * This can be used to unregister any listeners that were attached.
     *
     * @param hexViewer the JHexViewer component to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Automatically called to render the caret.
     *
     * @param g        the graphics context to draw to.
     * @param rowsView the view to draw into.
     */
    void paint(@NotNull Graphics g, @NotNull ByteRowsView rowsView);

    /**
     * Returns the current position of the caret.
     *
     * @return the position of the dot.
     */
    @NotNull
    IndexPosition getDot();

    /**
     * Sets the caret (dot + mark) to the specified index, with a forward bias. This implicitly sets the
     * selection range to zero.
     * <p/>
     * Note: The index and the bias will be adjusted to always point to a valid position. Therefore the forward bias may
     * become a backward bias.
     *
     * @param index the new index to set the caret to. If the index is negative or beyond the last possible caret index,
     *              the caret is placed at the beginning or at the end, respectively.
     * @see JHexViewer#lastPossibleCaretIndex()
     */
    void setDot(int index);

    /**
     * Sets the caret (dot + mark) to the specified position. This implicitly sets the
     * selection range to zero.
     *
     * @param index the new index to set the caret to. If the index is negative or beyond the last possible caret index,
     *              the caret is placed at the beginning or at the end, respectively.
     * @param bias  can be used to indicate an interest toward one of the two sides of the position in boundary
     *              conditions when the index is ambiguous. The bias will be adjusted if the new index doesn't allow the
     *              specified value.
     * @see JHexViewer#lastPossibleCaretIndex()
     */
    void setDot(int index, @NotNull IndexPosition.Bias bias);

    /**
     * Sets the caret (dot + mark) to the specified position. This implicitly sets the
     * selection range to zero.
     *
     * @param position the new position to set the caret to. If the index of the position is beyond the last possible
     *                 caret index, the caret is placed at the end. The bias of the position will be adjusted if the
     *                 new position doesn't allow the bias.
     */
    void setDot(@NotNull IndexPosition position);

    /**
     * Moves the caret (dot) to some other position with a forward bias, leaving behind the mark.
     * This implicitly modifies the selection range.
     * <p/>
     * Note: The index and the bias will be adjusted to always point to a valid position. Therefore the forward bias may
     * become a backward bias.
     *
     * @param index the new index to move the caret to. If negative or beyond the last possible caret index, the caret
     *              is placed at the beginning or at the end, respectively.
     * @see JHexViewer#lastPossibleCaretIndex()
     */
    void moveDot(int index);

    /**
     * Moves the caret (dot) to some other position with a forward bias, leaving behind the mark.
     * This implicitly modifies the selection range.
     *
     * @param index the new index to move the caret to. If negative or beyond the last possible caret index, the caret
     *              is placed at the beginning or at the end, respectively.
     * @param bias  can be used to indicate an interest toward one of the two sides of the position in boundary
     *              conditions when the index is ambiguous. The bias will be adjusted if the new index doesn't allow the
     *              specified value.
     * @see JHexViewer#lastPossibleCaretIndex()
     */
    void moveDot(int index, @NotNull IndexPosition.Bias bias);

    /**
     * Moves the caret (dot) to some other position, leaving behind the mark.
     * This implicitly modifies the selection range.
     *
     * @param position the new position to move the caret to. If the index of the position is beyond the last possible
     *                 caret index, the caret is placed at the end. The bias of the position will be adjusted if the
     *                 new position doesn't allow the bias.
     */
    void moveDot(@NotNull IndexPosition position);


    /**
     * Returns the current position of the mark. If there is a selection,
     * the dot and mark will point to different positions.
     *
     * @return the position of the mark.
     */
    @NotNull
    IndexPosition getMark();

    /**
     * Sets a color provider.
     * Setting a provider results in a repaint of the caret/selection.
     * <p/>
     * The provider is used during the paint process to determine which color should be used to render the caret
     * or the selection.
     *
     * @param colorProvider the new color provider.
     */
    void setColorProvider(@NotNull ICaretColorProvider colorProvider);

    /**
     * @return the current color provider.
     */
    @NotNull
    ICaretColorProvider getColorProvider();

    /**
     * @return painter used to render the selection.
     */
    @NotNull
    IHighlighter.IHighlightPainter getSelectionPainter();

    /**
     * Sets the new selection painter.
     * <p/>
     * If possible the selection painter should respect the colors provided by the current installed ICaretColorProvider.
     *
     * @param selectionPainter the painter to render the caret/selection.
     */
    void setSelectionPainter(@NotNull IHighlighter.IHighlightPainter selectionPainter);

    /**
     * Gets the caret blink rate.
     *
     * @return the delay in milliseconds. If this is
     * zero the caret will not blink.
     */
    int getBlinkRate();

    /**
     * Sets the caret blink rate.
     *
     * @param rate the rate in milliseconds, <code>0</code> to stop blinking.
     */
    void setBlinkRate(int rate);

    /**
     * @return <code>true</code> if no bytes are selected.
     */
    boolean isSelectionEmpty();

    /**
     * @return <code>true</code> if one or more bytes are selected.
     */
    boolean hasSelection();

    /**
     * @return the index of the first selected byte.
     */
    int getSelectionStart();

    /**
     * @return the index of the last selected byte.
     */
    int getSelectionEnd();

    /**
     * Sets the internal mark equals the dot, this sets the number of selected bytes to zero.
     */
    void clearSelection();

    /**
     * Sets a selection and moves the dot to the endIndex.
     *
     * @param startIndex the position of the mark.
     * @param endIndex   the position of the dot.
     */
    void setSelection(int startIndex, int endIndex);
}
