package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    protected Color defaultRowBackground = Color.white;

    /**
     * The default value to paint the foreground (aka text value) of an element.
     */
    @NotNull
    protected Color defaultElementForeground = Color.black;

    /**
     * The default value to paint the background of an element.
     * This value can be <code>null</code>, to not overwrite the color of the row background.
     */
    @Nullable
    protected Color defaultElementBackground = null;

    /**
     * Fills the complete row background with a specified color.
     *
     * @param g     the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     * @param color background color to paint, can be <code>null</code>, to not overwrite the current color of the background.
     */
    protected void paintRowBackground(@NotNull final Graphics g, @Nullable final Color color)
    {
        if (color != null)
        {
            final Rectangle bounds = g.getClipBounds();
            g.setColor(color);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
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
     * @return the foreground color to use.
     */
    @NotNull
    protected Color getElementForegroundColor(@NotNull final IRendererContext context, final int elementIndex)
    {
        final IRowColorProvider provider = context.getColorProvider();
        final Color color = provider.getRowElementForeground(context.getHexViewer(), context.getAreaId(), context.getRowData().rowIndex(), elementIndex);
        return color != null ? color : defaultElementForeground;
    }

    /**
     * Returns the background color of an element to be painted.
     * <p/>
     * The context is checked for an IRowColorProvider instance, if provided the result of the provider is returned.
     * If no IRowColorProvider is found the default element background color is used.
     *
     * @param context      context which may provides a preferred background color.
     * @param elementIndex the index of the element to paint.
     * @return the background color to use, or <code>null</code> to not overwrite the current color of the background.
     */
    @Nullable
    protected Color getElementBackgroundColor(@NotNull final IRendererContext context, final int elementIndex)
    {
        final IRowColorProvider provider = context.getColorProvider();
        final Color color = provider.getRowElementBackground(context.getHexViewer(), context.getAreaId(), context.getRowData().rowIndex(), elementIndex);
        return color != null ? color : defaultElementBackground;
    }

    /**
     * Returns the background color of a row to be painted.
     * <p/>
     * The context is checked for an IRowColorProvider instance, if provided the result of the provider is returned.
     * If no IRowColorProvider is found the default row background color is used.
     *
     * @param context context which may provides a preferred background color.
     * @return the background color to use, or <code>null</code> to not overwrite the current color of the background.
     */
    @Nullable
    protected Color getBackgroundColor(@NotNull final IRendererContext context)
    {
        final IRowColorProvider provider = context.getColorProvider();
        final Color color = provider.getRowBackground(context.getHexViewer(), context.getAreaId(), context.getRowData().rowIndex());
        return color != null ? color : defaultRowBackground;
    }
}
