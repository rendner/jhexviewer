package cms.rendner.hexviewer.view.ui.container.bytes;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.HexArea;
import cms.rendner.hexviewer.view.components.areas.bytes.TextArea;
import cms.rendner.hexviewer.view.ui.container.common.BaseAreaContainer;
import cms.rendner.hexviewer.view.ui.container.common.ScrollDirection;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This container wraps the hex and an text-area of the {@link cms.rendner.hexviewer.view.JHexViewer} and adds
 * scroll support for theses areas.
 *
 * @author rendner
 */
public final class ByteAreasContainer extends BaseAreaContainer
{
    /**
     * Used to manage a hex- and text-row as one combined row.
     */
    @NotNull
    private final VirtualBytesRow virtualBytesRow;

    /**
     * Creates a new instance.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    public ByteAreasContainer(@NotNull JHexViewer hexViewer)
    {
        super();

        final HexArea hexArea = hexViewer.getHexArea();
        hexArea.setAutoscrolls(true);

        final TextArea textArea = hexViewer.getTextArea();
        textArea.setAutoscrolls(true);

        virtualBytesRow = new VirtualBytesRow(hexViewer, hexArea, textArea);

        setLayout(new BorderLayout());
        add(hexArea, BorderLayout.LINE_START);
        add(textArea, BorderLayout.CENTER);
    }

    @Override
    public int rowHeight()
    {
        return virtualBytesRow.rowHeight();
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
