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
    private @NotNull final ByteArea area;
    private @NotNull final JHexViewer hexViewer;

    private @NotNull final Color focusedCaret = Color.darkGray;
    private @NotNull final Color caret = new Color(0xABB6B9);
    private @NotNull final Color focusedSelection = new Color(0xA7D3FB);
    private @NotNull final Color selection = new Color(0x8788CFFB, true);
    private @NotNull final Color highlight = new Color(0x41FFA11D, true);
    private @NotNull final Color background = Color.white;

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
    public Color getRowElementForeground(int rowIndex, int elementIndex, int offset)
    {
        return Color.darkGray;
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
