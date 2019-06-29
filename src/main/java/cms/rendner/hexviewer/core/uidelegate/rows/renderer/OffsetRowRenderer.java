package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.caret.ICaret;

import java.awt.*;

/**
 * Default implementation which paints the rows of the offset-area.
 *
 * @author rendner
 */
public class OffsetRowRenderer extends AbstractRowRenderer<IOffsetRowTemplate>
{
    @Override
    public void paintForeground(final Graphics g, final IOffsetRowTemplate rowTemplate, final IRendererContext context)
    {
        final IRowData rowData = context.getRowData();

        if (rowTemplate != null)
        {
            g.setColor(getElementForegroundColor(context, 0));

            final int onlyDigitsCount = rowTemplate.onlyDigitsCount();
            final int ascent = rowTemplate.ascent();
            final IOffsetValueFormatter valueFormatter = context.getHexViewer().getOffsetValueFormatter();

            final int valueToPaint = resolveValueToPaint(context.getHexViewer(), rowData);
            final String formattedValue = valueFormatter.format(onlyDigitsCount, valueToPaint);
            final IElement element = rowTemplate.element(0);
            g.drawString(formattedValue, element.x(), ascent + element.y());
        }
    }

    @Override
    public void paintBackground(final Graphics g, final IOffsetRowTemplate rowTemplate, final IRendererContext context)
    {
        paintRowBackground(g, getBackgroundColor(context));
        paintLayoutElementsBackground(g, rowTemplate, context);
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
    protected int resolveValueToPaint(final JHexViewer hexViewer, final IRowData rowData)
    {
        if (hexViewer.isShowOffsetCaretIndicator())
        {
            final ICaret caret = hexViewer.getCaret();
            if (caret != null)
            {
                final int caretIndex = caret.getDot();
                final int caretRowIndex = hexViewer.byteIndexToRowIndex(caretIndex);
                return rowData.rowIndex() == caretRowIndex ? caretIndex : rowData.offset();
            }
        }

        return rowData.offset();
    }
}
