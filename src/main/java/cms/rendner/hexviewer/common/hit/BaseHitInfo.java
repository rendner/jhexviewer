package cms.rendner.hexviewer.common.hit;

import java.util.Objects;

/**
 * Information about a hit on an object.
 * This class is used to describe the nearest object under a mouse position.
 *
 * @author rendner
 */
public abstract class BaseHitInfo
{
    /**
     * Indicates if the leading edge of an object was hit.
     */
    private final boolean isLeadingEdge;

    /**
     * Indicates if the hit was inside of the bounding box of the object.
     */
    private final boolean wasInside;

    /**
     * Creates a new instance.
     *
     * @param isLeadingEdge indicates if the leading edge of an object was hit.
     * @param wasInside     indicates if the hit was inside of the bounding box of the object.
     */
    protected BaseHitInfo(final boolean isLeadingEdge, boolean wasInside)
    {
        this.isLeadingEdge = isLeadingEdge;
        this.wasInside = wasInside;
    }

    /**
     * Returns <code>true</code> if the leading edge of an object was hit.
     *
     * @return <code>true</code> if the leading edge of the object was hit; <code>false</code> otherwise.
     */
    public boolean isLeadingEdge()
    {
        return isLeadingEdge;
    }

    /**
     * Checks if the hit was inside of the bounding box of the object.
     *
     * @return <code>true</code> if inside otherwise <code>false</code>
     */
    public boolean wasInside()
    {
        return wasInside;
    }

    /**
     * Checks if the hit was outside of the bounding box of the object.
     *
     * @return <code>true</code> if outside otherwise <code>false</code>
     */
    public boolean wasOutside()
    {
        return !wasInside;
    }

    /**
     * Checks if the hit was outside and after the bounding box of the object.
     *
     * @return <code>true</code> if outside and after otherwise <code>false</code>
     */
    public boolean wasOutsideAfter()
    {
        return !isLeadingEdge() && wasOutside();
    }

    /**
     * Checks if the hit was outside and before the bounding box of the object.
     *
     * @return <code>true</code> if outside and before otherwise <code>false</code>
     */
    public boolean wasOutsideBefore()
    {
        return isLeadingEdge() && wasOutside();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        final BaseHitInfo that = (BaseHitInfo) o;
        return isLeadingEdge == that.isLeadingEdge &&
                wasInside == that.wasInside;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(isLeadingEdge, wasInside);
    }
}
