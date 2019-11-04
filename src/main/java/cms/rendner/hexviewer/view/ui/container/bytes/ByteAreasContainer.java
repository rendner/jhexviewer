package cms.rendner.hexviewer.view.ui.container.bytes;

import cms.rendner.hexviewer.view.components.areas.bytes.AsciiArea;
import cms.rendner.hexviewer.view.components.areas.bytes.HexArea;
import cms.rendner.hexviewer.view.ui.container.common.BaseAreaContainer;
import cms.rendner.hexviewer.view.ui.container.common.ScrollDirection;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This container wraps the hex and an ascii-area of the {@link cms.rendner.hexviewer.view.JHexViewer} and adds
 * scroll support for theses areas.
 *
 * @author rendner
 */
public final class ByteAreasContainer extends BaseAreaContainer
{
    /**
     * The hex-area component of the {@link cms.rendner.hexviewer.view.JHexViewer}.
     */
    @NotNull
    private final HexArea hexArea;

    /**
     * The ascii-area component of the {@link cms.rendner.hexviewer.view.JHexViewer}.
     */
    @NotNull
    private final AsciiArea asciiArea;

    /**
     * Used to manage a hex- and ascii-row as one row.
     */
    @NotNull
    private final VirtualBytesRow virtualBytesRow;

    /**
     * Creates a new instance.
     * @param hexArea the hex-area component of the {@link cms.rendner.hexviewer.view.JHexViewer}.
     * @param asciiArea the ascii-area component of the {@link cms.rendner.hexviewer.view.JHexViewer}.
     */
    public ByteAreasContainer(@NotNull final HexArea hexArea, @NotNull final AsciiArea asciiArea)
    {
        super();

        this.hexArea = hexArea;
        this.hexArea.setAutoscrolls(true);

        this.asciiArea = asciiArea;
        this.asciiArea.setAutoscrolls(true);

        virtualBytesRow = new VirtualBytesRow(this.hexArea, this.asciiArea);

        setLayout(new BorderLayout());
        add(this.hexArea, BorderLayout.LINE_START);
        add(this.asciiArea, BorderLayout.CENTER);
    }

    @Override
    public int rowHeight()
    {
        return hexArea.getRowHeight();
    }

    @Override
    protected int computeHorizontalUnitIncrement(@NotNull final Rectangle visibleRect,
                                                 @MagicConstant(flags = {ScrollDirection.LEFT, ScrollDirection.RIGHT}) final int direction)
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

            return getWidth() - (visibleRect.x + visibleRect.width);
        }
    }
}