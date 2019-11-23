package example.themes.alternating;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Example implementation of an {@link IByteColorProvider}.
 *
 * @author rendner
 */
public final class ByteAreaColorProvider implements IByteColorProvider
{
    private @NotNull final ByteArea area;
    private @NotNull final JHexViewer hexViewer;

    private @NotNull final Color focusedCaret = new Color(0xD32705);
    private @NotNull final Color caret = new Color(0x5A6063);
    private @NotNull final Color focusedSelection = new Color(0xEBCFC4);
    private @NotNull final Color selection = new Color(0x7DF8DCD1, true);
    private @NotNull final Color evenRowBackground = Color.WHITE;
    private @NotNull final Color oddRowBackground = new Color(0xECECE1);
    private @NotNull final Color highlight = new Color(0xB5E9FF);
    private @NotNull final Color rowElementForeground = new Color(0x999999);

    public ByteAreaColorProvider(@NotNull final JHexViewer hexViewer, @NotNull final ByteArea area)
    {
        this.hexViewer = hexViewer;
        this.area = area;
    }

    @NotNull
    @Override
    public Color getCaret()
    {
        return hexViewer.getCaretFocusedArea() == area ? focusedCaret : caret;
    }

    @NotNull
    @Override
    public Color getSelection()
    {
        return hexViewer.getCaretFocusedArea() == area ? focusedSelection : selection;
    }

    @NotNull
    @Override
    public Color getRowBackground(final int rowIndex)
    {
        return IndexUtils.isEven(rowIndex) ? evenRowBackground : oddRowBackground;
    }

    @NotNull
    @Override
    public Color getRowElementForeground(final int byteValue, final long offset, final int rowIndex, final int elementInRowIndex)
    {
        return rowElementForeground;
    }

    @NotNull
    @Override
    public Color getDefaultHighlight()
    {
        return highlight;
    }
}
