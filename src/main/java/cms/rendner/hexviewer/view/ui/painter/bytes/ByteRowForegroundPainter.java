package cms.rendner.hexviewer.view.ui.painter.bytes;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.data.wrapper.RowData;
import cms.rendner.hexviewer.common.data.wrapper.RowDataBuilder;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.bytes.IByteRowTemplate;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphicsBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * Paints the foreground of a byte-area rowwise.
 *
 * @author rendner
 */
public final class ByteRowForegroundPainter implements IAreaForegroundPainter
{
    /**
     * The area to be painted by the painter.
     */
    @NotNull
    private final ByteArea area;
    /**
     * The {@link JHexViewer} to which the area to be painted belongs.
     */
    @NotNull
    private final JHexViewer hexViewer;

    /**
     * Updated on every paint call - the font ascent to align the text vertically.
     */
    private int ascent;
    /**
     * Updated on every paint call - used to format the bytes, of the data model of the {@link JHexViewer}, displayed in the area.
     */
    private IValueFormatter valueFormatter;
    /**
     * Updated on every paint call - describes where the bytes should be painted.
     */
    private IByteRowTemplate rowTemplate;
    /**
     * Updated on every paint call - provides colors for rendering the bytes.
     */
    @Nullable
    private IByteColorProvider colorProvider;

    /**
     * Creates a new instance which paints the foreground of the byte-area.
     *
     * @param hexViewer the {@link JHexViewer} to which the area belongs. Used to query additional properties of the {@link JHexViewer}.
     * @param area      the area to be painted by this instance.
     */
    public ByteRowForegroundPainter(@NotNull final JHexViewer hexViewer, @NotNull final ByteArea area)
    {
        super();
        this.hexViewer = hexViewer;
        this.area = area;
    }

    @Override
    public void paint(final @NotNull Graphics2D g)
    {
        rowTemplate = area.getRowTemplate().orElse(null);
        final RowDataBuilder rowDataBuilder = hexViewer.getDataModel()
                .map(dataModel -> new RowDataBuilder(dataModel, hexViewer.getBytesPerRow()))
                .orElse(null);

        final boolean canPaint = rowTemplate != null && rowDataBuilder != null;
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

        rowGraphicsList.forEach(rowGraphics -> {
            final RowData bytes = rowDataBuilder.build(rowGraphics.rowIndex);
            paintRowElementsBackground(rowGraphics, bytes);
            paintRowElementsForeground(rowGraphics, bytes);
            rowGraphics.dispose();
        });
    }

    /**
     * Paints the foreground of the row elements.
     *
     * @param rowGraphics the rowGraphics instance which belongs to the row to paint.
     * @param bytes       the data which should be displayed by the row. This can be less as the number of elements provided by the
     *                             {@link IByteRowTemplate} of the area, because the last row of an area could have less bytes
     *                              to display.
     */
    private void paintRowElementsForeground(@NotNull final RowGraphics rowGraphics, @NotNull final RowData bytes)
    {
        long byteOffset = hexViewer.rowIndexToByteIndex(rowGraphics.rowIndex);

        for (int i = 0; i < bytes.size(); i++)
        {
            final int byteValue = bytes.getByte(i);
            final Element byteElement = rowTemplate.element(i);
            final String byteToDraw = valueFormatter.format(byteValue);

            rowGraphics.g.setColor(getForegroundColor(byteValue, byteOffset, rowGraphics.rowIndex, i));
            rowGraphics.g.drawString(byteToDraw, byteElement.x(), ascent + byteElement.y());

            byteOffset++;
        }
    }

    /**
     * Paints the background of the row elements.
     *
     * @param rowGraphics     the rowGraphics instance which belongs to the row to paint.
     * @param bytes       the data which should be displayed by the row. This can be less as the number of elements provided by the
     *                                  {@link IByteRowTemplate} of the area, because the last row of an area could have less bytes
     *                                   to display.
     */
    private void paintRowElementsBackground(@NotNull final RowGraphics rowGraphics, @NotNull final RowData bytes)
    {
        long byteOffset = hexViewer.rowIndexToByteIndex(rowGraphics.rowIndex);

        for (int i = 0; i < bytes.size(); i++)
        {
            final Color color = getBackgroundColor(bytes.getByte(i), byteOffset, rowGraphics.rowIndex, i);
            if (color != null)
            {
                final Element byteElement = rowTemplate.element(i);

                rowGraphics.g.setColor(color);
                rowGraphics.g.fillRect(byteElement.x(), byteElement.y(), byteElement.width(), byteElement.height());
            }

            byteOffset++;
        }
    }

    @NotNull
    private Color getForegroundColor(final int byteValue, final long offset, final int rowIndex, final int elementInRowIndex)
    {
        Color color = null;
        if (colorProvider != null)
        {
            color = colorProvider.getRowElementForeground(byteValue, offset, rowIndex, elementInRowIndex);
        }
        return color == null ? Color.WHITE : color;
    }

    @Nullable
    private Color getBackgroundColor(final int byteValue, final long offset, final int rowIndex, final int elementInRowIndex)
    {
        if (colorProvider != null)
        {
            return colorProvider.getRowElementBackground(byteValue, offset, rowIndex, elementInRowIndex);
        }

        return null;
    }
}
