package cms.rendner.hexviewer.core.model.row.template.configuration.values;

import java.util.Objects;

/**
 * Abstract implementation of a value container which provides some default behaviour.
 *
 * @author rendner
 */
public abstract class AbstractValue implements IValueContainer
{
    /**
     * The value of the container.
     */
    private final double value;

    /**
     * Creates a new instance with a value of <code>0.0</code>.
     */
    public AbstractValue()
    {
        this(0.0);
    }

    /**
     * Creates a new instance.
     *
     * @param value the value of the container.
     */
    public AbstractValue(final double value)
    {
        super();
        this.value = value;
    }

    @Override
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
        if (!(o instanceof AbstractValue))
        {
            return false;
        }
        AbstractValue that = (AbstractValue) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }
}
