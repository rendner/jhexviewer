package cms.rendner.hexviewer.model.data;

import cms.rendner.hexviewer.view.JHexViewer;

/**
 * Provides the data displayed by a {@link JHexViewer}.
 *
 * @author rendner
 */
public interface IDataModel
{
    /**
     * Returns the number of bytes.
     * <p/>
     * The value returned should not change during the lifetime of the provider.
     * Repeated calls should always return the same result.
     *
     * @return the number of total available bytes, &gt;= 0.
     */
    long size();

    /**
     * @return <code>true</code> if the model contains no data, otherwise <code>false</code>.
     */
    boolean isEmpty();

    /**
     * Returns the byte value for the offset as int (the range of a signed byte is -128 to 127, but we want a range of 0 to 255).
     *
     * @param offset the offset in the internal data array, in the range [0, size()-1].
     * @return the value at the <code>offset</code>.
     */
    int getByte(long offset);
}
