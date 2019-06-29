package cms.rendner.hexviewer.core.model.row.template.configuration.values;

/**
 * Interface for values which depends on other properties/resources and needs to check if the calculated value
 * is in a specific range.
 *
 * @author rendner
 */
public interface IVetoableValueContainer extends IValueContainer
{
    /**
     * Checks the calculated value and returns another value if the calculated isn't acceptable.
     *
     * @param calculatedValue the calculated value.
     * @return the value which should be used, this value can be differ from the <code>calculatedValue</code> if
     * this value wasn't accepted.
     */
    double getAcceptedValue(double calculatedValue);
}
