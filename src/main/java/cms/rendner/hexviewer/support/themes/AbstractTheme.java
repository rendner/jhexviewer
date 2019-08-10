package cms.rendner.hexviewer.support.themes;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract theme which adds some base methods to separate the modifications.
 * Sub classes can implement some of the provided empty methods to alter the JHexViewer.
 *
 * @author rendner
 */
public abstract class AbstractTheme implements ITheme
{
    @Override
    public void applyTo(@NotNull final JHexViewer hexViewer)
    {
        customizeBackground(hexViewer);
        applyBorders(hexViewer);
        applyRowColorProvider(hexViewer);

        hexViewer.getCaret().ifPresent(this::customizeCaret);
        hexViewer.getHighlighter().ifPresent(this::customizeHighlighter);
    }

    /**
     * Modifies the background of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void customizeBackground(@NotNull final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies the highlighter of the {@link JHexViewer}.
     * This method is only called if the JHexViewer has a highlighter installed.
     *
     * @param highlighter the highlighter used in the JHexViewer.
     */
    protected void customizeHighlighter(@NotNull final IHighlighter highlighter)
    {
    }

    /**
     * Modifies the caret of the {@link JHexViewer}.
     * This method is only called if the JHexViewer has a caret installed.
     *
     * @param caret the caret used in the JHexViewer.
     */
    protected void customizeCaret(@NotNull final ICaret caret)
    {
    }

    /**
     * Modifies the row color provider.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @see JHexViewer#setRowColorProvider(AreaId, IRowColorProvider)
     */
    protected void applyRowColorProvider(@NotNull final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies the borders of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void applyBorders(@NotNull final JHexViewer hexViewer)
    {
    }
}