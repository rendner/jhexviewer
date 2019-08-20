package cms.rendner.hexviewer.core.view.color;

import cms.rendner.hexviewer.core.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Provides utility methods for custom color provider implementations.
 *
 * @author rendner
 */
public abstract class AbstractRowColorProvider implements IRowColorProvider
{
    /**
     * Utility to check if the caret is in a specific row.
     * Can be used to implementing alternating rows.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @param rowIndex index to check.
     * @return <code>true</code> if caret is in the checked row otherwise <code>false</code>
     */
    protected boolean isCaretRowIndex(@NotNull final JHexViewer hexViewer, final int rowIndex)
    {
        return hexViewer.getCaret()
                .map(caret -> rowIndex == hexViewer.byteIndexToRowIndex(caret.getDot().getIndex()))
                .orElse(Boolean.FALSE);
    }
}
