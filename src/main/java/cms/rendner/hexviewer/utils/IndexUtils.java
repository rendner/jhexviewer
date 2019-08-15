package cms.rendner.hexviewer.utils;

/**
 * Utility to convert between row and byte indices.
 *
 * @author rendner
 */
public final class IndexUtils
{
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
            return -1;
        }

        if (bytesPerRow > 0)
        {
            return rowIndex * bytesPerRow;
        }

        return -1;
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
            return -1;
        }

        if (bytesPerRow > 0)
        {
            return byteIndex / bytesPerRow;
        }

        return -1;
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
            return -1;
        }

        final int rowIndex = byteIndexToRowIndex(byteIndex, bytesPerRow);
        if (rowIndex != -1)
        {
            final int indexOfRowStartByte = rowIndexToByteIndex(rowIndex, bytesPerRow);
            if (indexOfRowStartByte != -1)
            {
                return byteIndex - indexOfRowStartByte;
            }
        }

        return -1;
    }

    /**
     * Hide constructor.
     */
    private IndexUtils(){}
}
