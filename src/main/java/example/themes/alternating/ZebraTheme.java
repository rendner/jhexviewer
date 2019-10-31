package example.themes.alternating;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.DefaultRowBasedBackgroundPainter;
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
        hexViewer.getOffsetArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultRowBasedBackgroundPainter<Area<?, ?, ?>>(hexViewer.getOffsetArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray);

                    @Override
                    public void paint(@NotNull Graphics2D g, int rowIndex, boolean isLastRow)
                    {
                        super.paint(g, rowIndex, isLastRow);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getRowHeight());
                    }
                })
        );
        hexViewer.getHexArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultRowBasedBackgroundPainter<Area<?, ?, ?>>(hexViewer.getHexArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray);

                    @Override
                    public void paint(@NotNull Graphics2D g, int rowIndex, boolean isLastRow)
                    {
                        super.paint(g, rowIndex, isLastRow);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getRowHeight());
                    }
                })
        );
        hexViewer.getAsciiArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultRowBasedBackgroundPainter<Area<?, ?, ?>>(hexViewer.getAsciiArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray);

                    @Override
                    public void paint(@NotNull Graphics2D g, int rowIndex, boolean isLastRow)
                    {
                        super.paint(g, rowIndex, isLastRow);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getRowHeight());
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
            private @NotNull
            final Color rowElementForegroundWhenCaretInRow = Color.black;
            private @NotNull
            final Color rowElementForeground = new Color(0x999999);
            private @NotNull
            final Color evenRowBackground = new Color(0xE8E8E8);
            private @NotNull
            final Color oddRowBackground = new Color(0xCFCFC4);

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
            public Color getRowBackground(int rowIndex)
            {
                return IndexUtils.isEven(rowIndex) ? evenRowBackground : oddRowBackground;
            }

            private boolean isCaretRowIndex(final int rowIndex)
            {
                return hexViewer.getCaret().map(caret ->
                {
                    final int caretIndex = caret.getDot();
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
