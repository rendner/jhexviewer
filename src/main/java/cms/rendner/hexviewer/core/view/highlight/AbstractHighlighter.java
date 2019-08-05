package cms.rendner.hexviewer.core.view.highlight;

import cms.rendner.hexviewer.core.JHexViewer;
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
     * List of highlights.
     */
    @NotNull
    protected List<IHighlight> highlights = new ArrayList<>();

    /**
     * The highlight which represents the current selected bytes.
     */
    protected IHighlight selectionHighlight;

    /**
     * Indicates if the selection should be painted behind or over the highlights.
     */
    protected boolean paintSelectionBehindHighlights = true;

    /**
     * The hexViewer to which this highlighter belongs.
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
    public boolean getPaintSelectionBehindHighlights()
    {
        return paintSelectionBehindHighlights;
    }

    @Override
    public void setPaintSelectionBehindHighlights(final boolean value)
    {
        if (paintSelectionBehindHighlights != value)
        {
            paintSelectionBehindHighlights = value;

            if (selectionHighlight != null)
            {
                damageBytes(selectionHighlight.getStartOffset(), selectionHighlight.getEndOffset());
            }
        }
    }

    @Override
    public void removeHighlight(@NotNull final IHighlight highlight)
    {
        if (highlight.equals(selectionHighlight))
        {
            selectionHighlight = null;
        }
        else
        {
            highlights.remove(highlight);
        }

        damageBytes(highlight.getStartOffset(), highlight.getEndOffset());
    }

    @Override
    public void removeHighlights(@NotNull final List<IHighlight> highlightsToRemove)
    {
        for (final IHighlight highlight : highlightsToRemove)
        {
            removeHighlight(highlight);
        }

        if(highlightsToRemove.contains(selectionHighlight))
        {
            selectionHighlight = null;
        }
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

        selectionHighlight = null;
    }

    @Override
    public int getHighlightsCount()
    {
        return highlights.size();
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
     * @param start the start of the byte range which should be damaged.
     * @param end   the end of the byte range which should be damaged.
     */
    protected void damageBytes(final int start, final int end)
    {
        hexViewer.getDamager().ifPresent(damager -> damager.damageBytes(start, end));
    }
}
