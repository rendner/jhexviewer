package cms.rendner.hexviewer.core.view.geom;

import cms.rendner.hexviewer.utils.CheckUtils;

/**
 * A IndexPosition indicates a location between two bytes. The bias can be used to indicate an interest
 * toward one of the two sides of the position in boundary conditions where a simple offset is ambiguous.
 *
 * @author rendner
 */
public class IndexPosition
{
    /**
     * The index of the byte to which this position refers.
     */
    private int index;

    /**
     * The interest in one of the two possible directions.
     */
    private Bias bias;

    /**
     * Creates a new instance with index set to <code>0</code> and bias to <code>Bias.Forward</code>.
     */
    public IndexPosition()
    {
        this(0, Bias.Forward);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param index the index of the byte to which this position refers.
     * @param bias  the interest in one of the two possible directions.
     * @throws IllegalArgumentException if index is &lt;0 or bias is <code>null</code>.
     */
    public IndexPosition(final int index, final Bias bias)
    {
        super();

        setIndex(index);
        setBias(bias);
    }

    /**
     * Copies the index and bias from another IndexPosition instance.
     *
     * @param source the source to copy from.
     * @throws IllegalArgumentException if source is <code>null</code>.
     */
    public void copyFrom(final IndexPosition source)
    {
        CheckUtils.checkNotNull(source);

        setIndex(source.index);
        setBias(source.bias);
    }

    /**
     * Copies the index and bias to another IndexPosition instance.
     *
     * @param target the target to copy to.
     * @throws IllegalArgumentException if target is <code>null</code>.
     */
    public void copyTo(final IndexPosition target)
    {
        CheckUtils.checkNotNull(target);

        target.setIndex(index);
        target.setBias(bias);
    }

    /**
     * @return the index of the byte to which this position refers.
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the new index.
     *
     * @param index the new index of the byte to which this position refers, &gt=0.
     * @throws IllegalArgumentException if index is &lt;0.
     */
    public void setIndex(final int index)
    {
        CheckUtils.checkMinValue(index, 0);
        this.index = index;
    }

    /**
     * @return the interest in one of the two possible directions.
     */
    public Bias getBias()
    {
        return bias;
    }

    /**
     * Sets the bias.
     *
     * @param bias the bias to toward to the next byte when the index is ambiguous.
     * @throws IllegalArgumentException if bias is <code>null</code>.
     */
    public void setBias(final Bias bias)
    {
        CheckUtils.checkNotNull(bias);
        this.bias = bias;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof IndexPosition))
        {
            return false;
        }

        IndexPosition position = (IndexPosition) o;

        if (index != position.index)
        {
            return false;
        }
        return bias == position.bias;

    }

    @Override
    public int hashCode()
    {
        int result = index;
        result = 31 * result + bias.hashCode();
        return result;
    }

    /**
     * @return the index and bias of the instance prefixed with the name of the class.
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[index:" + index + ", bias:" + bias + "]";
    }

    /**
     * A typesafe enumeration to indicate bias to a position.
     */
    public enum Bias
    {
        /**
         * Indicates a bias toward the next byte.
         */
        Forward,
        /**
         * Indicates a bias toward the previous byte.
         */
        Backward
    }
}
