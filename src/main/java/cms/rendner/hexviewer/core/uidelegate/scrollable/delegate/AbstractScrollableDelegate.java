package cms.rendner.hexviewer.core.uidelegate.scrollable.delegate;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.swing.scrollable.IScrollableDelegate;
import cms.rendner.hexviewer.swing.scrollable.ScrollableContainer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract implementation of a scroll delegate.
 * <p/>
 * This implementation defines the abstract methods {@link #computeHorizontalUnitIncrement(Rectangle, int)}
 * and {@link #computeVerticalUnitIncrement(Rectangle, int)} which have to be implemented for the sub classes
 * to finish the implementation.
 *
 * @author rendner
 */
public abstract class AbstractScrollableDelegate implements IScrollableDelegate
{
    /**
     * The JHexViewer component.
     */
    protected JHexViewer hexViewer;

    /**
     * The scroll target.
     */
    protected ScrollableContainer viewToScroll;

    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
        this.viewToScroll = hexViewer.getScrollableByteRowsContainer();
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = null;
        this.viewToScroll = null;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        final int visibleRowCount = hexViewer.getPreferredVisibleRowCount();
        final Dimension result = viewToScroll.getPreferredSize();

        if (visibleRowCount > 0)
        {
            final int rowHeight = hexViewer.rowHeight();
            result.height = (visibleRowCount * rowHeight);
        }

        return result;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        if (hexViewer.getPreferredVisibleRowCount() <= 0)
        {
            return true;
        }

        Container parent = SwingUtilities.getUnwrappedParent(viewToScroll);
        return parent instanceof JViewport && parent.getWidth() > viewToScroll.getPreferredSize().width;

    }

    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        if (hexViewer.getPreferredVisibleRowCount() <= 0)
        {
            return true;
        }

        Container parent = SwingUtilities.getUnwrappedParent(viewToScroll);
        return parent instanceof JViewport && parent.getHeight() > viewToScroll.getPreferredSize().height;

    }

    /**
     * Returns the distance to scroll to completely expose the next or previous
     * row (for vertical scrolling) or char (for horizontal scrolling).
     *
     * @param visibleRect the view area visible within the viewport.
     * @param orientation {@code SwingConstants.HORIZONTAL} or {@code SwingConstants.VERTICAL}.
     * @param direction   less or equal to zero to scroll up/back, greater than zero for down/forward.
     * @return the "unit" increment for scrolling in the specified direction. This value should always be positive.
     */
    public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction)
    {
        if (orientation == SwingConstants.HORIZONTAL)
        {
            return computeHorizontalUnitIncrement(visibleRect, direction);
        }
        else
        {
            return computeVerticalUnitIncrement(visibleRect, direction);
        }
    }

    /**
     * Returns the distance to scroll to completely expose the next or previous
     * block of rows (for vertical scrolling) or chars (for horizontal scrolling).
     *
     * @param visibleRect the view area visible within the viewport.
     * @param orientation {@code SwingConstants.HORIZONTAL} or {@code SwingConstants.VERTICAL}.
     * @param direction   less or equal to zero to scroll up/back, greater than zero for down/forward.
     * @return the "unit" increment for scrolling in the specified direction. This value should always be positive.
     */
    public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction)
    {
        if (orientation == SwingConstants.HORIZONTAL)
        {
            return visibleRect.width - computeHorizontalUnitIncrement(visibleRect, direction);
        }
        else
        {
            return visibleRect.height - computeVerticalUnitIncrement(visibleRect, direction);
        }
    }

    /**
     * Computes the distance to scroll to completely expose the next or previous char.
     *
     * @param visibleRect the view area visible within the viewport.
     * @param direction   less or equal to zero to scroll back, greater than zero for forward.
     * @return the "unit" increment for scrolling in the specified direction. This value should always be positive.
     */
    protected abstract int computeHorizontalUnitIncrement(final Rectangle visibleRect, final int direction);

    /**
     * Computes the distance to scroll to completely expose the next or previous row.
     *
     * @param visibleRect the view area visible within the viewport.
     * @param direction   less or equal to zero to scroll up, greater than zero for down.
     * @return the "unit" increment for scrolling in the specified direction. This value should always be positive.
     */
    protected abstract int computeVerticalUnitIncrement(final Rectangle visibleRect, final int direction);
}
