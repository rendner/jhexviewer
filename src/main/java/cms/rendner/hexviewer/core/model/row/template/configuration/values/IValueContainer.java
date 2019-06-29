package cms.rendner.hexviewer.core.model.row.template.configuration.values;

/**
 * Holds a value which represents a final value, or a value which depends on other resources/properties
 * and should resolved to a later time.
 *
 * @author rendner
 */
public interface IValueContainer
{
    /**
     * The value stored in the container.
     *
     * @return the value.
     */
    double getValue();
}
