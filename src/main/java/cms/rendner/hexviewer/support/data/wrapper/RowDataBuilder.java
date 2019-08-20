package cms.rendner.hexviewer.support.data.wrapper;

import cms.rendner.hexviewer.core.model.data.IDataModel;
import cms.rendner.hexviewer.utils.IndexUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Simplifies the creation of {@link RowData} instances.
 *
 * @author rendner
 */
public final class RowDataBuilder
{
    /**
     * Number of bytes displayed per row.
     */
    private final int bytesPerRow;

    /**
     * The data model to be used by the created data parts.
     */
    private final IDataModel dataModel;

    /**
     * Creates a new instance with the specified values.
     *
     * @param dataModel   the data model to be used by the created data parts.
     * @param bytesPerRow the number of bytes per row.
     */
    public RowDataBuilder(@NotNull final IDataModel dataModel, int bytesPerRow)
    {
        super();
        this.bytesPerRow = bytesPerRow;
        this.dataModel = dataModel;
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
        final int offsetOfFirstRowByte = Math.min(lastPossibleByteIndex, IndexUtils.rowIndexToByteIndex(rowIndex, bytesPerRow));
        final int offsetOfLastRowByte = Math.min(lastPossibleByteIndex, offsetOfFirstRowByte + bytesPerRow - 1);
        final int numberOfRowBytes = Math.max(0, 1 + (offsetOfLastRowByte - offsetOfFirstRowByte));
        return new RowData(
                dataModel,
                offsetOfFirstRowByte,
                numberOfRowBytes,
                rowIndex);
    }
}
