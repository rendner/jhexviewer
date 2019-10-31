package cms.rendner.hexviewer.view.components.damager;

import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * A damager can be used to mark parts of the byte areas for repaint.
 * This would usually the case if content has changed and should be repainted to present the
 * actual state.
 *
 * @author rendner
 */
public interface IDamager
{
    /**
     * Installs the damager to the {@link JHexViewer}.
     *
     * @param hexViewer the component to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the damager from the {@link JHexViewer}.
     *
     * @param hexViewer the component to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Damages a row of the area.
     *
     * @param rowIndex the index of the row which should be damaged.
     */
    void damageRow(int rowIndex);

    /**
     * Damages the position of the caret in all parts where the caret is displayed.
     * This method expects the old and new position, this values can be equal if the caret
     * is blinking.
     *
     * @param oldCaretIndex the position of the caret before a change.
     * @param newCaretIndex the position of the caret after a change.
     */
    void damageCaret(int oldCaretIndex, int newCaretIndex);

    /**
     * Damages a byte of the area.
     *
     * @param byteIndex the index of the byte which should be damaged.
     */
    void damageByte(int byteIndex);

    /**
     * Damages a range of bytes of the area.
     *
     * @param byteStartIndex the start of the byte range which should be damaged.
     * @param byteEndIndex   the end of the byte range which should be damaged.
     */
    void damageBytes(int byteStartIndex, int byteEndIndex);

    /**
     * Damages the whole region of the area.
     */
    void damageAll();

    /**
     * Damages the changed part of a highlight.
     *
     * @param oldStart the old start of the highlight.
     * @param oldEnd   the old end of the highlight.
     * @param newStart the new start of the highlight.
     * @param newEnd   the new end of the highlight.
     */
    void damageChangedHighlight(int oldStart, int oldEnd, int newStart, int newEnd);
}
