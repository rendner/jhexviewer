package cms.rendner.hexviewer.utils;

import org.jetbrains.annotations.Nullable;

/**
 * Utility to work with objects.
 *
 * @author rendner
 */
public final class ObjectUtils
{
    /**
     * Returns the first value if not <code>null</code> otherwise the second value.
     *
     * @param value  value to check if not <code>null</code>.
     * @param otherwise value to return if <code>value</code> is <code>null</code>.
     * @param <T>    the type of the values.
     * @return the first value if not <code>null</code> otherwise the second value
     */
    @Nullable
    public static <T> T ifNotNullOtherwise(@Nullable final T value, @Nullable final T otherwise)
    {
        return value != null ? value : otherwise;
    }

    /**
     * Hide constructor.
     */
    private ObjectUtils(){}
}
