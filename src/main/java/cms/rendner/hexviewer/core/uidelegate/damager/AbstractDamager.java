package cms.rendner.hexviewer.core.uidelegate.damager;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.areas.RowBasedView;
import cms.rendner.hexviewer.utils.RectangleUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Basis implementation of the damager interface.
 *
 * @author rendner
 */
public abstract class AbstractDamager implements IDamager
{
    /**
     * A return value which is used to retrieve the bounds for a row, byte or caret.
     * This instance be reused to minimize creation of new rectangles.
     */
    private final Rectangle rvRect = new Rectangle();

    /**
     * A return value which is used to retrieve the bounds for a row, byte or caret.
     * This instance be reused to minimize creation of new rectangles.
     */
    private final Rectangle rvRect2 = new Rectangle();

    /**
     * The hex viewer which should be damaged.
     */
    protected JHexViewer hexViewer;

    @Override
    public void install(final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
    }

    @Override
    public void uninstall(final JHexViewer hexViewer)
    {
        this.hexViewer = null;
    }

    @Override
    public void damageRow(final int rowIndex)
    {
        damageRow(AreaId.OFFSET, rowIndex);
        damageRow(AreaId.HEX, rowIndex);
        damageRow(AreaId.ASCII, rowIndex);
    }

    @Override
    public void damageByte(final int byteIndex)
    {
        damageByte(AreaId.HEX, byteIndex);
        damageByte(AreaId.ASCII, byteIndex);
    }

    @Override
    public void damageBytes(final int byteStartIndex, final int byteEndIndex)
    {
        damageBytes(AreaId.HEX, byteStartIndex, byteEndIndex);
        damageBytes(AreaId.ASCII, byteStartIndex, byteEndIndex);
    }

    @Override
    public void damageChangedHighlight(final int oldStart, final int oldEnd, final int newStart, final int newEnd)
    {
        damageChangedHighlight(AreaId.HEX, oldStart, oldEnd, newStart, newEnd);
        damageChangedHighlight(AreaId.ASCII, oldStart, oldEnd, newStart, newEnd);
    }

    @Override
    public void damageCaret(final int oldCaretIndex, final int newCaretIndex)
    {
        damageCaret(AreaId.HEX, oldCaretIndex);
        damageCaret(AreaId.ASCII, oldCaretIndex);

        if (oldCaretIndex != newCaretIndex)
        {
            damageCaret(AreaId.HEX, newCaretIndex);
            damageCaret(AreaId.ASCII, newCaretIndex);
        }
    }

    @Override
    public void damageRow(final AreaId id, final int rowIndex)
    {
        final RowBasedView rowsView = hexViewer.getRowsView(id);
        damage(rowsView, rowsView.getRowRect(rowIndex, rvRect));
    }

    @Override
    public void damageBytes(final AreaId id, final int startByteIndex, final int endByteIndex)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        final int startRowIndex = hexViewer.byteIndexToRowIndex(startByteIndex);
        final int endRowIndex = hexViewer.byteIndexToRowIndex(endByteIndex);

        if (startRowIndex == endRowIndex)
        {
            damage(rowsView, RectangleUtils.computeUnion(
                    rowsView.getByteRect(startByteIndex, rvRect),
                    rowsView.getByteRect(endByteIndex, rvRect2)));
        }
        else
        {
            damage(rowsView, RectangleUtils.computeUnion(
                    rowsView.getRowRect(startRowIndex, rvRect),
                    rowsView.getRowRect(endRowIndex, rvRect2)));
        }
    }

    @Override
    public void damageArea(final AreaId id)
    {
        hexViewer.getRowsView(id).repaint();
    }

    @Override
    public void damageAllAreas()
    {
        damageArea(AreaId.OFFSET);
        damageArea(AreaId.HEX);
        damageArea(AreaId.ASCII);
    }

    protected void damageByte(final AreaId id, final int byteIndex)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        damage(rowsView, rowsView.getByteRect(byteIndex, rvRect));
    }

    /**
     * Damages the changed part of a highlight from a byte-area.
     *
     * @param id       the id of the area.
     * @param oldStart the old start of the highlight.
     * @param oldEnd   the old end of the highlight.
     * @param newStart the new start of the highlight.
     * @param newEnd   the new end of the highlight.
     */
    protected void damageChangedHighlight(final AreaId id, final int oldStart, final int oldEnd, final int newStart, final int newEnd)
    {
        if (oldStart == newStart && oldEnd == newEnd)
        {
            // nothing has changed
            return;
        }

        if (oldStart == newStart)
        {
            // the end was modified
            final int start = Math.min(oldEnd, newEnd);
            int end = Math.max(oldEnd, newEnd);

            // there can be spaces between the bytes, these spaces should also be cleared/repainted by a highlight
            // for this reason add one byte to also clear the space after the end byte
            final int rowIndexOfEnd = hexViewer.byteIndexToRowIndex(end);
            final int rowIndexOfByteAfterEnd = hexViewer.byteIndexToRowIndex(end + 1);
            if (rowIndexOfEnd == rowIndexOfByteAfterEnd)
            {
                end++;
            }

            damageBytes(id, start, end);
        }
        else if (oldEnd == newEnd)
        {
            // the start was modified
            int start = Math.min(oldStart, newStart);
            final int end = Math.max(oldStart, newStart);

            // there can be spaces between the bytes, these spaces should also be cleared/repainted by a highlight
            // for this reason subtract one byte to also clear the space before the start byte
            final int rowIndexOfStart = hexViewer.byteIndexToRowIndex(start);
            final int rowIndexOfByteBeforeStart = hexViewer.byteIndexToRowIndex(start - 1);
            if (rowIndexOfStart == rowIndexOfByteBeforeStart)
            {
                start--;
            }

            damageBytes(id, start, end);
        }
        else
        {
            // maybe start and end was modified or we have no intersection -> create an union
            final int start = Math.min(oldStart, newStart);
            final int end = Math.max(oldEnd, newEnd);
            damageBytes(id, start, end);
        }
    }

    /**
     * Damages the position of the caret from a byte-area.
     *
     * @param id         the id of the area.
     * @param caretIndex the position of the caret.
     */
    protected void damageCaret(final AreaId id, final int caretIndex)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        damage(rowsView, rowsView.getCaretRect(caretIndex, rvRect));
    }

    /**
     * Damages a region in a component.
     *
     * @param target      the component which should be damaged.
     * @param dirtyRegion the region which should be damaged.
     */
    protected void damage(final JComponent target, final Rectangle dirtyRegion)
    {
        if (!dirtyRegion.isEmpty())
        {
            target.repaint(dirtyRegion);
        }
    }
}
