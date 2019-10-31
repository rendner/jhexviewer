package cms.rendner.hexviewer.common.utils;

import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.UIResource;

/**
 * Provides several utility methods to simplify setting properties through the UI.
 *
 * @author rendner
 */
public final class UIDelegateUtils
{
    /**
     * Checks if the property can be installed with a custom implementation.
     * <p/>
     * A property can be replaced if it is <code>null</code> or implements the {@link UIResource} interface.
     *
     * @param propertyValue property value to check.
     * @return <code>true</code> if another value can be installed, <code>false</code> otherwise.
     */
    public static boolean canInstallValue(@Nullable final Object propertyValue)
    {
        return (propertyValue == null || propertyValue instanceof UIResource);
    }

    /**
     * Checks if the property should be uninstalled.
     * <p/>
     * A property should be uninstalled if it implements the {@link UIResource} interface.
     *
     * @param propertyValue property value to check.
     * @return <code>true</code> if value should be uninstalled, <code>false</code> otherwise.
     */
    public static boolean shouldUninstallValue(@Nullable final Object propertyValue)
    {
        return propertyValue instanceof UIResource;
    }

    /**
     * Hide constructor.
     */
    private UIDelegateUtils()
    {
    }
}
