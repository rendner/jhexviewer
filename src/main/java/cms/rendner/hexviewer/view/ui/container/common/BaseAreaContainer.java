package cms.rendner.hexviewer.view.ui.container.common;

import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.view.ui.container.BorderlessJComponent;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Scrollable container for displaying area components.
 * This component implements the default scroll behaviour to scroll area components.
 *
 * @author rendner
 */
public abstract class BaseAreaContainer extends BorderlessJComponent implements Scrollable
{
    /**
     * The number of preferred visible rows displayed in the area.
     */
    private int preferredVisibleRowCount = 8;

    protected BaseAreaContainer()
    {
        // Don't use the BoxLayout (BUG?) -> at least on mac, the max height of a children is "16384" if a box layout is used
        //
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    }

    /**
     * Returns the number of rows which should be displayed.
     *
     * @return number of trows &gt;= 1.
     */
    public int getPreferredVisibleRowCount()
    {
        return preferredVisibleRowCount;
    }

    /**
     * Sets the number of preferred rows which should be displayed.
     *
     * @param newValue number of rows, &gt;= 1.
     */
    public void setPreferredVisibleRowCount(final int newValue)
    {
        CheckUtils.checkMinValue(newValue, 1);

        if (newValue != preferredVisibleRowCount)
        {
            preferredVisibleRowCount = newValue;
            revalidate();
            repaint();
        }
    }

    @NotNull
    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        final int visibleRowCount = getPreferredVisibleRowCount();
        final Dimension result = getPreferredSize();

        if (visibleRowCount > 0)
        {
            result.height = (visibleRowCount * rowHeight());
        }

        return result;
    }

    /**
     * Returns the height of a single row of the areas displayed by this component.
     * This call is forwarded to the areas maintained by this component.
     *
     * @return the height of one row, &gt;= 1.
     */
    protected abstract int rowHeight();

    @Override
    public int getScrollableUnitIncrement(@NotNull final Rectangle visibleRect,
                                          @MagicConstant(flags = {SwingConstants.HORIZONTAL, SwingConstants.VERTICAL}) final int orientation,
                                          @MagicConstant(flags = {ScrollDirection.UP, ScrollDirection.LEFT, ScrollDirection.RIGHT, ScrollDirection.DOWN}) final int direction)
    {
        if (orientation == SwingConstants.HORIZONTAL)
        {
            return computeHorizontalUnitIncrement(visibleRect, direction);
        }
        else
        {
            return rowHeight();
        }
    }

    @Override
    public int getScrollableBlockIncrement(@NotNull final Rectangle visibleRect,
                                           @MagicConstant(flags = {SwingConstants.HORIZONTAL, SwingConstants.VERTICAL}) final int orientation,
                                           @MagicConstant(flags = {ScrollDirection.UP, ScrollDirection.LEFT, ScrollDirection.RIGHT, ScrollDirection.DOWN}) final int direction)
    {
        if (orientation == SwingConstants.HORIZONTAL)
        {
            return visibleRect.width;
        }
        else
        {
            return visibleRect.height;
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        final Container parent = SwingUtilities.getUnwrappedParent(this);
        return parent instanceof JViewport && parent.getWidth() > this.getPreferredSize().width;
    }

    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        final Container parent = SwingUtilities.getUnwrappedParent(this);
        return parent instanceof JViewport && parent.getHeight() > this.getPreferredSize().height;
    }

    /**
     * Computes the distance to scroll to completely expose the next or previous char.
     *
     * @param visibleRect the view area visible within the viewport.
     * @param direction   less or equal to zero to scroll back, greater than zero for forward.
     * @return the "unit" increment for scrolling in the specified direction. This value should always be positive.
     */
    protected abstract int computeHorizontalUnitIncrement(@NotNull final Rectangle visibleRect,
                                                          @MagicConstant(flags = {ScrollDirection.LEFT, ScrollDirection.RIGHT}) final int direction);
}
