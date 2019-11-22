package example.themes.console;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.Area;
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
        hexViewer.getOffsetArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<Area<?, ?>>(hexViewer.getOffsetArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.white);

                    @Override
                    public void paint(@NotNull final Graphics2D g)
                    {
                        super.paint(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }
                })
        );
        hexViewer.getHexArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<Area<?, ?>>(hexViewer.getHexArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.white);

                    @Override
                    public void paint(@NotNull final Graphics2D g)
                    {
                        super.paint(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }
                })
        );
        hexViewer.getAsciiArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<Area<?, ?>>(hexViewer.getAsciiArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.white);

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
        final Border insideBorder = BorderFactory.createLineBorder(Color.white);
        hexViewer.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
    }

    @Override
    protected void adjustColorProviders(@NotNull final JHexViewer hexViewer)
    {
        final IOffsetColorProvider offsetColorProvider = new IOffsetColorProvider()
        {
            private @NotNull final Color rowElementForegroundWhenCaretInRow = new Color(0xFFFFFF);
            private @NotNull final Color rowElementForeground = new Color(0xABB6B9);
            private @NotNull final Color background = new Color(0x232629);

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
        hexViewer.getAsciiArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getAsciiArea()));
    }
}
