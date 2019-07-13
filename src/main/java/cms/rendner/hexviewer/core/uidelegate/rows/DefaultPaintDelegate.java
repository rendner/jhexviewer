package cms.rendner.hexviewer.core.uidelegate.rows;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.support.data.wrapper.IRowData;
import cms.rendner.hexviewer.support.data.wrapper.RowData;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.color.DefaultRowColorProvider;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.ByteRowRenderer;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.IRowRenderer;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.OffsetRowRenderer;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.RendererContext;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.core.view.geom.Range;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.areas.OffsetRowsView;
import cms.rendner.hexviewer.core.view.areas.RowBasedView;
import cms.rendner.hexviewer.utils.FallbackValue;
import cms.rendner.hexviewer.utils.CheckUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation which paints the rows of the three areas of the {@link JHexViewer}.
 * <p/>
 * This implementation repaints ALWAYS THE WHOLE ROW, even if only the caret should be repainted or single byte was
 * selected. In these cases there is no need to repaint a whole row if only a small part of a row was modified.
 * Repainting only the dirty part of a row is faster, but requires much more logic inside an IRowRenderer
 * to handle all the possible kinds of dirty marked row parts (e.g. start only, end only, middle only or full row)
 * whereby the possible performance gain is lost.
 *
 * @author rendner
 * @see AreaId
 * @see RowBasedView
 */
public class DefaultPaintDelegate implements IPaintDelegate
{
    /**
     * Rectangle instance used to store temporary the height of a single row.
     * This instance is reused.
     */
    protected final Rectangle rvRowRect = new Rectangle();

    /**
     * Range instance used to store temporary the range of the dirty rows.
     * This instance is reused.
     */
    protected final Range rvDirtyRows = new Range();

    /**
     * Contains for each of the three areas a rowColorProvider to be used to color the rows of the area.
     */
    protected final Map<AreaId, FallbackValue<IRowColorProvider>> rowColorProviderMap = new HashMap<>();

    /**
     * Contains for each of the three areas a rowRenderer to be used to render the rows of the area.
     */
    protected final Map<AreaId, FallbackValue<IRowRenderer<? extends IRowTemplate>>> rowRendererMap = new HashMap<>();

    /**
     * The instance which are served by the paint delegate.
     */
    protected JHexViewer hexViewer;

    /**
     * The context which is required by the IRowRenderer instances to paint the rows of the areas.
     */
    protected RendererContext context = new RendererContext();


    @Override
    public void install(final JHexViewer hexViewer)
    {
        CheckUtils.checkNotNull(hexViewer);

        this.hexViewer = hexViewer;
        context.setHexViewer(hexViewer);

        addDefaultRowRenderer(rowRendererMap);
        addDefaultRowColorProvider(rowColorProviderMap);

        if (this.hexViewer.getDamager() != null)
        {
            this.hexViewer.getDamager().damageAllAreas();
        }
    }

    @Override
    public void uninstall(final JHexViewer hexViewer)
    {
        CheckUtils.checkNotNull(hexViewer);

        if (this.hexViewer != null)
        {
            if (this.hexViewer.getDamager() != null)
            {
                this.hexViewer.getDamager().damageAllAreas();
            }

            this.hexViewer = null;
        }

        context.setHexViewer(null);
        rowRendererMap.clear();
        rowColorProviderMap.clear();
    }

    @Override
    public void setRowColorProvider(final AreaId id, final IRowColorProvider newColorProvider)
    {
        CheckUtils.checkNotNull(id);
        rowColorProviderMap.get(id).setPreferredValue(newColorProvider);

        if (hexViewer.getDamager() != null)
        {
            hexViewer.getDamager().damageArea(id);
        }
    }

    @Override
    public void paint(final Graphics g, final RowBasedView rowsView)
    {
        if (rowsView.hasTemplate())
        {
            rowsView.getRowRange(g.getClipBounds(), rvDirtyRows);

            if (!rvDirtyRows.isEmpty())
            {
                copyHexViewerFontInto(rowsView);

                final List<RowGraphicsAndData> rowGraphicsAndDataList = createRowGraphicsAndData(g, rvDirtyRows, rowsView);
                paintDirtyRows(g, rowGraphicsAndDataList, rowsView);
                disposeRowGraphics(rowGraphicsAndDataList);
            }
        }
    }

    /**
     * Adds the default renderer to be used to render the rows of the areas.
     * This method is automatically invoked in the <code>install</code> method and can be overwritten to provide
     * customized implementations.
     *
     * @param rowRendererMap the map to fill.
     */
    protected void addDefaultRowRenderer(final Map<AreaId, FallbackValue<IRowRenderer<? extends IRowTemplate>>> rowRendererMap)
    {
        rowRendererMap.put(AreaId.OFFSET, new FallbackValue<>(new OffsetRowRenderer()));
        rowRendererMap.put(AreaId.HEX, new FallbackValue<>(new ByteRowRenderer()));
        rowRendererMap.put(AreaId.ASCII, new FallbackValue<>(new ByteRowRenderer()));
    }

    /**
     * Adds the default color provider to be used to color the rows of the areas.
     * This method is automatically invoked in the <code>install</code> method and can be overwritten to provide
     * customized implementations.
     *
     * @param rowColorProviderMap the map to fill.
     */
    protected void addDefaultRowColorProvider(final Map<AreaId, FallbackValue<IRowColorProvider>> rowColorProviderMap)
    {
        final IRowColorProvider defaultProvider = new DefaultRowColorProvider();
        rowColorProviderMap.put(AreaId.OFFSET, new FallbackValue<>(defaultProvider));
        rowColorProviderMap.put(AreaId.HEX, new FallbackValue<>(defaultProvider));
        rowColorProviderMap.put(AreaId.ASCII, new FallbackValue<>(defaultProvider));
    }

    /**
     * (Re)paints the dirty rows of the specified view.
     *
     * @param g               the Graphics context in which to paint, which refers to the <code>rowsView</code>.
     * @param rowGraphicsAndDataList list of dirty rows to be repainted.
     *                        Each entry of this list points to a Graphics object with the coordinates and dimension
     *                        to be repainted.
     * @param rowsView        the component being painted.
     */

    protected void paintDirtyRows(final Graphics g, final List<RowGraphicsAndData> rowGraphicsAndDataList, final RowBasedView rowsView)
    {
        try
        {
            if (rowsView instanceof ByteRowsView)
            {
                paintByteRows(g, rowGraphicsAndDataList, (ByteRowsView) rowsView);
            }
            else if (rowsView instanceof OffsetRowsView)
            {
                paintOffsetRows(rowGraphicsAndDataList, (OffsetRowsView) rowsView);
            }
            else
            {
                throw new IllegalArgumentException("Unsupported type of RowBasedView found '" + rowsView.getClass().getSimpleName() + "'.");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * (Re)paints the dirty rows of the offset area.
     * <p/>
     * This method paints the parts of a row in this order:
     * <pre>
     *     <ol>
     *         <li>background</li>
     *         <li>foreground</li>
     *     </ol>
     * </pre>
     *
     * @param rowGraphicsAndDataList list of dirty rows to be repainted.
     *                        Each entry of this list points to a Graphics object with the coordinates and dimension
     *                        to be repainted.
     * @param rowsView        the component being painted.
     */
    protected void paintOffsetRows(final List<RowGraphicsAndData> rowGraphicsAndDataList, final OffsetRowsView rowsView)
    {
        final AreaId areaId = rowsView.getId();
        final IOffsetRowTemplate rowTemplate = rowsView.template();
        final IRowRenderer<IOffsetRowTemplate> renderer = (IRowRenderer<IOffsetRowTemplate>) rowRendererMap.get(areaId).getValue();

        prepareContextForArea(areaId);

        for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
        {
            context.setRowData(rowGraphicsAndData.rowData);
            renderer.paintBackground(rowGraphicsAndData.g, rowTemplate, context);
            renderer.paintForeground(rowGraphicsAndData.g, rowTemplate, context);
        }
    }

    /**
     * (Re)paints the dirty rows of a byte area.
     * <p/>
     * This method paints the parts of a row in this order:
     * <pre>
     *     <ol>
     *         <li>background</li>
     *         <li>highlights</li>
     *         <li>caret</li>
     *         <li>foreground</li>
     *     </ol>
     * </pre>
     *
     * @param g               the Graphics context in which to paint, which refers to the <code>rowsView</code>.
     * @param rowGraphicsAndDataList list of dirty rows to be repainted.
     *                        Each entry of this list points to a Graphics object with the coordinates and dimension
     *                        to be repainted.
     * @param rowsView        the component being painted.
     */
    protected void paintByteRows(final Graphics g, final List<RowGraphicsAndData> rowGraphicsAndDataList, final ByteRowsView rowsView)
    {
        final AreaId areaId = rowsView.getId();
        final IByteRowTemplate rowTemplate = rowsView.template();
        final IRowRenderer<IByteRowTemplate> renderer = (IRowRenderer<IByteRowTemplate>) rowRendererMap.get(areaId).getValue();

        prepareContextForArea(areaId);

        for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
        {
            context.setRowData(rowGraphicsAndData.rowData);
            renderer.paintBackground(rowGraphicsAndData.g, rowTemplate, context);
        }

        final IHighlighter highlighter = hexViewer.getHighlighter();
        if (highlighter != null)
        {
            highlighter.paint(g, rowsView);
        }

        final ICaret caret = hexViewer.getCaret();
        if (caret != null)
        {
            caret.paint(g, rowsView);
        }

        for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
        {
            context.setRowData(rowGraphicsAndData.rowData);
            renderer.paintForeground(rowGraphicsAndData.g, rowTemplate, context);
        }
    }

    /**
     * Sets the properties for the specified area.
     *
     * @param id the area to paint.
     */
    protected void prepareContextForArea(final AreaId id)
    {
        context.setAreaId(id);
        context.setColorProvider(rowColorProviderMap.get(id).getValue());
    }

    /**
     * Creates for each row which intersect with the <code>dirtyRows</code> range a RowGraphicsAndData instance.
     *
     * @param g         the Graphics context in which to paint, which refers to the <code>rowsView</code>.
     * @param dirtyRows a range which describes which rows are dirty and should be repainted.
     * @param rowsView  the component being painted.
     * @return list of RowGraphicsAndData which provide the data used to repaint a row.
     */
    protected List<RowGraphicsAndData> createRowGraphicsAndData(final Graphics g, final Range dirtyRows, final RowBasedView rowsView)
    {
        final List<RowGraphicsAndData> result = new ArrayList<>(dirtyRows.getLength());

        rowsView.getRowRect(dirtyRows.getStart(), rvRowRect);

        for (int rowIndex = dirtyRows.getStart(); rowIndex <= dirtyRows.getEnd(); rowIndex++)
        {
            final Graphics rowGraphics = g.create(rvRowRect.x, rvRowRect.y, rvRowRect.width, rvRowRect.height);
            final IRowData rowData = getRowData(rowIndex);
            result.add(new RowGraphicsAndData(rowGraphics, rowData));

            rvRowRect.y += rvRowRect.height;
        }

        return result;
    }

    /**
     * Creates a row data object which contains only the bytes of the specified row.
     *
     * @param rowIndex the index of the row.
     * @return the bytes of the specified row, never <code>null</code>.
     */
    protected IRowData getRowData(final int rowIndex)
    {
        final RowData result = new RowData(hexViewer.getDataModel(), hexViewer.bytesPerRow());
        result.setRowIndex(rowIndex);
        return result;
    }

    /**
     * Ensures that the specified component has the font of the {@link JHexViewer} component.
     *
     * @param rowsView the view component to adjust.
     */
    protected void copyHexViewerFontInto(final RowBasedView rowsView)
    {
        rowsView.setFont(hexViewer.getFont());
    }

    /**
     * Disposes a list of RowGraphicsAndData objects.
     *
     * @param rowGraphicsAndDataList list to dispose.
     */
    protected void disposeRowGraphics(final List<RowGraphicsAndData> rowGraphicsAndDataList)
    {
        for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
        {
            rowGraphicsAndData.dispose();
        }
    }

    /**
     * Helper object which holds a Graphics instance which has the bounds of a row and the data to be rendered by the row.
     * Drawing outside the bounds of the Graphics instance doesn't damage other rows.
     */
    protected class RowGraphicsAndData
    {
        /**
         * The Graphics instance with the dimension and coordinates of the specified row.
         */
        final Graphics g;

        /**
         * The data to render for the row.
         */
        final IRowData rowData;

        /**
         * Creates a new instance with the specified parameters.
         *
         * @param g     the Graphics instance used to repaint the row.
         * @param rowData the data to render for the row.
         */
        public RowGraphicsAndData(final Graphics g, final IRowData rowData)
        {
            super();

            this.g = g;
            this.rowData = rowData;
        }

        /**
         * Disposes the Graphics instance and releases any system resources that it is using.
         * The Graphics instance cannot be used after dispose has been called.
         */
        public void dispose()
        {
            g.dispose();
        }
    }
}
