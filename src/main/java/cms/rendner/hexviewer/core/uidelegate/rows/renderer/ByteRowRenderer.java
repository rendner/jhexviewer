package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.formatter.IValueFormatter;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;

import java.awt.*;

/**
 * Default implementation which paints the rows of the byte areas ({@link AreaId#HEX} and {@link AreaId#ASCII}).
 *
 * @author rendner
 */
public class ByteRowRenderer extends AbstractRowRenderer<IByteRowTemplate>
{
    @Override
    public void paintBackground(final Graphics g, final IByteRowTemplate rowTemplate, final IRendererContext context)
    {
        paintRowBackground(g, getBackgroundColor(context));
        paintLayoutElementsBackground(g, rowTemplate, context);
    }

    @Override
    public void paintForeground(final Graphics g, final IByteRowTemplate rowTemplate, final IRendererContext context)
    {
        final IRowData rowData = context.getRowData();

        if (!rowData.isEmpty())
        {
            final int ascent = rowTemplate.ascent();
            final IValueFormatter valueFormatter = getValueFormatter(context);

            for (int i = 0; i < rowData.size(); i++)
            {
                final Color color = getElementForegroundColor(context, i);
                g.setColor(color);

                final int byteValue = rowData.getByte(i);
                final IElement byteElement = rowTemplate.element(i);
                final String byteToDraw = valueFormatter.format(byteValue);
                g.drawString(byteToDraw, byteElement.x(), ascent + byteElement.y());
            }
        }
    }

    /**
     * Returns the matching value formatter depending on the current rendered area.
     *
     * @param context used to retrieve the value formatter.
     * @return the formatter to use.
     */
    protected IValueFormatter getValueFormatter(final IRendererContext context)
    {
        if (AreaId.HEX.equals(context.getAreaId()))
        {
            return context.getHexViewer().getHexValueFormatter();
        }

        return context.getHexViewer().getAsciiValueFormatter();
    }
}
