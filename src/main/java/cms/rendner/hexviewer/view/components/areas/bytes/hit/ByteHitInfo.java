package cms.rendner.hexviewer.view.components.areas.bytes.hit;

import cms.rendner.hexviewer.common.hit.BaseHitInfo;

import java.util.Objects;

/**
 * Information about a hit on an byte inside a byte-area.
 * This class is used to describe the nearest byte under a mouse position.
 *
 * @author rendner
 */
public final class ByteHitInfo extends BaseHitInfo
{
    /**
     * The index of the byte which was hit.
     */
    private final long index;

    /**
     * Creates a new instance.
     *
     * @param index         the index of the byte which was hit.
     * @param isLeadingEdge indicates if the leading edge of a byte was hit.
     * @param wasInside     indicates if the hit was inside of the bounding box of the byte.
     */
    public ByteHitInfo(final long index, final boolean isLeadingEdge, boolean wasInside)
    {
        super(isLeadingEdge, wasInside);
        this.index = index;
    }

    /**
     * Returns the insertion index (index for the caret). Is equals the index if the leading edge of the byte, and one
     * greater if the trailing edge was hit.
     *
     * @return the insertion index.
     */
    public long insertionIndex()
    {
        return isLeadingEdge() ? index : index + 1;
    }

    /**
     * The index of the byte which was hit.
     *
     * @return the index of the byte.
     */
    public long index()
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
        final ByteHitInfo that = (ByteHitInfo) o;
        return index == that.index;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), index);
    }
}
