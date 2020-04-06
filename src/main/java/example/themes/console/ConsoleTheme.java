package example.themes.console;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.painter.middleground.DefaultMiddlegroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.DefaultBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import cms.rendner.hexviewer.view.themes.AbstractTheme;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Example implementation how to customize the look of the {@link JHexViewer}.
 *
 * @author rendner
 */
public class ConsoleTheme extends AbstractTheme
{
    @Override
    protected void adjustPainters(@NotNull final JHexViewer hexViewer)
    {
        setAreaBackgroundPainter(hexViewer.getOffsetArea(), new DefaultBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE);

            @Override
            public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
            {
                super.paint(g, hexViewer, component);
                separator.paintBorder(component, g, 0, 0, component.getWidth(), component.getHeight());
            }
        });
        setAreaBackgroundPainter(hexViewer.getHexArea(), new DefaultBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE);

            @Override
            public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
            {
                super.paint(g, hexViewer, component);
                separator.paintBorder(component, g, 0, 0, component.getWidth(), component.getHeight());
            }
        });
        setAreaMiddlegroundPainter(hexViewer.getHexArea(), new DefaultMiddlegroundPainter()
        {
            private final Border dashedSeparator = BorderFactory.createDashedBorder(Color.LIGHT_GRAY, 3.0F, 5.0F);

            @Override
            public void paint(final @NotNull Graphics2D g, final @NotNull JHexViewer hexViewer, final @NotNull AreaComponent component)
            {
                super.paint(g, hexViewer, component);
                paintMultipleOf4Separator(g, component);
            }

            private void paintMultipleOf4Separator(@NotNull final Graphics2D g, @NotNull final AreaComponent component)
            {
                final int multipleCount = 4;
                final int bytesPerRow = hexViewer.getBytesPerRow();
                if (!IndexUtils.isMultipleOf(bytesPerRow, multipleCount))
                {
                    return;
                }

                final ByteArea area = (ByteArea) component;
                int leftByteIndex = multipleCount - 1;
                final int separatorsToPaint = (bytesPerRow / multipleCount) - 1;
                for (int i = 0; i < separatorsToPaint; i++)
                {
                    final Rectangle left = area.getByteRect(leftByteIndex);
                    final Rectangle right = area.getByteRect(leftByteIndex + 1);
                    final int spaceBetween = right.x - (left.x + left.width);
                    dashedSeparator.paintBorder(component, g, left.x + left.width + spaceBetween / 2, 0, 1, component.getHeight());
                    leftByteIndex += multipleCount;
                }
            }
        });
        setAreaBackgroundPainter(hexViewer.getTextArea(), new DefaultBackgroundPainter()
        {
            private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE);

            @Override
            public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
            {
                super.paint(g, hexViewer, component);
                separator.paintBorder(component, g, 0, 0, component.getWidth(), component.getHeight());
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
            private final Color rowElementForegroundWhenCaretInRow = new Color(0xFFFFFF);
            private final Color rowElementForeground = new Color(0xABB6B9);
            private final Color background = new Color(0x232629);

            @NotNull
            @Override
            public Color getRowElementForeground(final int rowIndex)
            {
                return hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(rowIndex) ? rowElementForegroundWhenCaretInRow : rowElementForeground;
            }

            @NotNull
            @Override
            public Color getBackground()
            {
                return background;
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
