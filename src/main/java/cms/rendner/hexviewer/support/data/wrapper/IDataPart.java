package cms.rendner.hexviewer.support.data.wrapper;

/**
 * Refers to data of a particular range of the data model used in the {@link cms.rendner.hexviewer.core.JHexViewer}.
 *
 * @author rendner
 */
public interface IDataPart
{
    /**
     * @return index of the first byte in this part in the data model of the {@link cms.rendner.hexviewer.core.JHexViewer}.
     */
    int offset();

    /**
     * @return the number of bytes in this part.
     */
    int size();

    /**
     * @return <code>true</code> if this part contains no byte.
     */
    boolean isEmpty();

    /**
     * Returns the byte value for the index as int (a signed byte is -128 to 127, but we want a range of 0 to 255).
     * This method doesn't validate if the index is out of bounds of the data part.
     *
     * @param indexInPart the index in the data part.
     * @return the value at the <code>indexInPart</code>.
     * @throws IndexOutOfBoundsException if the byte is read outside of the underlying data model.
     */
    int getByte(final int indexInPart) throws IndexOutOfBoundsException;
}
