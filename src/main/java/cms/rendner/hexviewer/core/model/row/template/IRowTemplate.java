package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.ElementHitInfo;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Describes the layout of a row.
 * Each row contains a number of elements which are displayed in the row.
 * <p/>
 * If an element should be drawn into a {@link Graphics} object the {@link IRowTemplate#ascent()} should be
 * added to the {@link IElement#y()} to center the char vertically inside the row.
 *
 * @author rendner
 */
public interface IRowTemplate
{
    /**
     * The height of the row.
     *
     * @return the height &gt;=0.
     */
    int height();

    /**
     * The width of the row.
     *
     * @return the width &gt;=0.
     */
    int width();

    /**
     * The space before the first element of the row.
     *
     * @return the space &gt;=0.
     */
    int leftInset();

    /**
     * The space after the last element of the row.
     *
     * @return the space &gt;=0.
     */
    int rightInset();

    /**
     * @return the font to use to render the text content of the row.
     */
    @NotNull
    Font font();

    /**
     * Checks if the position is inside the row.
     *
     * @param position the position to check.
     * @return <code>true</code> if inside, otherwise false.
     */
    boolean contains(@NotNull Point position);

    /**
     * Checks if the x coordinate is inside the row.
     *
     * @param xPosition the x coordinate to check.
     * @return <code>true</code> if inside, otherwise <code>false</code>.
     */
    boolean containsX(int xPosition);

    /**
     * Checks if the y coordinate is inside the row.
     *
     * @param yPosition the y coordinate to check.
     * @return <code>true</code> if inside, otherwise <code>false</code>.
     */
    boolean containsY(int yPosition);

    /**
     * Returns the element at the specified index in this row.
     *
     * @param index index of the element to return, in the range of <code>index &gt;= 0 && index &lt; elementCount()</code>)
     * @return the element at the index.
     */
    @NotNull
    IElement element(int index);

    /**
     * The ascent to vertical center a element in the row.
     *
     * @return the ascent.
     */
    int ascent();

    /**
     * The number of elements in the row.
     *
     * @return number of elements &gt;=0.
     */
    int elementCount();

    /**
     * Performs a hit test at the x position in the row.
     * <p/>
     * If <code>xPosition</code> is located before the first element the hit is counted as hit on the first element.
     * If <code>xPosition</code> is located after the element char the hit is counted as hit on the last element.
     * In such a case to check if a real hit has occurred check <code>{@link ElementHitInfo#wasInside()}</code>.
     *
     * @param xPosition the x position to check.
     * @return an info object which describes which element was hit.
     */
    @NotNull
    ElementHitInfo hitTest(int xPosition);

    /**
     * Performs a hit test at the x position in the row.
     * <p/>
     * If <code>xPosition</code> is located before the first element the hit is counted as hit on the first element.
     * If <code>xPosition</code> is located after the element char the hit is counted as hit on the last element.
     * In such a case to check if a real hit has occurred check <code>{@link ElementHitInfo#wasInside()}</code>.
     *
     * @param xPosition   the x position to check.
     * @param returnValue the object in which the result should be stored.
     * @return the <code>returnValue</code> object.
     */
    @NotNull
    ElementHitInfo hitTest(int xPosition, @NotNull ElementHitInfo returnValue);

    /**
     * Computes an union of the bounds of the elements in range of [<code>firstElementIndex</code>, <code>lastElementIndex</code>].
     *
     * @param firstElementIndex the index of the first element which should be included, in the range [0, elementCount()-1].
     * @param lastElementIndex  the index of the last element which should be included, in the range [0, elementCount()-1].
     * @param returnValue       the rectangle in which the result should be stored.
     * @return <code>returnValue</code> modified to specify the bounds
     */
    @NotNull
    Rectangle elementBounds(int firstElementIndex, int lastElementIndex, @NotNull Rectangle returnValue);

    /**
     * The dimension of a row.
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
}
