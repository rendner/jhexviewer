package cms.rendner.hexviewer.view.themes;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract theme which adds some base methods to separate the modifications.
 * Sub classes can implement some of the provided empty methods to alter the look of the {@link JHexViewer}.
 *
 * @author rendner
 */
public abstract class AbstractTheme implements ITheme
{
    @Override
    public void applyTo(@NotNull final JHexViewer hexViewer)
    {
        adjustPainters(hexViewer);
        adjustColorProviders(hexViewer);
        adjustRowLayouts(hexViewer);
        adjustComponentDefaults(hexViewer);
    }

    /**
     * Modifies the painters of the areas displayed by the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void adjustPainters(@NotNull final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies the color provider of the areas of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void adjustColorProviders(@NotNull final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies the row-template configuration of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void adjustRowLayouts(@NotNull final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies some basic swing component defaults like border or background of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void adjustComponentDefaults(@NotNull final JHexViewer hexViewer)
    {
    }

    /**
     * Sets the painter responsible for painting the background of the component.
     *
     * @param component the component.
     * @param painter   the painter to set.
     */
    protected void setAreaPainter(@NotNull final AreaComponent component, @Nullable final IAreaBackgroundPainter painter)
    {
        final IAreaPainter areaPainter = component.getPainter();
        if (areaPainter != null)
        {
            areaPainter.setBackgroundPainter(painter);
            component.repaint();
        }
    }

    /**
     * Sets the painter responsible for painting the foreground of the component.
     *
     * @param component the component.
     * @param painter   the painter to set.
     */
    protected void setAreaPainter(@NotNull final AreaComponent component, @Nullable final IAreaForegroundPainter painter)
    {
        final IAreaPainter areaPainter = component.getPainter();
        if (areaPainter != null)
        {
            areaPainter.setForegroundPainter(painter);
            component.repaint();
        }
    }
}