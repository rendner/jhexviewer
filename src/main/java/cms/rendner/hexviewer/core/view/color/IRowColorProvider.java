package cms.rendner.hexviewer.core.view.color;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Provides colors which can be used to paint customized colored rows. The usage of such an provider depends on the
 * implementation of the IRowRenderer used to paint the rows.
 * <p/>
 * Color providers can be set via {@link JHexViewer#setRowColorProvider} and are automatically forwarded to the
 * IRendererContext which is available in the in installed IRowRenderer.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext
 * @see cms.rendner.hexviewer.core.uidelegate.rows.renderer.IRowRenderer
 */
public interface IRowColorProvider
{
    /**
     * Returns the color to use for the row background.
     *
     * @param hexViewer the {@link JHexViewer} component.
     * @param areaId    the id of the area to be rendered.
     * @param rowIndex  the index of the row to draw.
     * @return the color to use.
     */
    @Nullable
    Color getRowBackground(@NotNull JHexViewer hexViewer, @NotNull AreaId areaId, int rowIndex);

    /**
     * Returns the color to use for the element background.
     *
     * @param hexViewer    the {@link JHexViewer} component.
     * @param areaId       the id of the area to be rendered.
     * @param rowIndex     the index of the row to draw.
     * @param elementIndex the index of the element to draw.
     * @return the color to use.
     */
    @Nullable
    Color getRowElementBackground(@NotNull JHexViewer hexViewer, @NotNull AreaId areaId, int rowIndex, int elementIndex);

    /**
     * Returns the color to use for the element foreground.
     *
     * @param hexViewer    the {@link JHexViewer} component.
     * @param areaId       the id of the area to be rendered.
     * @param rowIndex     the index of the row to draw.
     * @param elementIndex the index of the element to draw.
     * @return the color to use.
     */
    @Nullable
    Color getRowElementForeground(@NotNull JHexViewer hexViewer, @NotNull AreaId areaId, int rowIndex, int elementIndex);
}
