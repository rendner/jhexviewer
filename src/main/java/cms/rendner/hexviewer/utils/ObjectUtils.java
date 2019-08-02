package cms.rendner.hexviewer.utils;

/**
 * Utility to work with objects.
 *
 * @author rendner
 */
public class ObjectUtils
{
    /**
     * Returns the first value if not <code>null</code> otherwise the second value.
     *
     * @param value  value to check if not <code>null</code>.
     * @param otherwise value to return if <code>value</code> is <code>null</code>.
     * @param <T>    the type of the values.
     * @return the first value if not <code>null</code> otherwise the second value
     */
    public static <T> T ifNotNullOtherwise(final T value, final T otherwise)
    {
        return value != null ? value : otherwise;
    }
}
