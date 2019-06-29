package cms.rendner.hexviewer.core.uidelegate.rows.renderer.context;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;

/**
 * Contains data used in a <code>IRowRenderer</code> to support customized rendering of the rows of an area.
 *
 * @author rendner
 */
public class RendererContext implements IRendererContext
{
    /**
     * The hexViewer component.
     */
    private JHexViewer hexViewer;

    /**
     * The color provider used to determine colors of an row.
     */
    private IRowColorProvider colorProvider;

    /**
     * The data to display for a specific row.
     */
    private IRowData rowData;

    /**
     * The area to which the context belongs.
     */
    private AreaId areaId;

    @Override
    public JHexViewer getHexViewer()
    {
        return hexViewer;
    }

    /**
     * A reference to the {@link JHexViewer} component.
     *
     * @param hexViewer the reference.
     */
    public void setHexViewer(final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
    }

    @Override
    public IRowColorProvider getColorProvider()
    {
        return colorProvider;
    }

    /**
     * Sets the color provider to use during the rendering of a row.
     *
     * @param colorProvider color provider for the row to paint.
     */
    public void setColorProvider(final IRowColorProvider colorProvider)
    {
        this.colorProvider = colorProvider;
    }

    @Override
    public IRowData getRowData()
    {
        return rowData;
    }

    /**
     * The data of the row to be painted next.
     *
     * @param rowData data of the row to paint.
     */
    public void setRowData(final IRowData rowData)
    {
        this.rowData = rowData;
    }

    @Override
    public AreaId getAreaId()
    {
        return areaId;
    }

    /**
     * Sets the id of the area to be rendered.
     *
     * @param areaId area to be rendered.
     */
    public void setAreaId(final AreaId areaId)
    {
        this.areaId = areaId;
    }
}
