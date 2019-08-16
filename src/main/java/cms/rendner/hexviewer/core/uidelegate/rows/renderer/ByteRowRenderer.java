package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.formatter.IValueFormatter;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Default implementation which paints the rows of the byte areas ({@link AreaId#HEX} and {@link AreaId#ASCII}).
 *
 * @author rendner
 */
public class ByteRowRenderer extends AbstractRowRenderer<IByteRowTemplate>
{
    @Override
    public void paintBackground(@NotNull final Graphics g, @NotNull final IByteRowTemplate rowTemplate, @NotNull final IRendererContext context)
    {
        paintRowBackground(g, getBackgroundColor(context));
        paintLayoutElementsBackground(g, rowTemplate, context);
    }

    @Override
    public void paintForeground(@NotNull final Graphics g, @NotNull final IByteRowTemplate rowTemplate, @NotNull final IRendererContext context)
    {
        final IRowData rowData = context.getRowData();

        if (!rowData.isEmpty())
        {
            final int ascent = rowTemplate.ascent();
            final IValueFormatter valueFormatter = getValueFormatter(context);

            for (int i = 0; i < rowData.size(); i++)
            {
                final Color color = getElementForegroundColor(context, i);
                final IElement byteElement = rowTemplate.element(i);
                final String byteToDraw = valueFormatter.format(rowData.getByte(i));

                g.setColor(color);
                g.drawString(byteToDraw, byteElement.x(), ascent + byteElement.y());
            }
        }
    }

    /**
     * Paints the backgrounds for all elements defined in an IRowTemplate.
     *
     * @param g           the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param rowTemplate the layout of the row to be painted.
     * @param context     context which may used to draw customized backgrounds.
     */
    protected void paintLayoutElementsBackground(@NotNull final Graphics g, @NotNull final IByteRowTemplate rowTemplate, @NotNull final IRendererContext context)
    {
        final int elementsToPaint = rowTemplate.elementCount();

        Color startColor;
        Color endColor;

        int startIndex;
        int endIndex;

        int paintIndex = 0;
        while (paintIndex < elementsToPaint)
        {
            startColor = getElementBackgroundColor(context, paintIndex);

            if (startColor == null)
            {
                paintIndex++;
                continue;
            }

            startIndex = paintIndex;
            endIndex = paintIndex;

            while (++paintIndex < elementsToPaint)
            {
                endColor = getElementBackgroundColor(context, paintIndex);

                if (!startColor.equals(endColor))
                {
                    break;
                }

                endIndex = paintIndex;
            }

            final Rectangle elementBounds = rowTemplate.elementBounds(startIndex, endIndex);
            paintElementBackground(g, elementBounds, startColor);
        }
    }

    /**
     * Fills the background of an element with a specified color.
     *
     * @param g             the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param elementBounds the bounds of the element to paint, used to compute the area to fill.
     * @param color         background color to paint, can be <code>null</code>, to not overwrite the current color of the background.
     */
    protected void paintElementBackground(@NotNull final Graphics g, @NotNull final Rectangle elementBounds, @Nullable final Color color)
    {
        if (color != null && !elementBounds.isEmpty())
        {
            g.setColor(color);
            g.fillRect(elementBounds.x, elementBounds.y, elementBounds.width, elementBounds.height);
        }
    }

    /**
     * Returns the matching value formatter depending on the current rendered area.
     *
     * @param context used to retrieve the value formatter.
     * @return the formatter to use.
     */
    protected IValueFormatter getValueFormatter(@NotNull final IRendererContext context)
    {
        if (AreaId.HEX.equals(context.getAreaId()))
        {
            return context.getHexViewer().getHexValueFormatter();
        }

        return context.getHexViewer().getAsciiValueFormatter();
    }
}
