package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.geom.Range;
import cms.rendner.hexviewer.swing.scrollable.ScrollDirection;
import cms.rendner.hexviewer.swing.scrollable.ScrollableContainer;
import cms.rendner.hexviewer.utils.IndexUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class VerticalPageAction extends AbstractHexViewerAction
{
    private final int direction;
    private final boolean select;

    public VerticalPageAction(final int direction, final boolean select)
    {
        super();
        this.direction = direction;
        this.select = select;
    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {
        final JHexViewer hexViewer = getHexViewer(event);
        if (hexViewer != null)
        {
            hexViewer.getCaret().ifPresent(
                    caret -> {
                        final AreaId id = hexViewer.getFocusedArea();
                        final ScrollableContainer scrollableByteRowsContainer = hexViewer.getScrollableByteRowsContainer();
                        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);

                        final Rectangle visibleRect = scrollableByteRowsContainer.getVisibleRect();
                        final Rectangle newVisibleRect = getNewVisibleRect(scrollableByteRowsContainer, visibleRect);
                        final int newCaretPosition = getNewCaretIndex(hexViewer, rowsView, visibleRect, newVisibleRect);

                        snapToRow(rowsView, newVisibleRect, newCaretPosition);

                        // setting the new caret position results in scrolling automatically to the new caret position
                        if (select)
                        {
                            caret.moveDot(newCaretPosition);
                        }
                        else
                        {
                            caret.setDot(newCaretPosition);
                        }

                        // scroll to newVisibleRect to overwrite the automatic scrolling of the caret
                        scrollableByteRowsContainer.scrollRectToVisible(newVisibleRect);
            });
        }
    }

    private void snapToRow(final ByteRowsView rowsView, final Rectangle newVisibleRect, final int newCaretPosition)
    {
        final int caretRowIndex = IndexUtils.byteIndexToRowIndex(newCaretPosition, rowsView.bytesPerRow());
        final int trailingRowIndex = rowsView.verticalLocationToRowIndex(newVisibleRect.y + newVisibleRect.height - 1);

        if (trailingRowIndex == caretRowIndex)
        {
            // show trailing row completely
            final Rectangle trailingRowRect = rowsView.getRowRect(trailingRowIndex);
            final int offset = newVisibleRect.y + newVisibleRect.height - trailingRowRect.y;
            if (offset != trailingRowRect.height)
            {
                newVisibleRect.y += trailingRowRect.height - offset;
            }
        }
        else
        {
            // show leading row completely
            final int leadingRowIndex = rowsView.verticalLocationToRowIndex(newVisibleRect.y);
            final Rectangle leadingRowRect = rowsView.getRowRect(leadingRowIndex);
            newVisibleRect.y = leadingRowRect.y;
        }
    }

    private int getNewCaretIndex(final JHexViewer hexViewer, final ByteRowsView rowsView, final Rectangle visibleRect, final Rectangle newVisibleRect)
    {
        return hexViewer.getCaret().map(caret -> {
            final int caretIndex = caret.getDot();
            final int rowIndexOfCaret = hexViewer.byteIndexToRowIndex(caretIndex);
            final int caretIndexInRow = hexViewer.byteIndexToIndexInRow(caretIndex);

            final Range visibleRows = rowsView.getRowRange(visibleRect);

            if (visibleRows.isValid() && visibleRows.contains(rowIndexOfCaret))
            {
                final int rowDelta = (rowIndexOfCaret - visibleRows.getStart());
                final int caretOffset = (rowDelta * rowsView.bytesPerRow()) + caretIndexInRow;
                final int rowIndexOfNewLeadingRow = rowsView.verticalLocationToRowIndex(newVisibleRect.y);
                final int byteIndexOfNewLeadingRow = hexViewer.rowIndexToByteIndex(rowIndexOfNewLeadingRow);
                return Math.min(hexViewer.lastPossibleCaretIndex(), Math.max(0, byteIndexOfNewLeadingRow + caretOffset));
            }
            else
            {
                // caret isn't visible
                if (ScrollDirection.UP == direction)
                {
                    // place in first row
                    final int rowIndexOfNewLeadingRow = rowsView.verticalLocationToRowIndex(newVisibleRect.y);
                    final int byteIndexOfNewLeadingRow = hexViewer.rowIndexToByteIndex(rowIndexOfNewLeadingRow);
                    return Math.min(hexViewer.lastPossibleCaretIndex(), Math.max(0, byteIndexOfNewLeadingRow + caretIndexInRow));
                }
                else
                {
                    // place in last row
                    final int rowIndexOfNewTrailingRow = rowsView.verticalLocationToRowIndex(newVisibleRect.y + newVisibleRect.height - 1);
                    final int byteIndexOfNewTrailingRow = hexViewer.rowIndexToByteIndex(rowIndexOfNewTrailingRow);
                    return Math.min(hexViewer.lastPossibleCaretIndex(), Math.max(0, byteIndexOfNewTrailingRow + caretIndexInRow));
                }
            }
        }).get();
    }

    private Rectangle getNewVisibleRect(final ScrollableContainer scrollableByteRowsContainer, final Rectangle visibleRect)
    {
        final int blockIncrement = scrollableByteRowsContainer.getScrollableBlockIncrement(visibleRect, SwingConstants.VERTICAL, direction);
        final int scrollAmount = direction * blockIncrement;

        final Rectangle result = new Rectangle(visibleRect);
        result.y += scrollAmount;

        if (result.y < 0)
        {
            result.y = 0;
        }
        else
        {
            if (result.y + result.height > scrollableByteRowsContainer.getHeight())
            {
                result.y = Math.max(0, scrollableByteRowsContainer.getHeight() - result.height);
            }
        }

        return result;
    }
}
