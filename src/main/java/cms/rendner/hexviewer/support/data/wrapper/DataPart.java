package cms.rendner.hexviewer.support.data.wrapper;

import cms.rendner.hexviewer.core.model.data.IDataModel;
import org.jetbrains.annotations.NotNull;

/**
 * Refers to data of a particular range of the data model used in the {@link cms.rendner.hexviewer.core.JHexViewer}.
 *
 * @author rendner
 */
public class DataPart implements IDataPart
{
    /**
     * Number of bytes in this part.
     */
    protected int size;

    /**
     * The data model which provides the bytes.
     */
    @NotNull
    protected final IDataModel dataModel;

    /**
     * Index of the first byte in this part in the data model.
     */
    protected int offset;

    /**
     * Creates a new instance.
     *
     * @param dataModel the data model to use to get the bytes for the part.
     * @param offset    index of the first byte in this part in the data model.
     * @param size      the number of bytes in this part.
     */
    public DataPart(@NotNull final IDataModel dataModel, final int offset, final int size)
    {
        this.dataModel = dataModel;
        setRange(offset, size);
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size < 0;
    }

    @Override
    public int offset()
    {
        return offset;
    }

    @Override
    public int getByte(final int indexInPart)
    {
        return dataModel.getByte(offset + indexInPart);
    }

    /**
     * Adjusts the range of bytes in this part.
     *
     * @param offset index of the first byte in this part in the data model.
     * @param size   the number of bytes in this part.
     */
    protected void setRange(final int offset, final int size)
    {
        this.offset = offset;
        this.size = Math.max(0, Math.min(dataModel.size() - offset, size));
    }
}
