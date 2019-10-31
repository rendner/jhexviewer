package cms.rendner.hexviewer.view.components.areas.offset.model.colors;

import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Provides colors for parts which are painted in an offset-area, like the background of the row elements.
 * <p>
 * The individual components/parts are not forced to respect the colors provided by the color provider. But it is a good
 * way to use the colors provided by this object.
 *
 * @author rendner
 */
public interface IOffsetColorProvider extends IAreaColorProvider
{
    /**
     * Returns the color for the element background.
     *
     * @param rowIndex the row index of the element to draw.
     * @return the color to use, <code>null</code> if the installed area painter should decide which color to use. The
     * preferred behaviour would be that in case of <code>null</code> no background is painted.
     */
    @Nullable
    default Color getRowElementBackground(int rowIndex)
    {
        return null;
    }

    /**
     * Returns the color for the element foreground.
     *
     * @param rowIndex the row index of the element to draw..
     * @return the color to use, <code>null</code> if the installed area painter should decide which color to use. The
     * preferred behaviour would be that in case of <code>null</code> a default color is used to draw the foreground.
     */
    @Nullable
    default Color getRowElementForeground(int rowIndex)
    {
        return Color.black;
    }
}
