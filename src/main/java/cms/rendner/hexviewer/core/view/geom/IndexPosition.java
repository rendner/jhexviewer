package cms.rendner.hexviewer.core.view.geom;

import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

/**
 * A IndexPosition indicates a location between two bytes. The bias can be used to indicate an interest
 * toward one of the two sides of the position in boundary conditions where a simple offset is ambiguous.
 *
 * @author rendner
 */
public final class IndexPosition
{
    /**
     * The index of the byte to which this position refers.
     */
    private final int index;

    /**
     * The interest in one of the two possible directions.
     */
    private final Bias bias;

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
     * @param index the index of the byte to which this position refers, index has to be &gt;= 0.
     * @param bias  the interest in one of the two possible directions.
     */
    public IndexPosition(final int index, @NotNull final Bias bias)
    {
        super();

        CheckUtils.checkMinValue(index, 0);
        this.index = index;
        this.bias = bias;
    }

    /**
     * @return the index of the byte to which this position refers.
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * @return the interest in one of the two possible directions.
     */
    @NotNull
    public Bias getBias()
    {
        return bias;
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
