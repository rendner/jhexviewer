package cms.rendner.hexviewer.core.view.color;

import cms.rendner.hexviewer.core.view.areas.AreaId;

import java.awt.*;

/**
 * Provides colors which can be used to paint customized carets. The usage of such an provider depends on the
 * implementation of the installed ICaret implementation.
 *
 * @author rendner
 */
public interface ICaretColorProvider
{
    /**
     * Provides the color to use to paint the caret.
     *
     * @param areaId  the id of the area in which the caret is painted.
     * @param focused <code>true</code> if the target area is focused.
     * @return the color to user, never <code>null</code>
     */
    Color getCaretColor(AreaId areaId, boolean focused);

    /**
     * Provides the color to use to paint the selection.
     *
     * @param areaId  the id of the area in which the selection is painted.
     * @param focused <code>true</code> if the target area is focused.
     * @return the color to user, never <code>null</code>
     */
    Color getSelectionColor(AreaId areaId, boolean focused);
}
