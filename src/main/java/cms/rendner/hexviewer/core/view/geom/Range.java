package cms.rendner.hexviewer.core.view.geom;

import org.jetbrains.annotations.NotNull;

/**
 * A Range specifies a number of successive values.
 * <p/>
 * Used to forward a number of successive rows by their row index.
 *
 * @author rendner
 */
public final class Range
{
    /**
     * Constant for an invalid value.
     */
    private static final int MIN_VALID_INDEX = 0;

    /**
     * An invalid instance.
     */
    public static final Range INVALID = new Range(MIN_VALID_INDEX-1, MIN_VALID_INDEX-1);

    /**
     * The start value of the range.
     */
    private final int start;

    /**
     * The end value of the range.
     */
    private final int end;

    /**
     * Creates a new instance with an start and end set to 0.
     */
    public Range()
    {
        this(MIN_VALID_INDEX, MIN_VALID_INDEX);
    }

    /**
     * Creates a new instance with the specified start and end.
     * <p/>
     * The min value of start/end is used as the internal start value.
     * The max value of start/end is used as the internal end value.
     *
     * @param start the start value, should be &gt;=0 otherwise the range is invalid.
     * @param end   the end value, should be &gt;=0 otherwise the range is invalid.
     */
    public Range(final int start, final int end)
    {
        super();
        this.start = Math.min(start, end);
        this.end = Math.max(start, end);
    }

    /**
     * @return the start value of the range.
     */
    public int getStart()
    {
        return start;
    }

    /**
     * @return the end value of the range.
     */
    public int getEnd()
    {
        return end;
    }

    /**
     * Returns the length of the range.
     * If start is equals end, the range has a length of <code>1</code>.
     * Only invalid ranges have a length of <code>0</code>
     *
     * @return the length of the range, &gt;=0.
     */
    public int getLength()
    {
        return isValid() ? 1 + end - start : 0;
    }

    /**
     * Check if the range is valid.
     * A range is valid if start and end of the range are &gt;=0.
     *
     * @return <code>true</code> if range is valid.
     */
    public boolean isValid()
    {
        return isValid(start, end);
    }

    /**
     * Check if the range is valid.
     * A range is valid if start and end of the range are &gt;=0.
     *
     * @param start the start value to check.
     * @param end   the end value to check.
     * @return <code>true</code> if range is valid.
     */
    private boolean isValid(final int start, final int end)
    {
        return start >= MIN_VALID_INDEX && end >= MIN_VALID_INDEX && start <= end;
    }

    /**
     * Checks if given value lies in range.
     *
     * @param value the value to check.
     * @return <code>true</code> if <code>value &gt;= 0 && value &gt;= start && value &lt;= end</code>.
     */
    public boolean contains(final int value)
    {
        if (value <= MIN_VALID_INDEX)
        {
            return false;
        }

        return value >= start && value <= end;
    }

    /**
     * Convenience to calculate the intersection of two ranges without allocating a new range.
     * If the two ranges don't intersect, then the returned range is invalid.
     *
     * @param otherStart  the start value of the range to intersect against, <code>otherStart >= MIN_VALID_INDEX && otherStart <= otherEnd</code>.
     * @param otherEnd    the end value of the range to intersect against, <code>otherEnd >= MIN_VALID_INDEX && otherStart <= otherEnd</code>.
     * @return the intersection of the two ranges.
     */
    @NotNull
    public Range computeIntersection(final int otherStart, final int otherEnd)
    {
        if (!isValid())
        {
            return Range.INVALID;
        }
        else if (!isValid(otherStart, otherEnd))
        {
            return Range.INVALID;
        }

        final boolean startIsInRange = otherStart >= start && otherStart <= end;
        final boolean endIsInRange = otherEnd >= start && otherEnd <= end;
        final boolean startsBeforeAndEndsAfter = otherStart < start && otherEnd > end;

        if (startsBeforeAndEndsAfter)
        {
            return new Range(start, end);
        }
        if (startIsInRange && endIsInRange)
        {
            return new Range(otherStart, otherEnd);
        }
        else if (startIsInRange)
        {
            return new Range(otherStart, end);
        }
        else if (endIsInRange)
        {
            return new Range(start, otherEnd);
        }

        return Range.INVALID;
    }

    /**
     * Convenience method that calculates the union of two ranges without allocating a new range.
     *
     * @param otherStart  the start value of the second range, <code>otherStart >= MIN_VALID_INDEX && otherStart <= otherEnd</code>.
     * @param otherEnd    the end value of the second range, <code>otherEnd >= MIN_VALID_INDEX && otherStart <= otherEnd</code>.
     * @return the union.
     */
    @NotNull
    public Range computeUnion(final int otherStart, final int otherEnd)
    {
        if (!isValid())
        {
            return Range.INVALID;
        }
        else if (!isValid(otherStart, otherEnd))
        {
            return Range.INVALID;
        }

        return new Range(Math.min(start, otherStart), Math.max(end, otherEnd));
    }

    /**
     * @return the index of the byte in the view prefixed with the name of the class.
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[start:" + start + ", end: " + end + "]";
    }
}
