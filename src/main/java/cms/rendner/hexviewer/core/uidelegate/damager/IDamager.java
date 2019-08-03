package cms.rendner.hexviewer.core.uidelegate.damager;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import org.jetbrains.annotations.NotNull;

/**
 * A damager can be used to mark parts of the hex viewer for a repaint.
 * This would usually the case if content has changed and should be repainted to present the
 * actual state.
 *
 * @author rendner
 */
public interface IDamager
{
    /**
     * Installs the damager to the hex viewer which should be damaged.
     *
     * @param hexViewer the hex viewer to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the damager from the hex viewer.
     *
     * @param hexViewer the hex viewer to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Damages a row in all areas ({@link AreaId#OFFSET}, {@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @param rowIndex the index of the row which should be damaged.
     */
    void damageRow(int rowIndex);

    /**
     * Damages a row from an area.
     *
     * @param id       the id of the area.
     * @param rowIndex the index of the row which should be damaged.
     */
    void damageRow(@NotNull AreaId id, int rowIndex);

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
     * Damages a byte in all byte-areas ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @param byteIndex the index of the byte which should be damaged.
     */
    void damageByte(int byteIndex);

    /**
     * Damages a range of bytes in all byte-areas ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @param byteStartIndex the start of the byte range which should be damaged.
     * @param byteEndIndex   the end of the byte range which should be damaged.
     */
    void damageBytes(int byteStartIndex, int byteEndIndex);

    /**
     * Damages a range of bytes from a byte-area ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @param id             the id of the area.
     * @param byteStartIndex the start of the byte range which should be damaged.
     * @param byteEndIndex   the end of the byte range which should be damaged.
     */
    void damageBytes(@NotNull AreaId id, int byteStartIndex, int byteEndIndex);

    /**
     * Damages an area.
     *
     * @param id the id of the area.
     */
    void damageArea(@NotNull AreaId id);

    /**
     * Damages all areas.
     */
    void damageAllAreas();

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
