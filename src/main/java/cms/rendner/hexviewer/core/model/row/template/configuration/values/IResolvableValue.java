package cms.rendner.hexviewer.core.model.row.template.configuration.values;

/**
 * Interface for values which are calculated at runtime.
 *
 * @author rendner
 */
public interface IResolvableValue extends IValue
{
    /**
     * Resolves the value.
     *
     * @param inputValue an input value to resolve the value. Can be ignored if the resolvable value doesn't depend on
     *                   an external input.
     * @return the resolved value.
     */
    double resolve(double inputValue);
}
