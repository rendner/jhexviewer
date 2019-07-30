package cms.rendner.hexviewer.core.model.row.template.configuration.values;

import cms.rendner.hexviewer.utils.CheckUtils;

import java.util.Objects;

/**
 * The insets of a row.
 *
 * @author rendner
 */
public class RowInsetValues
{
    /**
     * The left inset of a row.
     */
    private IValue left;

    /**
     * The right inset of a row.
     */
    private IValue right;

    /**
     * Creates a new instance with an inset of <code>0</code>.
     */
    public RowInsetValues()
    {
        super();

        left = new FixedValue(0);
        right = new FixedValue(0);
    }

    /**
     * Creates a new instance with the specified values.
     *
     * @param left  the inset for the left side, can't be <code>null</code>.
     * @param right the inset for the right side, can't be <code>null</code>.
     */
    public RowInsetValues(final IValue left, final IValue right)
    {
        super();

        setLeft(left);
        setRight(right);
    }

    /**
     * Returns the value for the right side of a row.
     *
     * @return the right value.
     */
    public IValue getRight()
    {
        return right;
    }

    /**
     * Sets the new value for the right side.
     *
     * @param right the new value.
     */
    public void setRight(final IValue right)
    {
        CheckUtils.checkNotNull(right);
        this.right = right;
    }

    /**
     * Returns the value for the left side of a row.
     *
     * @return the left value.
     */
    public IValue getLeft()
    {
        return left;
    }

    /**
     * Sets the new value for the left side.
     *
     * @param left the new value.
     */
    public void setLeft(final IValue left)
    {
        CheckUtils.checkNotNull(left);
        this.left = left;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof RowInsetValues))
        {
            return false;
        }
        RowInsetValues that = (RowInsetValues) o;
        return left.equals(that.left) &&
                right.equals(that.right);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(left, right);
    }
}
