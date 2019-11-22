package cms.rendner.hexviewer.common.utils;

/**
 * Utility to convert between row and byte indices.
 *
 * @author rendner
 */
public final class IndexUtils
{
    /**
     * Constant representing an invalid index.
     */
    public static final int INVALID_INDEX = -1;

    /**
     * Checks if an index is odd or not.
     * Can be used to implementing alternating rows.
     *
     * @param index index to check.
     * @return <code>true</code> if index is odd otherwise <code>false</code>
     */
    public static boolean isOdd(final long index)
    {
        return (index & 1) != 0;
    }

    /**
     * Checks if an index is even or not.
     * Can be used to implementing alternating rows.
     *
     * @param index index to check.
     * @return <code>true</code> if index is even otherwise <code>false</code>
     */
    public static boolean isEven(final long index)
    {
        return (index & 1) == 0;
    }

    /**
     * Returns the byte index (zero based) for a row index.
     * <p/>
     * This method doesn't check if the calculated index is out of bounds.
     *
     * @param rowIndex    the row index to convert.
     * @param bytesPerRow the number of bytes per row, &gt;= 1.
     * @return the index of the first byte of the row, or <code>-1</code> if <code>rowIndex</code> or <code>bytesPerRow</code> is negative.
     */
    public static long rowIndexToByteIndex(final int rowIndex, final int bytesPerRow)
    {
        if (rowIndex < 0 || bytesPerRow < 1)
        {
            return INVALID_INDEX;
        }

        return (long)rowIndex * bytesPerRow;
    }

    /**
     * Returns the index (zero based) of the row to which a byte belongs.
     * <p/>
     * This method doesn't check if the calculated index is out of bounds.
     *
     * @param byteIndex   the byte index to convert.
     * @param bytesPerRow the number of bytes per row, &gt;= 1.
     * @return the index of the row, or <code>-1</code> if <code>rowIndex</code> or <code>bytesPerRow</code> is negative.
     */
    public static int byteIndexToRowIndex(final long byteIndex, final int bytesPerRow)
    {
        if (byteIndex < 0 || bytesPerRow < 1)
        {
            return INVALID_INDEX;
        }

        return (int)(byteIndex / bytesPerRow);
    }

    /**
     * Returns the byte index (zero based) in the row to which a byte belongs.
     *
     * @param byteIndex   the byte index to convert.
     * @param bytesPerRow the number of bytes per row, &gt;= 1.
     * @return the index inside a row, or <code>-1</code> if <code>rowIndex</code> or <code>bytesPerRow</code> is negative.
     */
    public static int byteIndexToIndexInRow(final long byteIndex, final int bytesPerRow)
    {
        if (byteIndex < 0 || bytesPerRow < 1)
        {
            return INVALID_INDEX;
        }

        return (int)(byteIndex % bytesPerRow);
    }

    /**
     * Hide constructor.
     */
    private IndexUtils()
    {
    }
}
