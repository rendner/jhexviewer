package cms.rendner.hexviewer.swing.scrollable;

import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Optional;

/**
 * Rows container which allows to customize the scrolling behaviour of the content.
 *
 * @author rendner
 */
public final class ScrollableRowsContainer extends ScrollableContainer
{
    /**
     * The number of preferred visible rows displayed in the JHexViewer.
     */
    private int preferredVisibleRowCount = 8;

    /**
     * The component forwards all "questions about how to scroll" to this delegate.
     */
    @Nullable
    private IScrollableDelegate scrollableDelegate;

    /**
     * Returns the number of rows which should be displayed.
     *
     * @return number of trows &gt;= 0.
     */
    public int getPreferredVisibleRowCount()
    {
        return preferredVisibleRowCount;
    }

    /**
     * Sets the number of preferred rows which should be displayed.
     *
     * @param newValue number of rows, &gt;= 0.
     */
    public void setPreferredVisibleRowCount(final int newValue)
    {
        CheckUtils.checkMinValue(newValue, 0);

        if (newValue != preferredVisibleRowCount)
        {
            preferredVisibleRowCount = newValue;
            revalidate();
            repaint();
        }
    }

    /**
     * @return the installed scroll delegate, can be <code>null</code> if no delegate was installed.
     */
    @NotNull
    public Optional<IScrollableDelegate> getScrollableDelegate()
    {
        return Optional.ofNullable(scrollableDelegate);
    }

    /**
     * Sets a scroll delegate.
     * This allows to overwrite the default scroll behaviour of this component.
     *
     * @param newScrollableDelegate the new scroll delegate, can be <code>null</code>
     */
    public void setScrollableDelegate(@Nullable final IScrollableDelegate newScrollableDelegate)
    {
        scrollableDelegate = newScrollableDelegate;
        revalidate();
    }

    @NotNull
    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        return scrollableDelegate == null ? new Dimension() : scrollableDelegate.getPreferredScrollableViewportSize();
    }

    @Override
    public int getScrollableUnitIncrement(@NotNull final Rectangle visibleRect, final int orientation, final int direction)
    {
        return scrollableDelegate == null ? 0 : scrollableDelegate.getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    @Override
    public int getScrollableBlockIncrement(@NotNull final Rectangle visibleRect, final int orientation, final int direction)
    {
        return scrollableDelegate == null ? 0 : scrollableDelegate.getScrollableBlockIncrement(visibleRect, orientation, direction);
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return scrollableDelegate != null && scrollableDelegate.getScrollableTracksViewportWidth();
    }

    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        return scrollableDelegate != null && scrollableDelegate.getScrollableTracksViewportHeight();
    }
}
