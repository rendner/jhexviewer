package cms.rendner.hexviewer.common.data.wrapper;

import cms.rendner.hexviewer.model.data.IDataModel;
import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Refers to data of a particular range of the data model used in the {@link JHexViewer}.
 *
 * @author rendner
 */
public final class DataPart
{
    /**
     * Number of bytes in this part.
     */
    private final long size;

    /**
     * The data model which provides the bytes.
     */
    @NotNull
    private final IDataModel dataModel;

    /**
     * Index of the first byte in this part in the data model.
     */
    private final long offset;

    /**
     * Creates a new instance.
     *
     * @param dataModel the data model to use to retrieve the bytes.
     * @param offset    index of the first byte in this part in the data model.
     * @param size      the number of bytes in this part.
     */
    public DataPart(@NotNull final IDataModel dataModel, final long offset, final long size)
    {
        this.dataModel = dataModel;
        this.offset = offset;
        this.size = size;
    }

    /**
     * @return the number of bytes in this part.
     */
    public final long size()
    {
        return size;
    }

    /**
     * @return <code>true</code> if this part contains no byte.
     */
    public final boolean isEmpty()
    {
        return size <= 0;
    }

    /**
     * @return index of the first byte in this part in the data model of the {@link JHexViewer}.
     */
    public final long offset()
    {
        return offset;
    }

    /**
     * Returns the byte value for the index as int (a signed byte is -128 to 127, but we want a range of 0 to 255).
     *
     * @param indexInPart the index in the data part, in the range [0, size()-1].
     * @return the value at the <code>indexInPart</code>.
     */
    public final int getByte(final long indexInPart)
    {
        if (isEmpty() || indexInPart < 0 || indexInPart >= size)
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
