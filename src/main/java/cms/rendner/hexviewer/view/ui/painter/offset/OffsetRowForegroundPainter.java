package cms.rendner.hexviewer.view.ui.painter.offset;

import cms.rendner.hexviewer.common.data.formatter.offset.IOffsetFormatter;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaLayerPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphicsBuilder;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * Paints the foreground of an offset-area row-wise.
 *
 * @author rendner
 */
public final class OffsetRowForegroundPainter implements IAreaLayerPainter
{
    /**
     * Updated on every paint call - the font ascent to align the text vertically.
     */
    private int ascent;
    /**
     * Updated on every paint call - used to format the bytes, of the data model of the {@link JHexViewer}, displayed in the area.
     */
    private IOffsetFormatter valueFormatter;
    /**
     * Updated on every paint call - provides colors for rendering the offset addresses.
     */
    private IOffsetColorProvider colorProvider;

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {

        final OffsetArea area = (OffsetArea) component;
        final IOffsetRowTemplate rowTemplate = area.getRowTemplate();

        final boolean canPaint = rowTemplate != null;
        if (!canPaint)
        {
            return;
        }

        applyRenderingHints(g);
        final List<RowGraphics> rowGraphicsList = RowGraphicsBuilder.buildForegroundRowGraphics(g, component);
        if (rowGraphicsList.isEmpty())
        {
            return;
        }

        valueFormatter = area.getValueFormatter();
        ascent = rowTemplate.fontMetrics().getAscent();
        colorProvider = area.getColorProvider();

        final Element element = rowTemplate.element();

        rowGraphicsList.forEach(rowGraphics ->
        {
            paintRowElementBackground(rowGraphics, element);
            paintRowElementForeground(rowGraphics, hexViewer, element);
            rowGraphics.dispose();
        });
    }

    /**
     * Sets rendering hints to allow to control the rendering quality and overall time/quality trade-off in the rendering process.
     * Refer to the RenderingHints class for definitions of some common keys and values.
     *
     * @param g the Graphics2D context to be modified.
     * @see RenderingHints
     */
    private void applyRenderingHints(@NotNull final Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * Paints the background of an row element.
     *
     * @param rowGraphics the rowGraphics instance which refers to the row to paint.
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
     * @param rowGraphics the rowGraphics instance which refers to the row to paint.
     * @param hexViewer   the JHexViewer to which the offset-area belongs.
     * @param element     the element to paint.
     */
    private void paintRowElementForeground(@NotNull final RowGraphics rowGraphics, @NotNull final JHexViewer hexViewer, @NotNull final Element element)
    {
        final long value = rowIndexToOffset(hexViewer, rowGraphics.rowIndex);
        final String formattedValue = valueFormatter.format(value);

        rowGraphics.g.setColor(getForegroundColor(rowGraphics.rowIndex));
        rowGraphics.g.drawString(formattedValue, element.x(), ascent + element.y());
    }

    /**
     * Returns the offset to be rendered for a row index.
     *
     * @param rowIndex  the index of the row to paint.
     * @param hexViewer the JHexViewer to which the offset-area belongs.
     * @return the offset of the {@link cms.rendner.hexviewer.view.components.caret.ICaret caret} if the offset should
     * be displayed and the row contains the caret, otherwise the index of the row.
     */
    private long rowIndexToOffset(@NotNull final JHexViewer hexViewer, final int rowIndex)
    {
        final long offset = hexViewer.rowIndexToByteIndex(rowIndex);

        if (hexViewer.isShowOffsetCaretIndicator())
        {
            return hexViewer.getCaret().map(caret ->
            {
                final long caretIndex = caret.getDot();
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
        return color == null ? Color.WHITE : color;
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
