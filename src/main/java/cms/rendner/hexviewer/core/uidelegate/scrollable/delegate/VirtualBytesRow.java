package cms.rendner.hexviewer.core.uidelegate.scrollable.delegate;

import cms.rendner.hexviewer.core.model.row.template.elements.ElementHitInfo;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;

import java.awt.*;

/**
 * Combines the row of the hex-area and ascii-area into a single row.
 * <p/>
 * A combined row is called a virtual row to distinguish between the existing rows of the areas and the combined row.
 *
 * @author rendner
 */
public class VirtualBytesRow
{
    /**
     * Constant for an invalid index.
     */
    private static final int INVALID_INDEX = -1;

    /**
     * A return value which is used to retrieve the result of a row hit test.
     * This instance be reused to minimize creation of new ElementHitInfo.
     */
    private final ElementHitInfo rvHitInfo = new ElementHitInfo();

    /**
     * The JHexViewer component.
     */
    private JHexViewer hexViewer;

    /**
     * Creates a new instance.
     *
     * @param hexViewer the JHexViewer component, to get access to the internal areas.
     */
    public VirtualBytesRow(final JHexViewer hexViewer)
    {
        super();
        this.hexViewer = hexViewer;
    }

    /**
     * Returns the bounds for a specific byte.
     *
     * @param virtualByteIndex the index of the byte in the virtual row. The value has to be &gt;= 0.
     * @param returnValue      this rect will be filled with the result.
     * @return the <code>returnValue</code> object.
     */
    public Rectangle getByteRect(final int virtualByteIndex, final Rectangle returnValue)
    {
        final AreaId id = virtualByteIndexToAreaId(virtualByteIndex);
        final int indexInArea = virtualByteIndexToByteIndexInArea(id, virtualByteIndex);

        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        rowsView.getByteRect(indexInArea, returnValue);
        returnValue.x += rowsView.getX();

        return returnValue;
    }

    /**
     * Returns the id of the area to which the index belongs.
     *
     * @param virtualByteIndex the index of the byte in the virtual row. The value has to be &gt;= 0.
     * @return the id of the area.
     */
    public AreaId virtualByteIndexToAreaId(final int virtualByteIndex)
    {
        return virtualByteIndex < hexViewer.bytesPerRow() ? AreaId.HEX : AreaId.ASCII;
    }

    /**
     * Converts a virtual byte index into a byte index for an area.
     *
     * @param id               the id of the area which contains the byte virtualByteIndex points to.
     * @param virtualByteIndex the index of the byte in the virtual row. The value has to be &gt;= 0.
     * @return the byte index inside the area.
     */
    public int virtualByteIndexToByteIndexInArea(final AreaId id, final int virtualByteIndex)
    {
        return AreaId.HEX.equals(id) ? virtualByteIndex : virtualByteIndex - hexViewer.bytesPerRow();
    }

    /**
     * Returns the id of the area to which the x location belongs.
     * <p/>
     * If the locations is left from the ascii-area, the id of the hex-area will be returned
     * otherwise the id of the ascii-area.
     *
     * @param virtualXLocation the x location in the virtual row.
     * @return the id of the area which contains this x location.
     */
    public AreaId virtualXLocationToArea(final int virtualXLocation)
    {
        final ByteRowsView asciiRowsView = hexViewer.getAsciiRowsView();
        return virtualXLocation < asciiRowsView.getX() ? AreaId.HEX : AreaId.ASCII;
    }

    /**
     * Returns the virtual index of the byte which intersects with the location.
     *
     * @param virtualXLocation the x location in the virtual row.
     * @return the virtual byte index for the virtual x location.
     */
    public int virtualXLocationToVirtualByteIndex(final int virtualXLocation)
    {
        final AreaId id = virtualXLocationToArea(virtualXLocation);
        final int elementIndex = virtualXLocationToElementIndex(id, virtualXLocation);
        if (elementIndex != INVALID_INDEX)
        {
            final int indexOffset = AreaId.HEX.equals(id) ? 0 : hexViewer.bytesPerRow();
            return indexOffset + elementIndex;
        }

        return INVALID_INDEX;
    }

    /**
     * Checks if a virtual byte index is valid.
     *
     * @param virtualByteIndex the index of the byte in the virtual row to check.
     * @return <code>true</code> if index is valid.
     */
    public boolean isValidVirtualByteIndex(final int virtualByteIndex)
    {
        return virtualByteIndex > -1 && virtualByteIndex < (hexViewer.bytesPerRow() * 2);
    }

    /**
     * Returns the index of a element of the specified area that intersects with the virtual location.
     *
     * @param id               the id of the area which contains the byte virtualByteIndex points to.
     * @param virtualXLocation the x location in the virtual row.
     * @return the element index which intersects with the virtual location.
     */
    private int virtualXLocationToElementIndex(final AreaId id, final int virtualXLocation)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        final IByteRowTemplate rowTemplate = rowsView.template();
        final int xInRowTemplate = virtualXLocation - rowsView.getX();
        rowTemplate.hitTest(xInRowTemplate, rvHitInfo);
        return rvHitInfo.index();
    }
}