package cms.rendner.hexviewer.core.uidelegate.rows.renderer.context;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import org.jetbrains.annotations.NotNull;

/**
 * Contains data used in a <code>IRowRenderer</code> to support customized rendering of rows.
 *
 * @author rendner
 */
public interface IRendererContext
{
    /**
     * @return the {@link JHexViewer} component.
     */
    @NotNull
    JHexViewer getHexViewer();

    /**
     * @return the color provider to determine the foreground and background color to use.
     */
    @NotNull
    IRowColorProvider getColorProvider();

    /**
     * @return the data of a single row to draw. This data is automatically updated and points always to the bytes of
     * the row to paint. The row data instance always contains all bytes of the row.
     */
    @NotNull
    IRowData getRowData();

    /**
     * @return the id of the area to be rendered.
     */
    @NotNull
    AreaId getAreaId();
}
