package cms.rendner.hexviewer.utils;

/**
 * Allows to specify a fallback value which is returned if no valid preferred value was set.
 *
 * @author rendner
 */
public class FallbackValue<T>
{
    /**
     * The preferred value.
     */
    private T preferredValue;

    /**
     * The fallback value.
     */
    private T fallbackValue;

    /**
     * Creates an empty instance and sets the fallback value to <code>null</code>.
     */
    public FallbackValue()
    {
        this(null);
    }

    /**
     * Creates an instance with the specified fallback value.
     *
     * @param fallbackValue the fallback value to use if no value is set.
     */
    public FallbackValue(final T fallbackValue)
    {
        super();
        this.fallbackValue = fallbackValue;
    }

    /**
     * Sets the fallback value.
     *
     * @param fallbackValue the fallback value to set, can be <code>null</code>.
     */
    public void setFallbackValue(final T fallbackValue)
    {
        this.fallbackValue = fallbackValue;
    }

    /**
     * @return the preferred value if not <code>null</code> otherwise the fallback value. Can be <code>null</code>.
     */
    public T getValue()
    {
        return preferredValue != null ? preferredValue : fallbackValue;
    }

    /**
     * Sets the value which is preferred over the fallback value.
     *
     * @param preferredValue value to set, can be <code>null</code>.
     */
    public void setPreferredValue(final T preferredValue)
    {
        this.preferredValue = preferredValue;
    }

    /**
     * @return the preferred value which "overwrites" the internal fallback value, can be <code>null</code>.
     */
    public T getPreferredValue()
    {
        return preferredValue;
    }
}
