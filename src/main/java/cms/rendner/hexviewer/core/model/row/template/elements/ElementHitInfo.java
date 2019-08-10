package cms.rendner.hexviewer.core.model.row.template.elements;

/**
 * Information about an element in the layout.
 * This class is used to describe the nearest element of a row under a mouse position.
 *
 * @author rendner
 */
public class ElementHitInfo
{
    private boolean isLeadingEdge;
    private boolean wasInside;
    private int index;

    /**
     * Repopulates the info with new data.
     * This method can be used to reuse an hit info instance.
     *
     * @param index         the index of the element which was hit.
     * @param isLeadingEdge indicates if the leading edge of an element was hit.
     * @param wasInside     indicates if the hit was inside of the bounding box of the element.
     */
    public void fillWith(final int index, final boolean isLeadingEdge, boolean wasInside)
    {
        this.index = index;
        this.isLeadingEdge = isLeadingEdge;
        this.wasInside = wasInside;
    }

    /**
     * Returns <code>true</code> if the leading edge of an element was
     * hit.
     *
     * @return <code>true</code> if the leading edge of the element was
     * hit; <code>false</code> otherwise.
     */
    public boolean isLeadingEdge()
    {
        return isLeadingEdge;
    }

    /**
     * Returns the insertion index (index for the caret). This is the index of the element inside the row if
     * the leading edge of the element was hit, and one greater
     * than the element index if the trailing edge was hit.
     *
     * @return the insertion index.
     */
    public int insertionIndex()
    {
        return isLeadingEdge ? index : index + 1;
    }

    /**
     * The index in the row of the element which was hit.
     *
     * @return the index of the element.
     */
    public int index()
    {
        return index;
    }

    /**
     * Checks if a hit was inside of the bounding box of the element.
     *
     * @return <code>true</code> if inside otherwise <code>false</code>
     */
    public boolean wasInside()
    {
        return wasInside;
    }

    /**
     * Checks if a hit was outside of the bounding box of the element.
     *
     * @return <code>true</code> if outside otherwise <code>false</code>
     */
    public boolean wasOutside()
    {
        return !wasInside;
    }

    /**
     *  Checks if a hit was outside and after the bounding box of the element.
     *
     * @return <code>true</code> if outside and after otherwise <code>false</code>
     */
    public boolean wasOutsideAfter()
    {
        return !isLeadingEdge() && wasOutside();
    }

    /**
     *  Checks if a hit was outside and before the bounding box of the element.
     *
     * @return <code>true</code> if outside and before otherwise <code>false</code>
     */
    public boolean wasOutsideBefore()
    {
        return isLeadingEdge() && wasOutside();
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
                "[isLeadingEdge=" + isLeadingEdge + ", index=" + index + ", wasInside=" + wasInside + "]";
    }
}
