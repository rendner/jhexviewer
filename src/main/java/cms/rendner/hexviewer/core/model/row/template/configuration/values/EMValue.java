package cms.rendner.hexviewer.core.model.row.template.configuration.values;

import java.util.Objects;

/**
 * This value depends on the used font and font size (typographically unit 'em').
 * The real value is calculated at runtime by using the current installed font of the JHexViewer.
 *
 * @author rendner
 */
public class EMValue extends AbstractValue implements IVetoableValueContainer
{
    /**
     * The minimum value.
     */
    private final double minValue;

    /**
     * Creates a new instance.
     *
     * @param value the value of the container.
     */
    public EMValue(final double value)
    {
        this(1, value);
    }

    /**
     * Creates a new instance.
     *
     * @param minValue the minimum value of the container.
     *                 This value is used as fallback if the calculated value is less than the minimum.
     * @param value    the value of the container.
     */
    public EMValue(final double minValue, final double value)
    {
        super(value);

        this.minValue = minValue;
    }

    @Override
    public double getAcceptedValue(final double calculatedValue)
    {
        return calculatedValue < minValue ? minValue : calculatedValue;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof EMValue))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }

        EMValue emValue = (EMValue) o;

        return minValue == emValue.minValue;

    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), minValue);
    }
}
