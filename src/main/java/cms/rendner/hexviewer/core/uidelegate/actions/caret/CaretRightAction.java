package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class CaretRightAction extends AbstractHexViewerAction
{
    private final boolean select;

    public CaretRightAction(final boolean select)
    {
        super();
        this.select = select;
    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {
        final JHexViewer hexViewer = getHexViewer(event);
        if (hexViewer != null)
        {
            hexViewer.getCaret().ifPresent(caret ->
            {
                final int newDotIndex = caret.getDot().getIndex() + 1;

                if (select)
                {
                    // special case: focused area isn't fully visible inside the visible rectangle
                    //
                    // A caret can't be placed after the last byte of a row, the caret is automatically adjusted in front of the first byte of the next row.
                    // When this action selects the last byte of a row and the width of the window is smaller than the width of the active area, the visible
                    // part in the window shouldn't jump to the first byte of the next row. Otherwise
                    // the user would be confused about the jumping content.
                    //
                    final int caretIndexInRow = hexViewer.byteIndexToIndexInRow(newDotIndex);
                    caret.moveDot(newDotIndex, caretIndexInRow == 0 ? IndexPosition.Bias.Backward : IndexPosition.Bias.Forward);
                }
                else
                {
                    caret.setDot(newDotIndex);
                }
            });
        }
    }
}
