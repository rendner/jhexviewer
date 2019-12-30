package example.themes.console;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.HexArea;
import cms.rendner.hexviewer.view.components.areas.bytes.TextArea;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.DefaultBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
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
        hexViewer.getOffsetArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<OffsetArea>(hexViewer.getOffsetArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE);

                    @Override
                    public void paint(@NotNull final Graphics2D g)
                    {
                        super.paint(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }
                })
        );
        hexViewer.getHexArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<HexArea>(hexViewer.getHexArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE);
                    private final Border subSeparator = BorderFactory.createDashedBorder(Color.GRAY, 3.0F, 5.0F);

                    @Override
                    public void paint(@NotNull final Graphics2D g)
                    {
                        super.paint(g);
                        paintMultipleOf8Separator(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }

                    private void paintMultipleOf8Separator(@NotNull final Graphics2D g)
                    {
                        final int bytesPerRow = hexViewer.getBytesPerRow();
                        if (!IndexUtils.isMultipleOf(bytesPerRow, 8))
                        {
                            return;
                        }

                        int leftByteIndex = 8 - 1;
                        final int separatorsToPaint = (bytesPerRow / 8) - 1;
                        for (int i = 0; i < separatorsToPaint; i++)
                        {
                            final Rectangle left = area.getByteRect(leftByteIndex);
                            final Rectangle right = area.getByteRect(leftByteIndex + 1);
                            final int spaceBetween = right.x - (left.x + left.width);
                            subSeparator.paintBorder(area, g, left.x + left.width + spaceBetween / 2, 0, 1, area.getHeight());
                            leftByteIndex += 8;
                        }
                    }
                })
        );
        hexViewer.getTextArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<TextArea>(hexViewer.getTextArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE);

                    @Override
                    public void paint(@NotNull final Graphics2D g)
                    {
                        super.paint(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }
                })
        );
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
            private @NotNull
            final Color rowElementForegroundWhenCaretInRow = new Color(0xFFFFFF);
            private @NotNull
            final Color rowElementForeground = new Color(0xABB6B9);
            private @NotNull
            final Color background = new Color(0x232629);

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
