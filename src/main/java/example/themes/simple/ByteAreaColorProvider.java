package example.themes.simple;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Example implementation of an {@link IByteColorProvider}.
 *
 * @author rendner
 */
public class ByteAreaColorProvider implements IByteColorProvider
{
    private final ByteArea area;
    private final JHexViewer hexViewer;

    private final Color focusedCaret = Color.DARK_GRAY;
    private final Color caret = new Color(0xABB6B9);
    private final Color focusedSelection = new Color(0xA7D3FB);
    private final Color selection = new Color(0x8788CFFB, true);
    private final Color highlight = new Color(0x41FFA11D, true);
    private final Color background = Color.WHITE;

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
        return Color.DARK_GRAY;
    }

    @NotNull
    @Override
    public Color getBackground()
    {
        return background;
    }

    @Override
    public @Nullable Color getDefaultHighlight()
    {
        return highlight;
    }
}
