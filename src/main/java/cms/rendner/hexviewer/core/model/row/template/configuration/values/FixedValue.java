package cms.rendner.hexviewer.core.model.row.template.configuration.values;

import java.util.Objects;

/**
 * Represents a fixed value, that means that the value returned by this value can't change during runtime'.
 *
 * @author rendner
 */
public final class FixedValue implements IValue
{
    /**
     * The value of the container.
     */
    private final double value;

    /**
     * Creates a new instance with a value of <code>0.0</code>.
     */
    public FixedValue()
    {
        this(0.0);
    }

    /**
     * Creates a new instance.
     *
     * @param value the value of the fixed value.
     */
    public FixedValue(final double value)
    {
        super();
        this.value = value;
    }

    /**
     * @return the value of the fixed value.
     */
    public double getValue()
    {
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof FixedValue))
        {
            return false;
        }
        FixedValue that = (FixedValue) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }
}
