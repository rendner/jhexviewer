package cms.rendner.hexviewer.support.data.walker;

import cms.rendner.hexviewer.core.model.data.IDataModel;
import cms.rendner.hexviewer.support.data.visitor.IRowWiseByteVisitor;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import cms.rendner.hexviewer.support.data.wrapper.RowDataBuilder;
import cms.rendner.hexviewer.utils.IndexUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Iterates row-wise over a specified range of bytes of the data model used by the {@link cms.rendner.hexviewer.core.JHexViewer}.
 *
 * @author rendner
 */
public class RowWiseByteWalker
{
    /**
     * Provides the data that is iterated over.
     */
    @NotNull
    protected final IDataModel dataModel;

    /**
     * The number of bytes displayed in one row
     */
    private final int bytesPerRow;

    /**
     * Creates a new instance.
     *
     * @param dataModel   the data to iterate over.
     * @param bytesPerRow the number of bytes displayed in one row
     */
    public RowWiseByteWalker(@NotNull final IDataModel dataModel, final int bytesPerRow)
    {
        super();

        this.dataModel = dataModel;
        this.bytesPerRow = bytesPerRow;
    }

    /**
     * Iterates of the specified range of bytes row-wise and forwards each row to the <code>visitor</code>.
     *
     * @param visitor to visit the bytes of the specified range row-wise.
     * @param start   the start index of the byte, included in the range.
     * @param end     the end index of the byte, included in the range.
     */
    public void walk(@NotNull final IRowWiseByteVisitor visitor, final int start, final int end)
    {
        visitor.start();

        int currentRowIndex = IndexUtils.byteIndexToRowIndex(start, bytesPerRow);
        final int lastRowIndex = IndexUtils.byteIndexToRowIndex(end, bytesPerRow);
        final RowDataBuilder rowDataBuilder = new RowDataBuilder(dataModel, bytesPerRow);

        while (currentRowIndex <= lastRowIndex)
        {
            final IRowData rowData = rowDataBuilder.build(currentRowIndex);
            final int leadingBytesToIgnore = Math.max(0, start - rowData.offset());
            final int trailingBytesToIgnore = Math.max(0, (rowData.offset() + rowData.size() - 1) - end);

            visitor.visitRow(rowData, leadingBytesToIgnore, trailingBytesToIgnore);

            currentRowIndex++;
        }

        visitor.end();
    }
}
