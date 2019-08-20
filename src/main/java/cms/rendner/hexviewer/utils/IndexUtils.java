package cms.rendner.hexviewer.utils;

/**
 * Utility to convert between row and byte indices.
 *
 * @author rendner
 */
public final class IndexUtils
{
    /**
     * Constant for an invalid index.
     */
    public static final int INVALID_INDEX = -1;

    /**
     *Checks if an index is odd or not.
     * Can be used to implementing alternating rows.
     *
     * @param index index to check.
     * @return <code>true</code> if index is odd otherwise <code>false</code>
     */
    public static boolean isOdd(final int index)
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
    public static boolean isEven(final int index)
    {
        return (index & 1) == 0;
    }

    /**
     * Returns the byte index (zero based) in the data provider of the first byte of a row.
     * <p/>
     * This method doesn't check if the calculated index is out of bounds.
     *
     * @param rowIndex    the index of the row to convert.
     * @param bytesPerRow the number of bytes per row.
     * @return the byte index of the first byte of the row, or <code>-1</code> if <code>rowIndex</code> or <code>bytesPerRow</code> is negative.
     */
    public static int rowIndexToByteIndex(final int rowIndex, final int bytesPerRow)
    {
        if (rowIndex < 0)
        {
            return INVALID_INDEX;
        }

        if (bytesPerRow > 0)
        {
            return rowIndex * bytesPerRow;
        }

        return INVALID_INDEX;
    }

    /**
     * Returns the index (zero based) of the row to which a byte belongs.
     * <p/>
     * This method doesn't check if the calculated index is out of bounds.
     *
     * @param byteIndex   the index of the byte to convert.
     * @param bytesPerRow the number of bytes per row.
     * @return the index of the row, or <code>-1</code> if <code>rowIndex</code> or <code>bytesPerRow</code> is negative.
     */
    public static int byteIndexToRowIndex(final int byteIndex, final int bytesPerRow)
    {
        if (byteIndex < 0)
        {
            return INVALID_INDEX;
        }

        if (bytesPerRow > 0)
        {
            return byteIndex / bytesPerRow;
        }

        return INVALID_INDEX;
    }

    /**
     * Returns the byte index (zero based) in the row to which a byte belongs.
     * <p/>
     * This method doesn't check if the calculated index is out of bounds.
     *
     * @param byteIndex   the index of the byte to convert.
     * @param bytesPerRow the number of bytes per row.
     * @return the index inside a row, or <code>-1</code> if <code>rowIndex</code> or <code>bytesPerRow</code> is negative.
     */
    public static int byteIndexToIndexInRow(final int byteIndex, final int bytesPerRow)
    {
        if (byteIndex < 0)
        {
            return INVALID_INDEX;
        }

        final int rowIndex = byteIndexToRowIndex(byteIndex, bytesPerRow);
        if (rowIndex != INVALID_INDEX)
        {
            final int indexOfRowStartByte = rowIndexToByteIndex(rowIndex, bytesPerRow);
            if (indexOfRowStartByte != INVALID_INDEX)
            {
                return byteIndex - indexOfRowStartByte;
            }
        }

        return INVALID_INDEX;
    }

    /**
     * Hide constructor.
     */
    private IndexUtils(){}
}
