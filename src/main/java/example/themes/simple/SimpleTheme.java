package example.themes.simple;

import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.DefaultBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.DefaultRowBasedBackgroundPainter;
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
                }
        ));
        hexViewer.getHexArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<Area<?, ?, ?>>(hexViewer.getHexArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray);

                    @Override
                    public void paint(@NotNull Graphics2D g)
                    {
                        super.paint(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }
                }
        ));
        hexViewer.getAsciiArea().getPainter().ifPresent(paintCallback -> paintCallback.setBackgroundPainter(
                new DefaultBackgroundPainter<Area<?, ?, ?>>(hexViewer.getAsciiArea())
                {
                    private final Border separator = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray);

                    @Override
                    public void paint(@NotNull Graphics2D g)
                    {
                        super.paint(g);
                        separator.paintBorder(area, g, 0, 0, area.getWidth(), area.getHeight());
                    }
                }
        ));
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
                return hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(rowIndex) ? Color.darkGray : Color.gray;
            }

            @Override
            public @Nullable Color getRowBackground(final int rowIndex)
            {
                return hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(rowIndex) ? Color.white : Color.lightGray;
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
        hexViewer.getHexArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getHexArea())
        {
            @Override
            public @NotNull Color getRowElementForeground(int offset, int rowIndex, int elementIndex)
            {
                if (isSelected(offset))
                {
                    return IndexUtils.isEven(elementIndex) ? Color.white : Color.blue;
                }
                return IndexUtils.isEven(elementIndex) ? Color.gray : Color.blue;
            }

            private boolean isSelected(final int offset)
            {
                return hexViewer.getCaret()
                        .map(caret -> caret.hasSelection() && caret.getSelectionStart() <= offset && offset <= caret.getSelectionEnd())
                        .orElse(Boolean.FALSE);
            }
        });
        hexViewer.getAsciiArea().setColorProvider(new ByteAreaColorProvider(hexViewer, hexViewer.getAsciiArea()));
    }
}
