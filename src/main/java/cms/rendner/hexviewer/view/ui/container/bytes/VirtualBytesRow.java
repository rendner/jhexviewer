package cms.rendner.hexviewer.view.ui.container.bytes;

import cms.rendner.hexviewer.common.rowtemplate.bytes.IByteRowTemplate;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Combines the row of the hex-area and ascii-area into a single row.
 * <p/>
 * A combined row is called a virtual row to distinguish between the existing rows of the areas and the combined row.
 *
 * @author rendner
 */
final class VirtualBytesRow
{
    /**
     * Constant for an invalid index.
     */
    private static final int INVALID_INDEX = -1;


    @NotNull
    private final ByteArea leftArea;
    @NotNull
    private final ByteArea rightArea;

    VirtualBytesRow(@NotNull final ByteArea leftArea, @NotNull final ByteArea rightArea)
    {
        super();
        this.leftArea = leftArea;
        this.rightArea = rightArea;
    }

    /**
     * Returns the bounds for a specific byte.
     *
     * @param virtualByteIndex the index of the byte in the virtual row. The value has to be &gt;= 0.
     * @return the bounds of the byte.
     */
    @NotNull
    Rectangle getByteRect(final int virtualByteIndex)
    {
        final ByteArea area = virtualByteIndexToArea(virtualByteIndex);
        final int indexInArea = virtualByteIndexToByteIndexInArea(area, virtualByteIndex);
        final Rectangle result = area.getByteRect(indexInArea);
        result.x += area.getX();

        return result;
    }

    /**
     * Returns the virtual index of the byte which intersects with the location.
     *
     * @param virtualXLocation the x location in the virtual row.
     * @return the virtual byte index for the virtual x location.
     */
    int virtualXLocationToVirtualByteIndex(final int virtualXLocation)
    {
        final ByteArea area = virtualXLocationToArea(virtualXLocation);
        final int elementInRowIndex = virtualXLocationToElementIndex(area, virtualXLocation);
        if (elementInRowIndex != INVALID_INDEX)
        {
            final int elementsInFrontOfArea = leftArea == area ? 0 : getBytesPerRow();
            return elementsInFrontOfArea + elementInRowIndex;
        }

        return INVALID_INDEX;
    }

    /**
     * Checks if a virtual byte index is valid.
     *
     * @param virtualByteIndex the index of the byte in the virtual row to check.
     * @return <code>true</code> if index is valid.
     */
    boolean isValidVirtualByteIndex(final int virtualByteIndex)
    {
        return virtualByteIndex > -1 && virtualByteIndex < (getBytesPerRow() * 2);
    }

    /**
     * Returns the index of an element of the specified area that intersects with the virtual location.
     *
     * @param area             the area which contains the byte virtualByteIndex points to.
     * @param virtualXLocation the x location in the virtual row.
     * @return the element index which intersects with the virtual location, or <code>-1</code> if no intersection can be
     * found.
     */
    private int virtualXLocationToElementIndex(@NotNull final ByteArea area, final int virtualXLocation)
    {
        final int xInRowTemplate = virtualXLocation - area.getX();
        return area.hitTest(xInRowTemplate, 0).map(hitInfo -> (int)hitInfo.index()).orElse(INVALID_INDEX);
    }

    /**
     * Returns the area to which the index belongs.
     *
     * @param virtualByteIndex the index of the byte in the virtual row. The value has to be &gt;= 0.
     * @return the area.
     */
    @NotNull
    private ByteArea virtualByteIndexToArea(final int virtualByteIndex)
    {
        return virtualByteIndex < getBytesPerRow() ? leftArea : rightArea;
    }

    /**
     * Converts a virtual byte index into a byte index for an area.
     *
     * @param area             the area which contains the byte virtualByteIndex points to.
     * @param virtualByteIndex the index of the byte in the virtual row. The value has to be &gt;= 0.
     * @return the byte index inside the area.
     */
    private int virtualByteIndexToByteIndexInArea(@NotNull final ByteArea area, final int virtualByteIndex)
    {
        return leftArea == area ? virtualByteIndex : virtualByteIndex - getBytesPerRow();
    }

    /**
     * Returns the area to which the x location belongs.
     * <p/>
     * If the locations is left from the ascii-area, the hex-area will be returned
     * otherwise the ascii-area.
     *
     * @param virtualXLocation the x location in the virtual row.
     * @return the area which contains this x location.
     */
    @NotNull
    private ByteArea virtualXLocationToArea(final int virtualXLocation)
    {
        return virtualXLocation < rightArea.getX() ? leftArea : rightArea;
    }

    private int getBytesPerRow()
    {
        return leftArea.getRowTemplate().map(IByteRowTemplate::elementCount).orElse(0);
    }
}
