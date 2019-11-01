package cms.rendner.hexviewer.view.ui.painter.offset;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphicsBuilder;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * Paints the foreground of an offset-area rowwise.
 *
 * @author rendner
 */
public final class OffsetRowForegroundPainter implements IAreaForegroundPainter
{
    /**
     * The {@link JHexViewer} to which the area to be painted belongs.
     */
    @NotNull
    private final JHexViewer hexViewer;

    /**
     * The area to be painted by the painter.
     */
    @NotNull
    private final OffsetArea area;

    /**
     * Updated on every paint call - the font ascent to align the text vertically.
     */
    private int ascent;
    /**
     * Updated on every paint call - used to format the bytes, of the data model of the {@link JHexViewer}, displayed in the area.
     */
    private IValueFormatter valueFormatter;
    /**
     * Updated on every paint call - provides colors for rendering the offset addresses.
     */
    private IOffsetColorProvider colorProvider;

    /**
     * Creates a new instance which paints the foreground of the offset-area.
     *
     * @param hexViewer the {@link JHexViewer} to which the area belongs. Used to query additional properties of the {@link JHexViewer}.
     */
    public OffsetRowForegroundPainter(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
        area = hexViewer.getOffsetArea();
    }

    @Override
    public void paint(@NotNull final Graphics2D g)
    {
        final IOffsetRowTemplate rowTemplate = area.getRowTemplate().orElse(null);

        final boolean canPaint = rowTemplate != null;
        if (!canPaint)
        {
            return;
        }

        final List<RowGraphics> rowGraphicsList = RowGraphicsBuilder.buildForegroundRowGraphics(g, area);
        if (rowGraphicsList.isEmpty())
        {
            return;
        }

        valueFormatter = area.getValueFormatter();
        ascent = rowTemplate.fontMetrics().getAscent();
        colorProvider = area.getColorProvider().orElse(null);

        final Element element = rowTemplate.element();

        rowGraphicsList.forEach(rowGraphics ->
        {
            paintRowElementBackground(rowGraphics, element);
            paintRowElementForeground(rowGraphics, element);
            rowGraphics.dispose();
        });
    }

    /**
     * Paints the background of an row element.
     *
     * @param rowGraphics the rowGraphics instance which belongs to the row to paint.
     * @param element     the element to paint.
     */
    private void paintRowElementBackground(@NotNull final RowGraphics rowGraphics, @NotNull final Element element)
    {
        final Color backgroundColor = getBackgroundColor(rowGraphics.rowIndex);
        if (backgroundColor != null)
        {
            rowGraphics.g.setColor(backgroundColor);
            rowGraphics.g.fillRect(element.x(), element.y(), element.width(), element.height());
        }
    }

    /**
     * Paints the foreground of an row element.
     *
     * @param rowGraphics the rowGraphics instance which belongs to the row to paint.
     * @param element     the element to paint.
     */
    private void paintRowElementForeground(@NotNull final RowGraphics rowGraphics, @NotNull final Element element)
    {
        final int value = rowIndexToOffset(rowGraphics.rowIndex);
        final String formattedValue = valueFormatter.format(value);

        rowGraphics.g.setColor(getForegroundColor(rowGraphics.rowIndex));
        rowGraphics.g.drawString(formattedValue, element.x(), ascent + element.y());
    }

    /**
     * Returns the offset to be rendered for a row index.
     *
     * @param rowIndex the index of the row to paint.
     * @return the offset of the {@link cms.rendner.hexviewer.view.components.caret.ICaret caret} if the offset should
     * be displayed and the row contains the caret, otherwise the index of the row.
     */
    private int rowIndexToOffset(final int rowIndex)
    {
        final int offset = hexViewer.rowIndexToByteIndex(rowIndex);

        if (hexViewer.isShowOffsetCaretIndicator())
        {
            return hexViewer.getCaret().map(caret ->
            {
                final int caretIndex = caret.getDot();
                final int caretRowIndex = hexViewer.byteIndexToRowIndex(caretIndex);
                return rowIndex == caretRowIndex ? caretIndex : offset;
            }).orElse(offset);
        }

        return offset;
    }

    @NotNull
    private Color getForegroundColor(final int rowIndex)
    {
        Color color = null;
        if (colorProvider != null)
        {
            color = colorProvider.getRowElementForeground(rowIndex);
        }
        return color == null ? Color.white : color;
    }

    @Nullable
    private Color getBackgroundColor(final int rowIndex)
    {
        if (colorProvider != null)
        {
            return colorProvider.getRowElementBackground(rowIndex);
        }
        return null;
    }
}
