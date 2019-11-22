package cms.rendner.hexviewer.view.components.caret;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A caret has a position referred to as a dot.
 * The dot is where the caret is currently located in the data model of the {@link JHexViewer}. There is a second position
 * maintained by the caret that represents the other end of a selection called mark. If there is no selection the dot and
 * mark will point to the same position. If a selection exists, the two values will be different.
 *
 * @author rendner
 */
public interface ICaret
{
    /**
     * Installs the caret to the {@link JHexViewer}.
     *
     * @param hexViewer the component to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the caret from the {@link JHexViewer}.
     *
     * @param hexViewer the component to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Renders the caret.
     *
     * @param g    the Graphics2D context of the area in which the caret should be painted.
     * @param area the area in which the caret should be painted.
     */
    void paint(@NotNull Graphics2D g, @NotNull ByteArea area);

    /**
     * Adds a listener for receiving notifications about caret movement.
     *
     * @param listener the listener instance to add.
     */
    void addCaretListener(@NotNull ICaretListener listener);

    /**
     * Removes a listener for receiving notifications about caret movement.
     *
     * @param listener the listener instance to remove.
     */
    void removeCaretListener(@NotNull ICaretListener listener);

    /**
     * Moves the caret relative, starting from the current position.
     *
     * @param offsetShift   number of bytes to move, can be negative to move the caret to the left.
     * @param withSelection if <code>true</code> the selection will be created/expanded, otherwise an existing selection will be cleared.
     * @param scrollToCaret indicates if the visible region should be adjusted to always have the caret inside the visible
     *                      region.
     */
    void moveCaretRelatively(long offsetShift, boolean withSelection, boolean scrollToCaret);

    /**
     * Moves caret absolute.
     *
     * @param offset        the new position of the caret.
     * @param withSelection if <code>true</code> the selection will be created/expanded, otherwise an existing selection will be cleared.
     * @param scrollToCaret indicates if the visible region should be adjusted to always have the caret inside the visible
     *                      region.
     */
    void moveCaret(long offset, boolean withSelection, boolean scrollToCaret);

    /**
     * Returns the current position of the caret.
     *
     * @return the position of the dot.
     */
    long getDot();

    /**
     * Returns the current position of the mark.
     *
     * @return the position of the mark.
     */
    long getMark();

    /**
     * @return <code>true</code> if one or more bytes are selected.
     */
    boolean hasSelection();

    /**
     * @return the index of the first selected byte.
     */
    long getSelectionStart();

    /**
     * @return the index of the last selected byte.
     */
    long getSelectionEnd();
}
