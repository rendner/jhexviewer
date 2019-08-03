package example.themes;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.color.AbstractRowColorProvider;
import cms.rendner.hexviewer.core.view.color.ICaretColorProvider;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import cms.rendner.hexviewer.support.themes.AbstractTheme;
import cms.rendner.hexviewer.swing.separator.Separator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This is a short example to demonstrate how to apply themes.
 *
 * @author rendner
 */
public class DarkTheme extends AbstractTheme
{
    @Override
    protected void customizeBackground(@NotNull final JHexViewer hexViewer)
    {
        final Color color = Color.black;
        hexViewer.setBackground(color);
        hexViewer.setOffsetViewBackground(color);
        hexViewer.setByteViewBackground(color);
    }

    @Override
    protected void applyBorders(@NotNull final JHexViewer hexViewer)
    {
        final int inset = 10;
        final Border outsideBorder = BorderFactory.createEmptyBorder(inset, inset, inset, inset);
        final Border insideBorder = BorderFactory.createLineBorder(Color.white);
        hexViewer.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));

        final Separator separator = new Separator(Color.white, 1);
        hexViewer.setOffsetSeparator(separator);
        hexViewer.setByteViewsSeparator(separator);
    }

    @Override
    protected void customizeHighlighter(@NotNull final IHighlighter highlighter)
    {
        highlighter.setDefaultColor(new Color(0x0094C8));
    }

    @Override
    protected void customizeCaret(@NotNull final ICaret caret)
    {
        caret.setColorProvider(new ICaretColorProvider()
        {
            private final Color focusedCaretColor = Color.white;
            private final Color caretColor = new Color(0xABB6B9);

            private final Color focusedSelectionColor = new Color(0x616B6E);
            private final Color selectionColor = new Color(0xA9616B6E, true);

            @NotNull
            @Override
            public Color getCaretColor(@NotNull final AreaId areaId, final boolean focused)
            {
                return focused ? focusedCaretColor : caretColor;
            }

            @NotNull
            @Override
            public Color getSelectionColor(@NotNull final AreaId areaId, final boolean focused)
            {
                return focused ? focusedSelectionColor : selectionColor;
            }
        });
    }

    @Override
    protected void applyRowColorProvider(@NotNull final JHexViewer hexViewer)
    {
        final IRowColorProvider byteAreaColors = new AbstractRowColorProvider()
        {
            private final Color rowElementForeground = new Color(0x9FD52B);

            @NotNull
            @Override
            public Color getRowBackground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex)
            {
                return Color.black;
            }

            @Nullable
            @Override
            public Color getRowElementBackground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex, final int elementIndex)
            {
                return null;
            }

            @NotNull
            @Override
            public Color getRowElementForeground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex, final int elementIndex)
            {
                return rowElementForeground;
            }
        };
        final IRowColorProvider offsetAreaColors = new AbstractRowColorProvider()
        {
            private final Color caretRowElementForeground = new Color(0xFFFFFF);
            private final Color rowElementForeground = new Color(0xABB6B9);

            @NotNull
            @Override
            public Color getRowBackground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex)
            {
                return Color.black;
            }

            @Nullable
            @Override
            public Color getRowElementBackground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex, final int elementIndex)
            {
                return null;
            }

            @NotNull
            @Override
            public Color getRowElementForeground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex, final int elementIndex)
            {
                return hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(hexViewer, rowIndex) ? caretRowElementForeground : rowElementForeground;
            }
        };
        hexViewer.setRowColorProvider(AreaId.OFFSET, offsetAreaColors);
        hexViewer.setRowColorProvider(AreaId.HEX, byteAreaColors);
        hexViewer.setRowColorProvider(AreaId.ASCII, byteAreaColors);
    }
}
