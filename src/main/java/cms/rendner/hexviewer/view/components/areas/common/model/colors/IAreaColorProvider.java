package cms.rendner.hexviewer.view.components.areas.common.model.colors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * A color provider provides an easy way to exchange colors to customize the look of an area without the need to subclass
 * some of the parts which paint the area.
 * <p>
 * The individual components/parts are not forced to respect the colors provided by the color provider. But it is a good
 * way to use the colors provided by this object.
 *
 * @author rendner
 */
public interface IAreaColorProvider
{
    /**
     * @return the background color for the area, <code>null</code> if the painter should decide which
     * color to use. The preferred behaviour of the painter would be that in case of <code>null</code> no background is
     * painted.
     */
    @Nullable
    default Color getBackground()
    {
        return null;
    }

    /**
     * Returns the background color for a specific row.
     *
     * @param rowIndex the index of the row.
     * @return the color for the row background.
     */
    @Nullable
    default Color getRowBackground(int rowIndex)
    {
        return null;
    }

    /**
     * Checks if a color is available for a specific key.
     * <p/>
     * There is no default implementation for this. Subclasses have to implementation the required functionality.
     *
     * @param key the key/identifier for a color.
     * @return the color mapped to the key, <code>null</code> if no color was found for the key.
     */
    default boolean hasColor(@NotNull String key)
    {
        return false;
    }

    /**
     * Returns the color for a specific key.
     * <p/>
     * There is no default implementation for this. Subclasses have to implementation the required functionality.
     *
     * @param key the key/identifier for a color.
     * @return the color mapped to the key, <code>null</code> if no color was found for the key.
     */
    @Nullable
    default Color getColor(@NotNull String key)
    {
        return null;
    }

    /**
     * Returns the color for a specific key.
     * <p/>
     * There is no default implementation for this. Subclasses have to implementation the required functionality.
     *
     * @param key       the key/identifier for a color.
     * @param otherwise returned if the color for the key doesn't exists or is <code>null</code>.
     * @return the color mapped to the key, or <code>otherwise</code> if the mapped color is <code>null</code> or doesn't exist.
     */
    @NotNull
    default Color getColor(@NotNull String key, @NotNull Color otherwise)
    {
        return otherwise;
    }
}
