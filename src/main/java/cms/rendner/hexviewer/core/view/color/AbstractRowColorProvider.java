package cms.rendner.hexviewer.core.view.color;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.caret.ICaret;

/**
 * Provides utility methods for custom color provider implementations.
 *
 * @author rendner
 */
public abstract class AbstractRowColorProvider implements IRowColorProvider
{
    /**
     * Utility to check if an index is odd or not.
     * Can be used to implementing alternating rows.
     *
     * @param index index to check.
     * @return <code>true</code> if index is odd otherwise <code>false</code>
     */
    protected boolean isOdd(final int index)
    {
        return (index & 1) != 0;
    }

    /**
     * Utility to check if an index is even or not.
     * Can be used to implementing alternating rows.
     *
     * @param index index to check.
     * @return <code>true</code> if index is even otherwise <code>false</code>
     */
    protected boolean isEven(final int index)
    {
        return (index & 1) == 0;
    }

    /**
     * Utility to check if the caret is in a specific row.
     * Can be used to implementing alternating rows.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @param rowIndex index to check.
     * @return <code>true</code> if caret is in the checked row otherwise <code>false</code>
     */
    protected boolean isCaretRowIndex(final JHexViewer hexViewer, final int rowIndex)
    {
        final ICaret caret = hexViewer.getCaret();
        return caret != null && rowIndex == hexViewer.byteIndexToRowIndex(caret.getDot());
    }
}
