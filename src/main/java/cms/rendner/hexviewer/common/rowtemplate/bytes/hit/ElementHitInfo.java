package cms.rendner.hexviewer.common.rowtemplate.bytes.hit;

import cms.rendner.hexviewer.common.hit.BaseHitInfo;

import java.util.Objects;

/**
 * Information about a hit on an element inside a row template.
 * This class is used to describe the nearest element under a mouse position.
 *
 * @author rendner
 */
public final class ElementHitInfo extends BaseHitInfo
{
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
    public ElementHitInfo(final int index, final boolean isLeadingEdge, boolean wasInside)
    {
        super(isLeadingEdge, wasInside);
        this.index = index;
    }

    /**
     * Returns the insertion index (index for the caret). Is equals the index if the leading edge of the element,
     * and one greater if the trailing edge was hit.
     *
     * @return the insertion index.
     */
    public long insertionIndex()
    {
        return isLeadingEdge() ? index : index + 1;
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

    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
                "[isLeadingEdge=" + isLeadingEdge() + ", index=" + index + ", wasInside=" + wasInside() + "]";
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
        if (!super.equals(o))
        {
            return false;
        }
        final ElementHitInfo that = (ElementHitInfo) o;
        return index == that.index;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), index);
    }
}
