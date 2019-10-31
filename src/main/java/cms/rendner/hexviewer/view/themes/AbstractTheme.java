package cms.rendner.hexviewer.view.themes;

import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

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
}