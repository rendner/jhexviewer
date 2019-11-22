package example.themes.console;

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

    private @NotNull final Color focusedCaret = Color.white;
    private @NotNull final Color caret = new Color(0xABB6B9);
    private @NotNull final Color focusedSelection = new Color(0x616B65);
    private @NotNull final Color selection = new Color(0x61, 0x6B, 0x65, 0xA5);
    private @NotNull final Color rowElementForeground = Color.white;
    private @NotNull final Color background = new Color(0x232629);
    private @NotNull final Color highlight = new Color(0x6095B6);

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
    public Color getBackground()
    {
        return background;
    }

    @NotNull
    @Override
    public Color getRowElementForeground(final long offset, final int rowIndex, final int elementIndex)
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
