package cms.rendner.hexviewer.core.uidelegate.scrollable.delegate;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.swing.scrollable.ScrollDirection;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Default implementation of a scroll delegate.
 *
 * @author rendner
 */
public class DefaultScrollableDelegate extends AbstractScrollableDelegate
{
    /**
     * Used to manage the hex-rows and ascii-rows as large virtual rows.
     */
    private VirtualBytesRow virtualBytesRow;

    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        super.install(hexViewer);
        virtualBytesRow = new VirtualBytesRow(hexViewer);
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
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
                final Rectangle leadingByteRect = virtualBytesRow.getByteRect(leadingByteIndex);

                if (leadingByteRect.x < visibleRect.x)
                {
                    return visibleRect.x - leadingByteRect.x;
                }
                else
                {
                    final int prevByteIndex = leadingByteIndex - 1;
                    if (virtualBytesRow.isValidVirtualByteIndex(prevByteIndex))
                    {
                        final Rectangle prevByteRect = virtualBytesRow.getByteRect(prevByteIndex);
                        return visibleRect.x - prevByteRect.x;
                    }
                }
            }

            return visibleRect.x;
        }
        else
        {
            if (virtualBytesRow.isValidVirtualByteIndex(leadingByteIndex))
            {
                final Rectangle leadingByteRect = virtualBytesRow.getByteRect(leadingByteIndex);

                if (leadingByteRect.x > visibleRect.x)
                {
                    return leadingByteRect.x - visibleRect.x;
                }
                else
                {
                    final int nextByteIndex = leadingByteIndex + 1;
                    if (virtualBytesRow.isValidVirtualByteIndex(nextByteIndex))
                    {
                        final Rectangle nextByteRect = virtualBytesRow.getByteRect(nextByteIndex);
                        return nextByteRect.x - visibleRect.x;
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
        final Rectangle leadingRowRect = getRowRect(id, leadingRowIndex);

        if (ScrollDirection.DOWN == direction)
        {
            return leadingRowRect.height - (visibleRect.y - leadingRowRect.y);
        }
        else
        {
            if (leadingRowRect.y == visibleRect.y)
            {
                if (leadingRowIndex == 0)
                {
                    return 0;
                }
                else
                {
                    final int prevRowIndex = leadingRowIndex - 1;
                    final Rectangle prevRowRect = getRowRect(id, prevRowIndex);
                    return prevRowRect.height;
                }
            }
            else
            {
                return visibleRect.y - leadingRowRect.y;
            }
        }
    }

    /**
     * Reads the bounds of a row into the return value.
     *
     * @param id          the id of the area the row is located.
     * @param rowIndex    the index of the row
     * @return the bounds of the specified row.
     */
    private Rectangle getRowRect(final AreaId id, final int rowIndex)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        final Rectangle result = rowsView.getRowRect(rowIndex);
        result.x += rowsView.getX();
        return result;
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
