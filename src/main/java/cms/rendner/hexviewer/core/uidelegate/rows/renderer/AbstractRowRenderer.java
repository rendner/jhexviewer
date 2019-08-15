package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;

import java.awt.*;

/**
 * Abstract row renderer which provides utility methods to simplify the usage.
 *
 * @author rendner
 */
public abstract class AbstractRowRenderer<T extends IRowTemplate> implements IRowRenderer<T>
{
    /**
     * The default value to paint the background of a row.
     * This value can be <code>null</code>, to not overwrite the background of the component.
     */
    protected Color defaultRowBackground = Color.white;

    /**
     * The default value to paint the foreground (aka text value) of an element.
     */
    protected Color defaultElementForeground = Color.black;

    /**
     * The default value to paint the background of an element.
     * This value can be <code>null</code>, to not overwrite the color of the row background.
     */
    protected Color defaultElementBackground = null;

    /**
     * Fills the complete row background with a specified color.
     *
     * @param g     the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param color background color to paint, can be <code>null</code>, to not overwrite the current color of the background.
     */
    protected void paintRowBackground(final Graphics g, final Color color)
    {
        if (color != null)
        {
            final Rectangle bounds = g.getClipBounds();
            g.setColor(color);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    /**
     * Fills the background of an element with a specified color.
     *
     * @param g             the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param elementBounds the bounds of the element to paint, is used to fill the background of the element.
     * @param color         background color to paint, can be <code>null</code>, to not overwrite the current color of the background.
     */
    protected void paintElementBackground(final Graphics g, final Rectangle elementBounds, final Color color)
    {
        if (color != null && !elementBounds.isEmpty())
        {
            g.setColor(color);
            g.fillRect(elementBounds.x, elementBounds.y, elementBounds.width, elementBounds.height);
        }
    }

    /**
     * Returns the foreground color of an element to be painted.
     * <p/>
     * The context is checked for an IRowColorProvider instance, if provided the result of the provider is returned.
     * If no IRowColorProvider is found the default element foreground color is used.
     *
     * @param context      context which may provides a preferred foreground color.
     * @param elementIndex the index of the element to paint.
     * @return the foreground color to use, can be <code>null</code>.
     */
    protected Color getElementForegroundColor(final IRendererContext context, final int elementIndex)
    {
        Color result = null;
        final IRowColorProvider provider = context.getColorProvider();
        if (provider != null)
        {
            result = provider.getRowElementForeground(context.getHexViewer(), context.getAreaId(), context.getRowData().rowIndex(), elementIndex);
        }

        return result != null ? result : defaultElementForeground;
    }

    /**
     * Returns the background color of an element to be painted.
     * <p/>
     * The context is checked for an IRowColorProvider instance, if provided the result of the provider is returned.
     * If no IRowColorProvider is found the default element background color is used.
     *
     * @param context      context which may provides a preferred background color.
     * @param elementIndex the index of the element to paint.
     * @return the background color to use, can be <code>null</code>.
     */
    protected Color getElementBackgroundColor(final IRendererContext context, final int elementIndex)
    {
        Color result = null;
        final IRowColorProvider provider = context.getColorProvider();
        if (provider != null)
        {
            result = provider.getRowElementBackground(context.getHexViewer(), context.getAreaId(), context.getRowData().rowIndex(), elementIndex);
        }

        return result != null ? result : defaultElementBackground;
    }

    /**
     * Returns the background color of a row to be painted.
     * <p/>
     * The context is checked for an IRowColorProvider instance, if provided the result of the provider is returned.
     * If no IRowColorProvider is found the default row background color is used.
     *
     * @param context context which may provides a preferred background color.
     * @return the background color to use, can be <code>null</code>.
     */
    protected Color getBackgroundColor(final IRendererContext context)
    {
        Color result = null;
        final IRowColorProvider provider = context.getColorProvider();
        if (provider != null)
        {
            result = provider.getRowBackground(context.getHexViewer(), context.getAreaId(), context.getRowData().rowIndex());
        }

        return result != null ? result : defaultRowBackground;
    }

    /**
     * Paints the backgrounds for all elements defined in an IRowTemplate.
     *
     * @param g           the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param rowTemplate the layout of the row to be painted.
     * @param context     context which may used to draw customized backgrounds.
     */
    protected void paintLayoutElementsBackground(final Graphics g, final IRowTemplate rowTemplate, final IRendererContext context)
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
}
