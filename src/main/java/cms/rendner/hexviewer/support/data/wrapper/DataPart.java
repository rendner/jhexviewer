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
    private final int size;

    /**
     * The data model which provides the bytes.
     */
    @NotNull
    private final IDataModel dataModel;

    /**
     * Index of the first byte in this part in the data model.
     */
    private final int offset;

    /**
     * Creates a new instance.
     *
     * @param dataModel the data model to use to retrieve the bytes.
     * @param offset    index of the first byte in this part in the data model.
     * @param size      the number of bytes in this part.
     */
    public DataPart(@NotNull final IDataModel dataModel, final int offset, final int size)
    {
        this.dataModel = dataModel;
        this.offset = offset;
        this.size = size;
    }

    @Override
    public final int size()
    {
        return size;
    }

    @Override
    public final boolean isEmpty()
    {
        return size <= 0;
    }

    @Override
    public final int offset()
    {
        return offset;
    }

    @Override
    public final int getByte(final int indexInPart)
    {
        if(isEmpty() || indexInPart < 0 || indexInPart >= size)
        {
            throw new IndexOutOfBoundsException("The specified index '" + indexInPart + "' is out of bounds.");
        }
        return dataModel.getByte(offset + indexInPart);
    }

    /**
     * @return the offset and size of the instance prefixed with the name of the class.
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[offset:" + offset + ", size:" + size + "]";
    }
}
