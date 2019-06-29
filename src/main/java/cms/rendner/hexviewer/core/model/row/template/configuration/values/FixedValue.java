package cms.rendner.hexviewer.core.model.row.template.configuration.values;

/**
 * Represents a fixed value, that means that this value can be used directly without any transformations.
 *
 * @author rendner
 */
public class FixedValue extends AbstractValue
{
    /**
     * Creates a new instance.
     *
     * @param value the value of the container.
     */
    public FixedValue(final double value)
    {
        super(value);
    }
}
