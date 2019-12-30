package cms.rendner.hexviewer.view.components.damager;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the damager interface.
 *
 * @author rendner
 */
public class DefaultDamager implements IDamager
{
    /**
     * List of areas which should be damaged by this instance.
     */
    private List<ByteArea> areas;
    /**
     * The {@link JHexViewer} component to which this instance was installed.
     */
    private JHexViewer hexViewer;

    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
        areas = Arrays.asList(hexViewer.getHexArea(), hexViewer.getTextArea());
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        areas = null;
        this.hexViewer = null;
    }

    @Override
    public void damageRow(final int rowIndex)
    {
        areas.forEach(area -> area.damageRow(rowIndex));
    }

    @Override
    public void damageByte(final long byteIndex)
    {
        areas.forEach(area -> damage(area, area.getByteRect(byteIndex)));
    }

    @Override
    public void damageBytes(final long byteStartIndex, final long byteEndIndex)
    {
        final int startRowIndex = hexViewer.byteIndexToRowIndex(byteStartIndex);
        final int endRowIndex = hexViewer.byteIndexToRowIndex(byteEndIndex);
        if (startRowIndex == endRowIndex)
        {
            areas.forEach(area -> damage(area, area.getRowRect(startRowIndex)));
        }
        else
        {
            areas.forEach(area -> damage(area, area.getRowRect(startRowIndex).union(area.getRowRect(endRowIndex))));
        }
    }

    @Override
    public void damageChangedHighlight(final long oldStart, final long oldEnd, final long newStart, final long newEnd)
    {
        if (oldStart == newStart && oldEnd == newEnd)
        {
            // nothing has changed
            return;
        }

        if (oldStart == newStart)
        {
            // the end was modified
            final long start = Math.min(oldEnd, newEnd);
            long end = Math.max(oldEnd, newEnd);

            // there can be spaces between the bytes, these spaces should also be cleared/repainted by a highlight
            // for this reason add one byte to also clear the space after the end byte
            final int rowIndexOfEnd = hexViewer.byteIndexToRowIndex(end);
            final int rowIndexOfByteAfterEnd = hexViewer.byteIndexToRowIndex(end + 1);
            if (rowIndexOfEnd == rowIndexOfByteAfterEnd)
            {
                end++;
            }

            damageBytes(start, end);
        }
        else if (oldEnd == newEnd)
        {
            // the start was modified
            long start = Math.min(oldStart, newStart);
            final long end = Math.max(oldStart, newStart);

            // there can be spaces between the bytes, these spaces should also be cleared/repainted by a highlight
            // for this reason subtract one byte to also clear the space before the start byte
            final int rowIndexOfStart = hexViewer.byteIndexToRowIndex(start);
            final int rowIndexOfByteBeforeStart = hexViewer.byteIndexToRowIndex(start - 1);
            if (rowIndexOfStart == rowIndexOfByteBeforeStart)
            {
                start--;
            }

            damageBytes(start, end);
        }
        else
        {
            // maybe start and end was modified or we have no intersection -> create an union
            final long start = Math.min(oldStart, newStart);
            final long end = Math.max(oldEnd, newEnd);
            damageBytes(start, end);
        }
    }

    @Override
    public void damageCaret(final long oldCaretIndex, final long newCaretIndex)
    {
        damageCaret(oldCaretIndex);

        if (oldCaretIndex != newCaretIndex)
        {
            damageCaret(newCaretIndex);
        }
    }

    @Override
    public void damageAll()
    {
        areas.forEach(Component::repaint);
    }

    /**
     * Damages the position of the caret.
     *
     * @param caretIndex the position of the caret.
     */
    private void damageCaret(final long caretIndex)
    {
        areas.forEach(area -> damage(area, area.getCaretRect(caretIndex)));
    }

    /**
     * Damages a region of the area.
     *
     * @param dirtyRegion the region which should be damaged.
     */
    private void damage(@NotNull final ByteArea area, @NotNull final Rectangle dirtyRegion)
    {
        if (!dirtyRegion.isEmpty())
        {
            area.repaint(dirtyRegion);
        }
    }
}
