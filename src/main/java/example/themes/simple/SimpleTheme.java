package example.themes.simple;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.DefaultBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.RowBasedBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import cms.rendner.hexviewer.view.themes.AbstractTheme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Example implementation how to customize the rendering of the {@link JHexViewer}.
 *
 * @author rendner
 */
public class SimpleTheme extends AbstractTheme
{
    @Override
    protected void adjustPainters(@NotNull final JHexViewer hexViewer)
    {
        setAreaBackgroundPainter(hexViewer.getOffsetArea(), new RowBasedBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY);

            @Override
            public void paintRow(@NotNull final RowGraphics rowGraphics, @NotNull final AreaComponent component, boolean isLastRow)
            {
                super.paintRow(rowGraphics, component, isLastRow);
                separator.paintBorder(component, rowGraphics.g, 0, 0, component.getWidth(), component.getRowHeight());
            }
        });
        setAreaBackgroundPainter(hexViewer.getHexArea(), new DefaultBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY);

            @Override
            public void paint(@NotNull final Graphics2D g, @NotNull JHexViewer hexViewer, @NotNull final AreaComponent component)
            {
                super.paint(g, hexViewer, component);
                separator.paintBorder(component, g, 0, 0, component.getWidth(), component.getHeight());
            }
        });
        setAreaBackgroundPainter(hexViewer.getTextArea(), new DefaultBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY);

            @Override
            public void paint(@NotNull final Graphics2D g, @NotNull JHexViewer hexViewer, @NotNull final AreaComponent component)
            {
                super.paint(g, hexViewer, component);
                separator.paintBorder(component, g, 0, 0, component.getWidth(), component.getHeight());
            }
        });
    }

    @Override
    protected void adjustColorProviders(@NotNull final JHexViewer hexViewer)
    {
        final IOffsetColorProvider offsetColorProvider = new IOffsetColorProvider()
        {
            @NotNull
            @Override
            public Color getRowElementForeground(final int rowIndex)
            {
                return hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(rowIndex) ? Color.DARK_GRAY : Color.GRAY;
            }

            @Override
            public @Nullable Color getRowBackground(final int rowIndex)
            {
                return hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(rowIndex) ? Color.WHITE : Color.LIGHT_GRAY;
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
        hexViewer.getHexArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getHexArea())
        {
            @Override
            public @NotNull Color getRowElementForeground(final int byteValue, final long offset, final int rowIndex, final int elementInRowIndex)
            {
                if (isSelected(offset))
                {
                    return IndexUtils.isEven(elementInRowIndex) ? Color.WHITE : Color.BLUE;
                }
                return IndexUtils.isEven(elementInRowIndex) ? Color.GRAY : Color.BLUE;
            }

            private boolean isSelected(final long offset)
            {
                return hexViewer.getCaret()
                        .map(caret -> caret.hasSelection() && caret.getSelectionStart() <= offset && offset <= caret.getSelectionEnd())
                        .orElse(Boolean.FALSE);
            }
        });
        hexViewer.getTextArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getTextArea()));
    }
}
