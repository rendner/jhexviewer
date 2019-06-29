package cms.rendner.hexviewer.support.themes;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;

/**
 * Abstract theme which adds some base methods to separate the modifications.
 * Sub classes can implement some of the provided empty methods to alter the JHexViewer.
 *
 * @author rendner
 */
public abstract class AbstractTheme implements ITheme
{
    @Override
    public void applyTo(final JHexViewer hexViewer)
    {
        customizeBackground(hexViewer);
        applyBorders(hexViewer);
        applyRowColorProvider(hexViewer);

        final ICaret caret = hexViewer.getCaret();
        if (caret != null)
        {
            customizeCaret(caret);
        }

        final IHighlighter highlighter = hexViewer.getHighlighter();
        if (highlighter != null)
        {
            customizeHighlighter(highlighter);
        }
    }

    /**
     * Modifies the background of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void customizeBackground(final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies the highlighter of the {@link JHexViewer}.
     *
     * @param highlighter the highlighter used in the JHexViewer, never <code>null</code>.
     */
    protected void customizeHighlighter(final IHighlighter highlighter)
    {
    }

    /**
     * Modifies the caret of the {@link JHexViewer}.
     *
     * @param caret the caret used in the JHexViewer, never <code>null</code>.
     */
    protected void customizeCaret(final ICaret caret)
    {
    }

    /**
     * Modifies the row color provider.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @see JHexViewer#setRowColorProvider(AreaId, IRowColorProvider)
     */
    protected void applyRowColorProvider(final JHexViewer hexViewer)
    {
    }

    /**
     * Modifies the borders of the {@link JHexViewer}.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    protected void applyBorders(final JHexViewer hexViewer)
    {
    }
}