package cms.rendner.hexviewer.common.data.wrapper;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.model.data.IDataModel;
import org.jetbrains.annotations.NotNull;

/**
 * Simplifies the creation of {@link RowData} instances.
 *
 * @author rendner
 */
public final class RowDataBuilder
{
    /**
     * The data model to be used by the created data parts.
     */
    private final IDataModel dataModel;

    /**
     * The number of bytes per row
     */
    private final int bytesPerRow;

    /**
     * Creates a new instance with the specified values.
     *
     * @param dataModel   the data model to be used by the created data parts.
     * @param bytesPerRow the number of bytes per row.
     */
    public RowDataBuilder(@NotNull final IDataModel dataModel, final int bytesPerRow)
    {
        super();
        this.dataModel = dataModel;
        this.bytesPerRow = bytesPerRow;
    }

    /**
     * Creates a new row data part which points to the data for the specified row.
     *
     * @param rowIndex the index of the row.
     * @return the data of the row.
     */
    @NotNull
    public RowData build(final int rowIndex)
    {
        final int lastPossibleByteIndex = Math.max(0, dataModel.size() - 1);
        final int offsetOfFirstRowByte = IndexUtils.rowIndexToByteIndex(rowIndex, bytesPerRow);

        if (offsetOfFirstRowByte > lastPossibleByteIndex)
        {
            // invalid offset
            return new RowData(dataModel, offsetOfFirstRowByte, 0, rowIndex);
        }
        else
        {
            final int offsetOfLastRowByte = Math.min(lastPossibleByteIndex, offsetOfFirstRowByte + bytesPerRow - 1);
            final int numberOfRowBytes = Math.max(0, 1 + (offsetOfLastRowByte - offsetOfFirstRowByte));
            return new RowData(dataModel, offsetOfFirstRowByte, numberOfRowBytes, rowIndex);
        }
    }
}
