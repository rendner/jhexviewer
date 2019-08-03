package cms.rendner.hexviewer.core.model.row.template.elements;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Element with the properties <code>x</code>, <code>y</code>, <code>width</code> and <code>height</code>.
 * Elements contain a precalculate position to align the digits/characters in a row template.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.core.model.row.template.IRowTemplate
 */
public interface IElement
{
    /**
     * Returns the x-position of the element, shorthand for <code>position().x()</code>.
     *
     * @return the x-position.
     */
    int x();

    /**
     * Returns the y-position of the element, shorthand for <code>position().y()</code>.
     *
     * @return the y-position.
     */
    int y();

    /**
     * Returns the height of the element, shorthand for <code>dimension().height()</code>.
     *
     * @return the height.
     */
    int height();

    /**
     * Returns the width of the element, shorthand for <code>dimension().width()</code>.
     *
     * @return the width.
     */
    int width();

    /**
     * Returns the right of the element, shorthand for <code>x() + width()</code>.
     *
     * @return the right position.
     */
    int right();

    /**
     * Returns the right of the element, shorthand for <code>y() + height()</code>.
     *
     * @return the bottom position.
     */
    int bottom();

    /**
     * The position of the element.
     *
     * @return the position.
     */
    @NotNull
    IPosition position();

    /**
     * The dimension of the element.
     *
     * @return the dimension.
     */
    @NotNull
    IDimension dimension();

    /**
     * Checks if the position is inside the element.
     *
     * @param position the position to check.
     * @return <code>true</code> if inside otherwise <code>false</code>.
     */
    boolean contains(@NotNull Point position);

    /**
     * Checks if the x-position is horizontal inside the element.
     *
     * @param xPosition the x-position to check.
     * @return <code>true</code> if horizontal inside otherwise <code>false</code>.
     */
    boolean containsX(int xPosition);

    /**
     * Checks if the y-position is vertical inside the element.
     *
     * @param yPosition the y-position to check.
     * @return <code>true</code> if vertical inside otherwise <code>false</code>.
     */
    boolean containsY(int yPosition);

    /**
     * Copies the x, y, width and height of the element into a <code>Rectangle</code>.
     *
     * @param returnValue the rectangle which should be filled.
     * @return <code>returnValue</code> modified to specify the bounds of the element.
     */
    @NotNull
    Rectangle toRectangle(@NotNull Rectangle returnValue);

    /**
     * The dimension of an element.
     */
    interface IDimension
    {
        /**
         * The width.
         *
         * @return width.
         */
        int width();

        /**
         * The height.
         *
         * @return height.
         */
        int height();
    }

    /**
     * The position of an element.
     */
    interface IPosition
    {
        /**
         * The x.
         *
         * @return x.
         */
        int x();

        /**
         * The y.
         *
         * @return y.
         */
        int y();
    }
}
