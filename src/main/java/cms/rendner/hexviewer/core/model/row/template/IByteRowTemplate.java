package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.core.model.row.template.element.HitInfo;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Describes the layout of a byte-row.
 * A byte row consist of chars which are grouped to a "byte".
 * Between the bytes, a caret can be placed.
 *
 * @author rendner
 * @see IRowTemplate
 */
public interface IByteRowTemplate extends IRowTemplate
{
    /**
     * Computes the bounds of the caret in this row.
     * A caret is always placed in front of a byte.
     *
     * @param byteIndex   the index of the byte before which the caret should be inserted, in the range [0, getNumberOfBytes()-1].
     * @return the bounds of the caret
     */
    @NotNull
    Rectangle caretBounds(int byteIndex);

    /**
     * Returns the element at the specified index in this row.
     *
     * @param index index of the element to return, in the range of <code>index &gt;= 0 && index &lt; elementCount()</code>)
     * @return the element at the index.
     */
    @NotNull
    Element element(int index);

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
     * In such a case to check if a real hit has occurred check <code>{@link HitInfo#wasInside()}</code>.
     *
     * @param xPosition the x position to check.
     * @return an info object which describes which element was hit.
     */
    @NotNull
    HitInfo hitTest(int xPosition);

    /**
     * Computes an union of the bounds of the elements in range of [<code>firstElementIndex</code>, <code>lastElementIndex</code>].
     *
     * @param firstElementIndex the index of the first element which should be included, in the range [0, elementCount()-1].
     * @param lastElementIndex  the index of the last element which should be included, in the range [0, elementCount()-1].
     * @return the bounds of the elements.
     */
    @NotNull
    Rectangle elementBounds(int firstElementIndex, int lastElementIndex);
}
