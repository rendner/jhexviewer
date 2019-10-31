package cms.rendner.hexviewer.view.ui.container.common;

/**
 * A typesafe enumeration for scroll directions.
 *
 * @author rendner
 */
public interface ScrollDirection
{
    /**
     * Value used to scroll up.
     * (increment the vertical position by -1)
     */
    int UP = -1;

    /**
     * Value used to scroll left.
     * (increment the horizontal position by -1)
     */
    int LEFT = -1;

    /**
     * Value used to scroll down.
     * (increment the vertical position by 1)
     */
    int DOWN = 1;

    /**
     * Value used to scroll right.
     * (increment the horizontal position by 1)
     */
    int RIGHT = 1;
}