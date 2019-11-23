package example.themes.retro;

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

    private @NotNull final Color focusedCaret = Color.DARK_GRAY;
    private @NotNull final Color caret = new Color(0xABB6B9);
    private @NotNull final Color focusedSelection = new Color(0xFFA11D);
    private @NotNull final Color selection = new Color(0x41FFA11D, true);
    private @NotNull final Color background = new Color(0xECEAD3);
    private @NotNull final Color rowElementForeground = new Color(0x9D8D73);

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
    public Color getRowElementForeground(final int byteValue, final long offset, final int rowIndex, final int elementInRowIndex)
    {
        return rowElementForeground;
    }

    @NotNull
    @Override
    public Color getBackground()
    {
        return background;
    }
}
