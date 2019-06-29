package cms.rendner.hexviewer.core.view.geom;

import cms.rendner.hexviewer.utils.CheckUtils;

/**
 * A Range specifies a number of successive values.
 * <p/>
 * Used to forward a number of successive rows by their row index.
 *
 * @author rendner
 */
public class Range
{
    /**
     * Constant for an invalid value.
     */
    private static final int MIN_VALID_INDEX = 0;

    /**
     * The start value of the range.
     */
    private int start;

    /**
     * The end value of the range.
     */
    private int end;

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
        resize(start, end);
    }

    /**
     * Sets the start and end to an invalid value.
     */
    public void invalidate()
    {
        start = end = MIN_VALID_INDEX - 1;
    }

    /**
     * Changes the start and end of the range.
     * <p/>
     * The min value of start/end is used as the internal start value.
     * The max value of start/end is used as the internal end value.
     *
     * @param start the start value.
     * @param end   the end value.
     */
    public void resize(final int start, final int end)
    {
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
     * Checks if the range is empty.
     * <p/>
     * A range is empty if the start or end is an invalid value (invalid value: &lt;0).
     *
     * @return <code>true</code> if the range is invalid and has no length.
     */
    public boolean isEmpty()
    {
        return getLength() == 0;
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
    protected boolean isValid(final int start, final int end)
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
        if(value <= MIN_VALID_INDEX)
        {
            return false;
        }

        return value >= start && value <= end;
    }

    /**
     * Calculates the intersection of two ranges.
     * If the two ranges don't intersect, then the returned range is invalid.
     *
     * @param otherRange the second range.
     * @return the intersection of the two rectangles.
     * @throws IllegalStateException    if this range is invalid.
     * @throws IllegalArgumentException if otherRange is <code>null</code> or invalid.
     */
    public Range computeIntersection(final Range otherRange)
    {
        CheckUtils.checkNotNull(otherRange);
        return computeIntersection(otherRange.start, otherRange.end, null);
    }

    /**
     * Convenience to calculate the intersection of two ranges without allocating a new range.
     * If the two ranges don't intersect, then the returned range is invalid.
     *
     * @param otherRange  the range to intersect against, can't be <code>null</code>.
     * @param returnValue the intersection of the two ranges is returned in this range, can't be <code>null</code>.
     * @return <code>returnValue</code>, modified to specify the intersection.
     * @throws IllegalStateException    if this range is invalid.
     * @throws IllegalArgumentException if otherRange or returnValue is <code>null</code> or otherRange is invalid.
     */
    public Range computeIntersection(final Range otherRange, final Range returnValue)
    {
        CheckUtils.checkNotNull(otherRange);
        CheckUtils.checkNotNull(returnValue);

        return computeIntersection(otherRange.start, otherRange.end, returnValue);
    }

    /**
     * Convenience to calculate the intersection of two ranges without allocating a new range.
     * If the two ranges don't intersect, then the returned range is invalid.
     *
     * @param otherStart  the start value of the range to intersect against.
     * @param otherEnd    the end value of the range to intersect against.
     * @param returnValue the intersection of the two ranges is returned in this range.
     * @return <code>result</code>, modified to specify the intersection.
     * @throws IllegalStateException    if this range is invalid.
     * @throws IllegalArgumentException if returnValue is <code>null</code> or the other range is invalid.
     */
    public Range computeIntersection(final int otherStart, final int otherEnd, final Range returnValue)
    {
        CheckUtils.checkNotNull(returnValue);

        if (!isValid())
        {
            throw new IllegalStateException("Can't intersect with an invalid range.");
        }
        if (!isValid(otherStart, otherEnd))
        {
            throw new IllegalArgumentException("Can't intersect with an invalid range.");
        }

        returnValue.invalidate();

        final boolean startIsInRange = otherStart >= start && otherStart <= end;
        final boolean endIsInRange = otherEnd >= start && otherEnd <= end;
        final boolean startsBeforeAndEndsAfter = otherStart < start && otherEnd > end;

        if (startsBeforeAndEndsAfter)
        {
            returnValue.start = start;
            returnValue.end = end;
        }
        if (startIsInRange && endIsInRange)
        {
            returnValue.start = otherStart;
            returnValue.end = otherEnd;
        }
        else if (startIsInRange)
        {
            returnValue.start = otherStart;
            returnValue.end = end;
        }
        else if (endIsInRange)
        {
            returnValue.start = start;
            returnValue.end = otherEnd;
        }

        return returnValue;
    }

    /**
     * Convenience method that calculates the union of two ranges without allocating a new range.
     *
     * @param otherRange the second range.
     * @return the union of the tow ranges.
     * @throws IllegalStateException    if this range is invalid.
     * @throws IllegalArgumentException if otherRange is <code>null</code> or invalid.
     */
    public Range computeUnion(final Range otherRange)
    {
        CheckUtils.checkNotNull(otherRange);
        return computeUnion(otherRange.start, otherRange.end, null);
    }

    /**
     * Convenience method that calculates the union of two ranges without allocating a new range.
     *
     * @param otherRange  the second range.
     * @param returnValue the union of the two ranges is returned in this range.
     * @return the <code>returnValue</code> modified to specify the union.
     * @throws IllegalStateException    if this range is invalid.
     * @throws IllegalArgumentException if otherRange or returnValue is <code>null</code> or otherRange is invalid.
     */
    public Range computeUnion(final Range otherRange, final Range returnValue)
    {
        CheckUtils.checkNotNull(otherRange);
        CheckUtils.checkNotNull(returnValue);

        return computeUnion(otherRange.start, otherRange.end, returnValue);
    }

    /**
     * Convenience method that calculates the union of two ranges without allocating a new range.
     *
     * @param otherStart  the start value of the second range.
     * @param otherEnd    the end value of the second range.
     * @param returnValue the union of the two ranges is returned in this range.
     * @return the <code>returnValue</code> modified to specify the union.
     * @throws IllegalStateException    if this range is invalid.
     * @throws IllegalArgumentException if returnValue is <code>null</code> or the other range is invalid.
     */
    public Range computeUnion(final int otherStart, final int otherEnd, final Range returnValue)
    {
        CheckUtils.checkNotNull(returnValue);

        if (!isValid())
        {
            throw new IllegalStateException("Can't create union with an invalid range.");
        }
        if (!isValid(otherStart, otherEnd))
        {
            throw new IllegalArgumentException("Can't create union with an invalid range.");
        }


        returnValue.start = Math.min(otherStart, start);
        returnValue.end = Math.max(otherEnd, end);

        return returnValue;
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
