package cms.rendner.hexviewer.swing.separator;

import javax.swing.*;
import java.awt.*;

/**
 * Adds support to draw vertically separators into a <code>JViewport</code>.
 * <p/>
 * Separators which are defined in children of this component (inside of JSeparatedView instances) are directly painted
 * into this component to overcome some limitations:
 * <p/>
 * If the separator is drawn by the viewport-view (JSeparatedView) directly, the separator has the same height as his parent
 * which is in this case the viewport-view (JSeparatedView). If the viewport-view (JSeparatedView) height is smaller than
 * the height of the viewport (JSeparatedViewport), the vertical separators won't reach the bottom of the viewport (JSeparatedViewport).
 * Which looks awkward.
 * <p/>
 * To fix this dilemma, separators can be added as a child of the viewport-view (JSeparatedView) via a SeparatorPlaceholder.
 *
 * @author rendner
 */
public class JSeparatedViewport extends JViewport
{
    /**
     * Paints the children and a separator on top of the children to fill the placeholder for the separator
     * with the separator.
     *
     * @param g the <code>Graphics</code> context in which to paint.
     */
    @Override
    protected void paintChildren(final Graphics g)
    {
        super.paintChildren(g);
        paintSeparators(g);
    }

    /**
     * Paint all separators.
     *
     * @param g the <code>Graphics</code> context in which to paint.
     */
    private void paintSeparators(final Graphics g)
    {
        final Component view = getView();
        if (view instanceof JSeparatedView)
        {
            final JSeparatedView separatedView = (JSeparatedView) view;
            final int offset = separatedView.getX();
            final int separatorHeight = getHeight();
            final int placeholderCount = separatedView.getSeparatorPlaceholderCount();

            for (int i = 0; i < placeholderCount; i++)
            {
                paintSeparator(g, separatedView.getSeparatorPlaceholder(i), offset, separatorHeight);
            }
        }
    }

    /**
     * Paints a separator.
     *
     * @param g           the <code>Graphics</code> context in which to paint.
     * @param placeholder the placeholder which owns the separator which should be painted.
     * @param xOffset     the x position of the view which is wrapped by the viewport.
     * @param height      the height for the separator.
     */
    private void paintSeparator(final Graphics g, final VSeparatorPlaceholder placeholder, final int xOffset, final int height)
    {
        final Separator separator = placeholder.getSeparator();
        if (null != separator)
        {
            separator.paint(g, xOffset + placeholder.getX(), 0, height);
        }
    }
}
