package cms.rendner.hexviewer.support.data.wrapper;

import cms.rendner.hexviewer.core.model.data.IDataModel;
import org.jetbrains.annotations.NotNull;

/**
 * Refers to data of a single row of the data model used in the {@link cms.rendner.hexviewer.core.JHexViewer}.
 *
 * @author rendner
 */
public final class RowData extends DataPart
{
    /**
     * The index of the row to which this data belongs.
     */
    private final int rowIndex;

    /**
     * Creates a new instance which the specified properties.
     *
     * @param dataModel the data model to use to get the bytes for the part.
     * @param offset    index of the first byte in this part in the data model.
     * @param size      the number of bytes in this part.
     * @param rowIndex  the index of the row to which this data belongs.
     */
    public RowData(@NotNull final IDataModel dataModel, final int offset, final int size, final int rowIndex)
    {
        super(dataModel, offset, size);
        this.rowIndex = rowIndex;
    }

    /**
     * The index of the row to which this data belongs.
     */
    public int rowIndex()
    {
        return rowIndex;
    }

    /**
     * @return the offset and size of the instance prefixed with the name of the class.
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[offset:" + offset() + ", size:" + size() + ", rowIndex: " + rowIndex + "]";
    }
}
