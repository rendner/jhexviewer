package cms.rendner.hexviewer.view.ui.painter.offset;

import cms.rendner.hexviewer.common.data.formatter.offset.IOffsetFormatter;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Paints the rows foreground of an offset-area.
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
     * Temporary value - used to format the the offset addresses displayed in the area.
     */
    private IOffsetFormatter valueFormatter;
    /**
     * Temporary value - describes where the offset addresses should be painted.
     */
    private IOffsetRowTemplate rowTemplate;
    /**
     * Temporary value - provides colors for rendering the offset addresses.
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
    public void prePaint()
    {
        rowTemplate = area.getRowTemplate().orElse(null);
        colorProvider = area.getColorProvider().orElse(null);
        valueFormatter = area.getValueFormatter();
    }

    @Override
    public boolean canPaint()
    {
        return rowTemplate != null && valueFormatter != null;
    }

    @Override
    public void postPaint()
    {
        rowTemplate = null;

        valueFormatter = null;
        colorProvider = null;
    }

    @Override
    public void paint(@NotNull final Graphics2D g, final int rowIndex)
    {
        final Element element = rowTemplate.element();

        final Color backgroundColor = getBackgroundColor(rowIndex);
        if (backgroundColor != null)
        {
            g.setColor(backgroundColor);
            g.fillRect(element.x(), element.y(), element.width(), element.height());
        }

        final int ascent = rowTemplate.fontMetrics().getAscent();
        final int value = rowIndexToOffset(rowIndex);
        final String formattedValue = valueFormatter.format(value);

        g.setColor(getForegroundColor(rowIndex));
        g.drawString(formattedValue, element.x(), ascent + element.y());
    }

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
