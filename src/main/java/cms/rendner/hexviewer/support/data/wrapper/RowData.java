package cms.rendner.hexviewer.support.data.wrapper;

import cms.rendner.hexviewer.core.model.data.IDataModel;

/**
 * Refers to data of a single row of the data model used in the {@link cms.rendner.hexviewer.core.JHexViewer}..
 *
 * @author rendner
 */
public class RowData extends DataPart implements IRowData
{
    /**
     * The index of the row to which this data belongs.
     */
    protected int rowIndex;

    /**
     * Number of excluded leading bytes in this row.
     */
    protected int excludedLeadingBytes;

    /**
     * Number of bytes per row, used to calculate the bytes which belong to the specified row.
     */
    protected int bytesPerRow;

    /**
     * Creates a new instance which the specified properties.
     *
     * @param dataModel   the data model which provides the bytes.
     * @param bytesPerRow the number of bytes per row.
     */
    public RowData(final IDataModel dataModel, final int bytesPerRow)
    {
        super(dataModel, 0, bytesPerRow);
        this.bytesPerRow = bytesPerRow;
    }

    /**
     * Adjusts the row index and updates the internal range of bytes which belong to the row index.
     *
     * @param rowIndex the index of the row for which this instance should provide the bytes.
     */
    public void setRowIndex(final int rowIndex)
    {
        this.rowIndex = rowIndex;
        setRange(rowIndex * bytesPerRow + excludedLeadingBytes, size);
    }

    /**
     * Adjusts the available bytes of the row
     *
     * @param offsetInRow the number of excluded leading bytes of the row.
     * @param size        the number of bytes of the row.
     */
    public void setRowRange(final int offsetInRow, final int size)
    {
        excludedLeadingBytes = offsetInRow;

        final int rangeOffset = rowIndex * bytesPerRow + excludedLeadingBytes;
        setRange(rangeOffset, size);
    }

    @Override
    public int excludedLeadingBytes()
    {
        return excludedLeadingBytes;
    }

    @Override
    public int excludedTrailingBytes()
    {
        return bytesPerRow - (size() + excludedLeadingBytes);
    }

    @Override
    public int rowIndex()
    {
        return rowIndex;
    }
}
