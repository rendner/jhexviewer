package cms.rendner.hexviewer.utils;

/**
 * Allows to specify a default value which is returned if no valid alternative value was set.
 *
 * @author rendner
 */
public class FallbackValue<T>
{
    /**
     * The preferred value.
     */
    private T value;

    /**
     * The "fall back" value.
     */
    private T defaultValue;

    /**
     * Creates an empty instance.
     */
    public FallbackValue()
    {
        this(null);
    }

    /**
     * Creates an instance with the specified default value.
     *
     * @param defaultValue the default value.
     */
    public FallbackValue(final T defaultValue)
    {
        super();
        this.defaultValue = defaultValue;
    }

    /**
     * Overwrites the default value.
     * This becomes the new "fall back" value.
     *
     * @param defaultValue the "fall back" value to set, can be <code>null</code>.
     */
    public void setDefaultValue(final T defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the value if not <code>null</code> otherwise the default value. Can be <code>null</code>.
     */
    public T getValue()
    {
        return value != null ? value : defaultValue;
    }

    /**
     * Sets the value which is preferred over the default value.
     *
     * @param value value to set, can be <code>null</code>.
     */
    public void setValue(final T value)
    {
        this.value = value;
    }

    /**
     * @return the "fall back" value, can be <code>null</code>.
     */
    public T getCustomValue()
    {
        return value;
    }
}
