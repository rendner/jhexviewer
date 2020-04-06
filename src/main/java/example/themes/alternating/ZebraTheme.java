package example.themes.alternating;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.RowBasedBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import cms.rendner.hexviewer.view.themes.AbstractTheme;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Example implementation how to customize the rendering of the {@link JHexViewer}.
 *
 * @author rendner
 */
public class ZebraTheme extends AbstractTheme
{
    @Override
    protected void adjustPainters(@NotNull final JHexViewer hexViewer)
    {
        setAreaBackgroundPainter(hexViewer.getOffsetArea(), new RowBasedBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY);

            @Override
            public void paintRow(@NotNull final RowGraphics rowGraphics, @NotNull AreaComponent component, final boolean isLastRow)
            {
                super.paintRow(rowGraphics, component, isLastRow);
                separator.paintBorder(component, rowGraphics.g, 0, 0, component.getWidth(), component.getRowHeight());
            }
        });
        setAreaBackgroundPainter(hexViewer.getHexArea(), new RowBasedBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY);

            @Override
            public void paintRow(@NotNull final RowGraphics rowGraphics, @NotNull AreaComponent component, final boolean isLastRow)
            {
                super.paintRow(rowGraphics, component, isLastRow);
                separator.paintBorder(component, rowGraphics.g, 0, 0, component.getWidth(), component.getRowHeight());
            }
        });
        setAreaBackgroundPainter(hexViewer.getTextArea(), new RowBasedBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY);

            @Override
            public void paintRow(@NotNull final RowGraphics rowGraphics, @NotNull AreaComponent component, final boolean isLastRow)
            {
                super.paintRow(rowGraphics, component, isLastRow);
                separator.paintBorder(component, rowGraphics.g, 0, 0, component.getWidth(), component.getRowHeight());
            }
        });
    }

    @Override
    protected void adjustComponentDefaults(@NotNull final JHexViewer hexViewer)
    {
        final Color color = new Color(20, 26, 40);
        hexViewer.setBackground(color);

        final int inset = 2;
        final Border outsideBorder = BorderFactory.createEmptyBorder(inset, inset, inset, inset);
        final Border insideBorder = BorderFactory.createLineBorder(Color.WHITE);
        hexViewer.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
    }

    @Override
    protected void adjustColorProviders(@NotNull final JHexViewer hexViewer)
    {
        final IOffsetColorProvider offsetColorProvider = new IOffsetColorProvider()
        {
            private final Color rowElementForegroundWhenCaretInRow = Color.BLACK;
            private final Color rowElementForeground = new Color(0x999999);
            private final Color evenRowBackground = new Color(0xE8E8E8);
            private final Color oddRowBackground = new Color(0xCFCFC4);

            @NotNull
            @Override
            public Color getRowElementForeground(final int rowIndex)
            {
                if (hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(rowIndex))
                {
                    return rowElementForegroundWhenCaretInRow;
                }
                return rowElementForeground;
            }

            @NotNull
            @Override
            public Color getRowBackground(final int rowIndex)
            {
                return IndexUtils.isEven(rowIndex) ? evenRowBackground : oddRowBackground;
            }

            private boolean isCaretRowIndex(final int rowIndex)
            {
                return hexViewer.getCaret().map(caret ->
                {
                    final long caretIndex = caret.getDot();
                    final int caretRowIndex = hexViewer.byteIndexToRowIndex(caretIndex);
                    return rowIndex == caretRowIndex;
                }).orElse(Boolean.FALSE);
            }
        };

        hexViewer.getOffsetArea().setColorProvider(offsetColorProvider);
        hexViewer.getHexArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getHexArea()));
        hexViewer.getTextArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getTextArea()));
    }
}
