package cms.rendner.hexviewer.model.rowtemplate.configuration.values;

import java.util.Objects;

/**
 * Represents a fixed value, the value returned by this class can't change during runtime'.
 *
 * @author rendner
 */
public final class FixedValue implements IValue
{
    /**
     * The fixed value.
     */
    private final double value;

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
     * @return the fixed value.
     */
    public double value()
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
