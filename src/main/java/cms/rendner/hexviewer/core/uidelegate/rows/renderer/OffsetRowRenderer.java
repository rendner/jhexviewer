package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Default implementation which paints the rows of the offset-area.
 *
 * @author rendner
 */
public class OffsetRowRenderer extends AbstractRowRenderer<IOffsetRowTemplate>
{
    @Override
    public void paintForeground(@NotNull final Graphics g, @NotNull final IOffsetRowTemplate rowTemplate, @NotNull final IRendererContext context)
    {
        final int onlyDigitsCount = rowTemplate.onlyDigitsCount();
        final int ascent = rowTemplate.ascent();
        final IRowData rowData = context.getRowData();
        final IOffsetValueFormatter valueFormatter = context.getHexViewer().getOffsetValueFormatter();
        final String formattedValue = valueFormatter.format(onlyDigitsCount, resolveValueToPaint(context.getHexViewer(), rowData));
        final Element element = rowTemplate.element();

        g.setColor(getElementForegroundColor(context, 0));
        g.drawString(formattedValue, element.x(), ascent + element.y());
    }

    @Override
    public void paintBackground(@NotNull final Graphics g, @NotNull final IOffsetRowTemplate rowTemplate, @NotNull final IRendererContext context)
    {
        paintRowBackground(g, getBackgroundColor(context));
        paintLayoutElementBackground(g, rowTemplate, context);
    }

    /**
     * Paints the backgrounds for the single element defined in the row template.
     *
     * @param g           the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param rowTemplate the layout of the row to be painted.
     * @param context     context which may used to draw customized backgrounds.
     */
    protected void paintLayoutElementBackground(@NotNull final Graphics g, @NotNull final IOffsetRowTemplate rowTemplate, @NotNull final IRendererContext context)
    {
        final Color color = getElementBackgroundColor(context, 0);
        if (color != null)
        {
            final Element element = rowTemplate.element();
            g.setColor(color);
            g.fillRect(element.x(), element.y(), element.width(), element.height());
        }
    }

    /**
     * Returns the value to paint.
     * <p/>
     * If {@link JHexViewer#isShowOffsetCaretIndicator()} returns <code>true</code> and the caret is placed in the row to paint
     * the offset at the byte at the caret will be returned otherwise the offset of the first byte of the row to paint.
     *
     * @param hexViewer the {@link JHexViewer} component.
     * @param rowData   the data of the row to paint.
     * @return the value to paint.
     */
    protected int resolveValueToPaint(@NotNull final JHexViewer hexViewer, @NotNull final IRowData rowData)
    {
        if (hexViewer.isShowOffsetCaretIndicator())
        {
            return hexViewer.getCaret().map(caret ->
            {
                final int caretIndex = caret.getDot().getIndex();
                final int caretRowIndex = hexViewer.byteIndexToRowIndex(caretIndex);
                return rowData.rowIndex() == caretRowIndex ? caretIndex : rowData.offset();
            }).orElseGet(rowData::offset);
        }

        return rowData.offset();
    }
}
