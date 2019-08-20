package cms.rendner.hexviewer.core.uidelegate.rows;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.geom.Range;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.damager.IDamager;
import cms.rendner.hexviewer.core.uidelegate.rows.color.DefaultRowColorProvider;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.ByteRowRenderer;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.IRowRenderer;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.OffsetRowRenderer;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.RendererContext;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.areas.OffsetRowsView;
import cms.rendner.hexviewer.core.view.areas.RowBasedView;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.support.data.wrapper.RowData;
import cms.rendner.hexviewer.support.data.wrapper.RowDataBuilder;
import cms.rendner.hexviewer.utils.FallbackValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
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
     * Contains for each of the three areas a rowColorProvider to be used to color the rows of the area.
     */
    @NotNull
    protected final Map<AreaId, FallbackValue<IRowColorProvider>> rowColorProviderMap = new EnumMap<>(AreaId.class);

    /**
     * Contains for each of the three areas a rowRenderer to be used to render the rows of the area.
     */
    @NotNull
    protected final Map<AreaId, FallbackValue<IRowRenderer<? extends IRowTemplate>>> rowRendererMap = new EnumMap<>(AreaId.class);

    /**
     * The instance which are served by the paint delegate.
     */
    protected JHexViewer hexViewer;

    /**
     * The context which is required by the IRowRenderer instances to paint the rows of the areas.
     */
    protected RendererContext context;


    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
        context = new RendererContext(hexViewer);

        addDefaultRowRenderer(rowRendererMap);
        addDefaultRowColorProvider(rowColorProviderMap);

        hexViewer.getDamager().ifPresent(IDamager::damageAllAreas);
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        context = null;
        rowRendererMap.clear();
        rowColorProviderMap.clear();
        this.hexViewer = null;

        hexViewer.getDamager().ifPresent(IDamager::damageAllAreas);
    }

    @Override
    public void setRowColorProvider(@NotNull final AreaId id, @Nullable final IRowColorProvider newColorProvider)
    {
        rowColorProviderMap.get(id).setPreferredValue(newColorProvider);
        hexViewer.getDamager().ifPresent(damager -> damager.damageArea(id));
    }

    @Override
    public void paint(@NotNull final Graphics g, @NotNull final RowBasedView<? extends IRowTemplate> rowsView)
    {
        if (rowsView.hasTemplate())
        {
            final Range dirtyRows = rowsView.getIntersectingRows(g.getClipBounds());

            if (dirtyRows.isValid())
            {
                final List<RowGraphicsAndData> rowGraphicsAndDataList = createRowGraphicsAndData(g, dirtyRows, rowsView);
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
    protected void addDefaultRowRenderer(@NotNull final Map<AreaId, FallbackValue<IRowRenderer<? extends IRowTemplate>>> rowRendererMap)
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
    protected void addDefaultRowColorProvider(@NotNull final Map<AreaId, FallbackValue<IRowColorProvider>> rowColorProviderMap)
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

    protected void paintDirtyRows(@NotNull final Graphics g, @NotNull final List<RowGraphicsAndData> rowGraphicsAndDataList, @NotNull final RowBasedView rowsView)
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
    protected void paintOffsetRows(@NotNull final List<RowGraphicsAndData> rowGraphicsAndDataList, @NotNull final OffsetRowsView rowsView)
    {
        rowsView.template().ifPresent(rowTemplate -> {
            final AreaId areaId = rowsView.getId();
            @SuppressWarnings("unchecked")
            final IRowRenderer<IOffsetRowTemplate> renderer = (IRowRenderer<IOffsetRowTemplate>) rowRendererMap.get(areaId).getValue();

            prepareContextForArea(areaId);

            for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
            {
                context.setRowData(rowGraphicsAndData.rowData);
                renderer.paintBackground(rowGraphicsAndData.g, rowTemplate, context);
                renderer.paintForeground(rowGraphicsAndData.g, rowTemplate, context);
            }
        });
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
    protected void paintByteRows(@NotNull final Graphics g, @NotNull final List<RowGraphicsAndData> rowGraphicsAndDataList, @NotNull final ByteRowsView rowsView)
    {
        rowsView.template().ifPresent(rowTemplate -> {
            final AreaId areaId = rowsView.getId();
            @SuppressWarnings("unchecked")
            final IRowRenderer<IByteRowTemplate> renderer = (IRowRenderer<IByteRowTemplate>) rowRendererMap.get(areaId).getValue();

            prepareContextForArea(areaId);

            for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
            {
                context.setRowData(rowGraphicsAndData.rowData);
                renderer.paintBackground(rowGraphicsAndData.g, rowTemplate, context);
            }

            hexViewer.getHighlighter().ifPresent(highlighter -> highlighter.paint(g, rowsView));
            hexViewer.getCaret().ifPresent(caret -> caret.paint(g, rowsView));

            for (final RowGraphicsAndData rowGraphicsAndData : rowGraphicsAndDataList)
            {
                context.setRowData(rowGraphicsAndData.rowData);
                renderer.paintForeground(rowGraphicsAndData.g, rowTemplate, context);
            }
        });
    }

    /**
     * Sets the properties for the specified area.
     *
     * @param id the area to paint.
     */
    protected void prepareContextForArea(@NotNull final AreaId id)
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
    @NotNull
    protected List<RowGraphicsAndData> createRowGraphicsAndData(@NotNull final Graphics g, @NotNull final Range dirtyRows, @NotNull final RowBasedView rowsView)
    {
        final List<RowGraphicsAndData> result = new ArrayList<>(dirtyRows.getLength());

        hexViewer.getDataModel().ifPresent(dataModel -> {
            final Rectangle dirtyRowBounds = rowsView.getRowRect(dirtyRows.getStart());

            final RowDataBuilder rowDataBuilder = new RowDataBuilder(dataModel, hexViewer.bytesPerRow());
            for (int rowIndex = dirtyRows.getStart(); rowIndex <= dirtyRows.getEnd(); rowIndex++)
            {
                final Graphics rowGraphics = g.create(dirtyRowBounds.x, dirtyRowBounds.y, dirtyRowBounds.width, dirtyRowBounds.height);
                result.add(new RowGraphicsAndData(rowGraphics, rowDataBuilder.build(rowIndex)));

                dirtyRowBounds.y += dirtyRowBounds.height;
            }
        });

        return result;
    }

    /**
     * Disposes a list of RowGraphicsAndData objects.
     *
     * @param rowGraphicsAndDataList list to dispose.
     */
    protected void disposeRowGraphics(@NotNull final List<RowGraphicsAndData> rowGraphicsAndDataList)
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
    protected static class RowGraphicsAndData
    {
        /**
         * The Graphics instance with the dimension and coordinates of the specified row.
         */
        @NotNull
        final Graphics g;

        /**
         * The data to render for the row.
         */
        @NotNull
        final RowData rowData;

        /**
         * Creates a new instance with the specified parameters.
         *
         * @param g     the Graphics instance used to repaint the row.
         * @param rowData the data to render for the row.
         */
        public RowGraphicsAndData(@NotNull final Graphics g, @NotNull final RowData rowData)
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
