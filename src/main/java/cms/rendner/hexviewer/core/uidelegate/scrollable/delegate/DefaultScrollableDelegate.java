package cms.rendner.hexviewer.core.uidelegate.scrollable.delegate;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.swing.scrollable.ScrollDirection;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;

import java.awt.*;

/**
 * Default implementation of a scroll delegate.
 *
 * @author rendner
 */
public class DefaultScrollableDelegate extends AbstractScrollableDelegate
{
    /**
     * A return value which is used to retrieve the bounds of a row.
     * This instance be reused to minimize creation of new rectangles.
     */
    private final Rectangle rvRect = new Rectangle();

    /**
     * Used to manage the hex-rows and ascii-rows as large virtual rows.
     */
    private VirtualBytesRow virtualBytesRow;

    @Override
    public void install(final JHexViewer hexViewer)
    {
        super.install(hexViewer);
        virtualBytesRow = new VirtualBytesRow(hexViewer);
    }

    @Override
    public void uninstall(final JHexViewer hexViewer)
    {
        virtualBytesRow = null;
        super.uninstall(hexViewer);
    }

    @Override
    protected int computeHorizontalUnitIncrement(final Rectangle visibleRect, final int direction)
    {
        final int leadingByteIndex = virtualBytesRow.virtualXLocationToVirtualByteIndex(visibleRect.x);

        if (ScrollDirection.LEFT == direction)
        {
            if (virtualBytesRow.isValidVirtualByteIndex(leadingByteIndex))
            {
                final Rectangle rvLeadingByteRect = virtualBytesRow.getByteRect(leadingByteIndex, rvRect);

                if (rvLeadingByteRect.x < visibleRect.x)
                {
                    return visibleRect.x - rvLeadingByteRect.x;
                }
                else
                {
                    final int prevByteIndex = leadingByteIndex - 1;
                    if (virtualBytesRow.isValidVirtualByteIndex(prevByteIndex))
                    {
                        final Rectangle rvPrevByteRect = virtualBytesRow.getByteRect(prevByteIndex, rvRect);
                        return visibleRect.x - rvPrevByteRect.x;
                    }
                }
            }

            return visibleRect.x;
        }
        else
        {
            if (virtualBytesRow.isValidVirtualByteIndex(leadingByteIndex))
            {
                final Rectangle rvLeadingByteIndex = virtualBytesRow.getByteRect(leadingByteIndex, rvRect);

                if (rvLeadingByteIndex.x > visibleRect.x)
                {
                    return rvLeadingByteIndex.x - visibleRect.x;
                }
                else
                {
                    final int nextByteIndex = leadingByteIndex + 1;
                    if (virtualBytesRow.isValidVirtualByteIndex(nextByteIndex))
                    {
                        final Rectangle rvNextByteIndex = virtualBytesRow.getByteRect(nextByteIndex, rvRect);
                        return rvNextByteIndex.x - visibleRect.x;
                    }
                }
            }

            return viewToScroll.getWidth() - (visibleRect.x + visibleRect.width);
        }
    }

    @Override
    protected int computeVerticalUnitIncrement(final Rectangle visibleRect, final int direction)
    {
        final int leadingRowIndex = verticalLocationToRowIndex(visibleRect.y);

        if (leadingRowIndex == -1)
        {
            return 0;
        }

        final AreaId id = horizontalLocationToArea(visibleRect.x);
        final Rectangle rvLeadingRowRect = getRowRect(id, leadingRowIndex, rvRect);

        if (ScrollDirection.DOWN == direction)
        {
            return rvLeadingRowRect.height - (visibleRect.y - rvLeadingRowRect.y);
        }
        else
        {
            if (rvLeadingRowRect.y == visibleRect.y)
            {
                if (leadingRowIndex == 0)
                {
                    return 0;
                }
                else
                {
                    final int prevRowIndex = leadingRowIndex - 1;
                    final Rectangle rvPrevRowRect = getRowRect(id, prevRowIndex, rvRect);
                    return rvPrevRowRect.height;
                }
            }
            else
            {
                return visibleRect.y - rvLeadingRowRect.y;
            }
        }
    }

    /**
     * Reads the bounds of a row into the return value.
     *
     * @param id          the id of the area the row is located.
     * @param rowIndex    the index of the row
     * @param returnValue the result is applied to this rectangle, can't be <code>null</code>.
     * @return the modified result which contains the bounds of the specified row.
     */
    private Rectangle getRowRect(final AreaId id, final int rowIndex, final Rectangle returnValue)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        rowsView.getRowRect(rowIndex, returnValue);
        returnValue.x += rowsView.getX();
        return returnValue;
    }

    /**
     * Returns the index of the row which contains the specified y coordinate.
     *
     * @param yLocation the y value to convert.
     * @return the index of the row, or <code>-1</code> if the specified y value was out of view bounds.
     */
    private int verticalLocationToRowIndex(final int yLocation)
    {
        return hexViewer.getHexRowsView().verticalLocationToRowIndex(yLocation);
    }

    /**
     * Returns the id of the area which contains the specified x coordinate.
     * <p/>
     * If the locations is left from the ascii-area, the id of the hex-area will be returned
     * otherwise the id of the ascii-area.
     *
     * @param xLocation the x value to convert.
     * @return the id of the area, never <code>null</code>.
     */
    private AreaId horizontalLocationToArea(final int xLocation)
    {
        final ByteRowsView asciiRowsView = hexViewer.getAsciiRowsView();
        return xLocation < asciiRowsView.getX() ? AreaId.HEX : AreaId.ASCII;
    }
}
