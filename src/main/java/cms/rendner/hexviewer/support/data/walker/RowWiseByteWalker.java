package cms.rendner.hexviewer.support.data.walker;

import cms.rendner.hexviewer.support.data.visitor.IRowWiseByteVisitor;
import cms.rendner.hexviewer.core.model.data.IDataModel;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import cms.rendner.hexviewer.support.data.wrapper.RowData;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.IndexUtils;

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
    protected final IDataModel dataModel;

    /**
     * The number of bytes displayed in one row
     */
    private final int bytesPerRow;

    /**
     * Creates a new instance.
     *
     * @param dataModel   the data to iterate over. Cant be <code>null</code>.
     * @param bytesPerRow the number of bytes displayed in one row
     */
    public RowWiseByteWalker(final IDataModel dataModel, final int bytesPerRow)
    {
        super();

        CheckUtils.checkNotNull(dataModel);

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
    public void walk(final IRowWiseByteVisitor visitor, final int start, final int end)
    {
        visitor.start();

        int byteIndex = start;

        while (byteIndex <= end)
        {
            final IRowData rowData = createRowData(byteIndex, end);
            visitor.visitRow(rowData);
            byteIndex += rowData.size();
        }

        visitor.end();
    }

    /**
     * Creates a instance which only provides the specified range of data from the data model.
     *
     * @param start the start index of the byte, included in the range.
     * @param end   the end index of the byte, included in the range.
     * @return the part which only contains the bytes of the specified range.
     */
    protected IRowData createRowData(final int start, final int end)
    {
        final int offsetInRow = IndexUtils.byteIndexToIndexInRow(start, bytesPerRow);
        final int numberOfBytes = Math.min(bytesPerRow - offsetInRow, end + 1 - start);
        final int rowIndex = IndexUtils.byteIndexToRowIndex(start, bytesPerRow);

        final RowData result = new RowData(dataModel, bytesPerRow);
        result.setRowIndex(rowIndex);
        result.setRowRange(offsetInRow, numberOfBytes);
        return result;
    }
}
