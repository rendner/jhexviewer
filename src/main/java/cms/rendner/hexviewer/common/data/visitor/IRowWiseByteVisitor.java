package cms.rendner.hexviewer.common.data.visitor;

import cms.rendner.hexviewer.common.data.wrapper.RowData;
import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor for doing operations on row of bytes level (searching, printing, etc.).
 * Allows to keep track of the row for the current visited byte.
 * <p/>
 * An implementation of this interface should be used by a {@link cms.rendner.hexviewer.common.data.walker.RowWiseByteWalker}
 * to iterate over a range of bytes displayed in the {@link JHexViewer}.
 *
 * @author rendner
 */
public interface IRowWiseByteVisitor
{
    /**
     * Notifies the visitor to initialize the required setup.
     * This method is called before the first time <code>visitRow</code> is called.
     */
    void start();

    /**
     * Is called for every row of bytes to visit.
     *
     * @param rowData               data of the row to visit.
     * @param leadingBytesToIgnore  number of leading bytes to exclude from the row data.
     * @param trailingBytesToIgnore number of trailing bytes to exclude from the row data.
     */
    void visitRow(@NotNull RowData rowData, int leadingBytesToIgnore, int trailingBytesToIgnore);

    /**
     * Notifies the visitor that all rows are visited.
     * This method is called after the last time <code>visitRow</code> was called.
     */
    void end();
}
