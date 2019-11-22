package cms.rendner.hexviewer.view.components.highlighter;

import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a highlighter.
 *
 * @author rendner
 */
public abstract class AbstractHighlighter implements IHighlighter
{
    /**
     * List of highlights created by this highlighter instance.
     */
    @NotNull
    protected List<IHighlight> highlights = new ArrayList<>();

    /**
     * The component to which this highlighter belongs.
     */
    protected JHexViewer hexViewer;

    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        removeAllHighlights();
        this.hexViewer = null;
    }

    @Override
    public void removeHighlight(@NotNull final IHighlight highlight)
    {
        highlights.remove(highlight);
        damageBytes(highlight.getStartOffset(), highlight.getEndOffset());
    }

    @Override
    public void removeHighlights(@NotNull final List<IHighlight> highlightsToRemove)
    {
        highlightsToRemove.forEach(this::removeHighlight);
    }

    @Override
    public void removeAllHighlights()
    {
        if (!highlights.isEmpty())
        {
            final List<IHighlight> highlightsToRemove = highlights;
            highlights = new ArrayList<>();
            removeHighlights(highlightsToRemove);
        }
    }

    @Override
    public int getHighlightsCount()
    {
        return highlights.size();
    }

    @Override
    public boolean hasHighlights()
    {
        return !highlights.isEmpty();
    }

    @NotNull
    @Override
    public List<IHighlight> getHighlights()
    {
        return new ArrayList<>(highlights);
    }

    /**
     * Damages a range of bytes in all byte-areas.
     * The IDamager bound to the associated JHexViewer is used to damage the areas.
     *
     * @param start the start of the byte range which should be damaged.
     * @param end   the end of the byte range which should be damaged.
     */
    protected void damageBytes(final long start, final long end)
    {
        hexViewer.getDamager().ifPresent(damager -> damager.damageBytes(start, end));
    }
}
