package cms.rendner.hexviewer.support.data.visitor;

import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import org.jetbrains.annotations.NotNull;

/**
 * Visitor for doing operations on row of bytes level (searching, printing, etc.).
 * Allows to keep track of the row for the current visited byte.
 * <p/>
 * An implementation of this interface should be used by a {@link cms.rendner.hexviewer.support.data.walker.RowWiseByteWalker}
 * to iterate over a range of bytes displayed in the {@link cms.rendner.hexviewer.core.JHexViewer}.
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
     * @param rowData data of the row to visit.
     */
    void visitRow(@NotNull IRowData rowData);

    /**
     * Notifies the visitor that all rows are visited.
     * This method is called after the last time <code>visitRow</code> was called.
     */
    void end();
}
