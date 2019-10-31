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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Paints the rows foreground of a byte-area.
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
     * Temporary value - used to format the bytes, of the data model of the {@link JHexViewer}, displayed in the area.
     */
    private IValueFormatter valueFormatter;
    /**
     * Temporary value - describes where the bytes should be painted.
     */
    private IByteRowTemplate rowTemplate;
    /**
     * Temporary value - provides colors for rendering the bytes.
     */
    private IByteColorProvider colorProvider;
    /**
     * Temporary value - produces a list of bytes which should be displayed in a specific row.
     */
    private RowDataBuilder rowDataBuilder;

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
    public void prePaint()
    {
        rowTemplate = area.getRowTemplate().orElse(null);
        valueFormatter = area.getValueFormatter();
        colorProvider = area.getColorProvider().orElse(null);
        hexViewer.getDataModel().ifPresent(dataModel -> rowDataBuilder = new RowDataBuilder(dataModel, hexViewer.getBytesPerRow()));
    }

    @Override
    public boolean canPaint()
    {
        return rowTemplate != null && rowDataBuilder != null;
    }

    @Override
    public void postPaint()
    {
        rowTemplate = null;
        valueFormatter = null;
        colorProvider = null;
        rowDataBuilder = null;
    }

    @Override
    public void paint(final @NotNull Graphics2D g, final int rowIndex)
    {
        paintRowElementsBackground(g, rowIndex);
        paintRowElementsForeground(g, rowIndex);
    }

    /**
     * Paints the foreground of the row elements.
     *
     * @param g        the Graphics2D object to paint into.
     * @param rowIndex the index of the row to paint.
     */
    protected void paintRowElementsForeground(@NotNull final Graphics2D g, final int rowIndex)
    {
        final int ascent = rowTemplate.fontMetrics().getAscent();
        final RowData bytes = rowDataBuilder.build(rowIndex);

        int byteOffset = hexViewer.rowIndexToByteIndex(rowIndex);

        for (int i = 0; i < bytes.size(); i++)
        {
            final Element byteElement = rowTemplate.element(i);
            final String byteToDraw = valueFormatter.format(bytes.getByte(i));

            g.setColor(getForegroundColor(byteOffset, rowIndex, i));
            g.drawString(byteToDraw, byteElement.x(), ascent + byteElement.y());

            byteOffset++;
        }
    }

    /**
     * Paints the background of the row elements.
     *
     * @param g        the Graphics2D object to paint into.
     * @param rowIndex the index of the row to paint.
     */
    protected void paintRowElementsBackground(@NotNull final Graphics2D g, final int rowIndex)
    {
        int byteOffset = hexViewer.rowIndexToByteIndex(rowIndex);

        final int offsetOfLastByteInRow = Math.min(hexViewer.getLastPossibleByteIndex(), (byteOffset + hexViewer.getBytesPerRow() - 1));
        final int bytesToPaint = 1 + (offsetOfLastByteInRow - byteOffset);

        for (int i = 0; i < bytesToPaint; i++)
        {
            final Color color = getBackgroundColor(byteOffset, rowIndex, i);
            if (color != null)
            {
                final Element byteElement = rowTemplate.element(i);

                g.setColor(color);
                g.fillRect(byteElement.x(), byteElement.y(), byteElement.width(), byteElement.height());
            }

            byteOffset++;
        }
    }

    @NotNull
    private Color getForegroundColor(final int offset, final int rowIndex, final int elementIndex)
    {
        Color color = null;
        if (colorProvider != null)
        {
            color = colorProvider.getRowElementForeground(offset, rowIndex, elementIndex);
        }
        return color == null ? Color.white : color;
    }

    @Nullable
    private Color getBackgroundColor(final int offset, final int rowIndex, final int elementIndex)
    {
        if (colorProvider != null)
        {
            return colorProvider.getRowElementBackground(offset, rowIndex, elementIndex);
        }

        return null;
    }
}
