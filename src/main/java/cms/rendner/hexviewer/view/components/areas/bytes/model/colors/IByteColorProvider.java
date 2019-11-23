package cms.rendner.hexviewer.view.components.areas.bytes.model.colors;

import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Provides colors for parts which are painted in a byte-area, like caret, selection and highlights.
 * <p>
 * The individual components/parts are not forced to respect the colors provided by the color provider. But it is a good
 * way to use the colors provided by this object.
 *
 * @author rendner
 */
public interface IByteColorProvider extends IAreaColorProvider
{
    /**
     * Returns the background color for a row element.
     *
     * @param byteValue         the byte to paint.
     * @param offset            the offset of the byte to paint.
     * @param rowIndex          the row index of the element.
     * @param elementInRowIndex the index of the element in the row.
     * @return the color to use, <code>null</code> if the area painter should decide which color to use. The
     * preferred behaviour of the painter would be that in case of <code>null</code> no background is painted.
     */
    @Nullable
    default Color getRowElementBackground(int byteValue, long offset, int rowIndex, int elementInRowIndex)
    {
        return null;
    }

    /**
     * Returns the foreground color for a row element.
     *
     * @param byteValue         the byte to paint.
     * @param offset            the offset of the byte to paint.
     * @param rowIndex          the row index of the element.
     * @param elementInRowIndex the index of the element in the row.
     * @return the color to use, <code>null</code> if the area painter should decide which color to use. The
     * preferred behaviour would be that in case of <code>null</code> a default color is used to draw the foreground.
     */
    @Nullable
    default Color getRowElementForeground(int byteValue, long offset, int rowIndex, int elementInRowIndex)
    {
        return Color.black;
    }

    /**
     * Provides the color to use to paint the caret.
     *
     * @return the color to use, <code>null</code> if the installed caret should decide which color to use.
     */
    @Nullable
    default Color getCaret()
    {
        return Color.gray;
    }

    /**
     * Provides the color to use to paint the selection.
     *
     * @return the color to use, <code>null</code> if the installed caret should decide which color to use.
     */
    @Nullable
    default Color getSelection()
    {
        return Color.darkGray;
    }

    /**
     * Provides the color to use to paint highlights if they don't have a custom painter assigned.
     *
     * @return the color to use, <code>null</code> if the installed highlighter should decide which color to use.
     */
    @Nullable
    default Color getDefaultHighlight()
    {
        return Color.yellow;
    }
}
