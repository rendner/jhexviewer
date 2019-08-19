package cms.rendner.hexviewer.core.model.row.template.element;

import java.util.Objects;

/**
 * Information about a hit on an element.
 * This class is used to describe the nearest element under a mouse position.
 *
 * @author rendner
 */
public final class HitInfo
{
    /**
     * Indicates if the leading edge of an element was hit.
     */
    private final boolean isLeadingEdge;

    /**
     * Indicates if the hit was inside of the bounding box of the element.
     */
    private final boolean wasInside;

    /**
     * The index of the element which was hit.
     */
    private final int index;

    /**
     * Creates a new instance.
     *
     * @param index         the index of the element which was hit.
     * @param isLeadingEdge indicates if the leading edge of an element was hit.
     * @param wasInside     indicates if the hit was inside of the bounding box of the element.
     */
    public HitInfo(final int index, final boolean isLeadingEdge, boolean wasInside)
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
     * Returns the insertion index (index for the caret). Is equals the index if the leading edge of the element
     * was hit, and one greater if the trailing edge was hit.
     *
     * @return the insertion index.
     */
    public int insertionIndex()
    {
        return isLeadingEdge ? index : index + 1;
    }

    /**
     * The index of the element which was hit.
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof HitInfo))
        {
            return false;
        }
        HitInfo hitInfo = (HitInfo) o;
        return isLeadingEdge == hitInfo.isLeadingEdge &&
                wasInside == hitInfo.wasInside &&
                index == hitInfo.index;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(isLeadingEdge, wasInside, index);
    }
}
