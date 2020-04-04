package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphicsBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * Paints the area background row-wise.
 * <p/>
 * This class uses the background colors provided by the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider#getRowBackground(int)}
 * of the area to paint the background row-wise.
 */
public class RowBasedBackgroundPainter implements IAreaBackgroundPainter
{
    /**
     * Is re-used for fetching the bounds of a Graphics2D object.
     */
    @NotNull
    private final Rectangle clipBounds = new Rectangle();

    /**
     * Updated on every paint call - provides colors for rendering the background.
     */
    @Nullable
    private IAreaColorProvider colorProvider;

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        colorProvider = ((Area<?, ?>) component).getColorProvider();

        final List<RowGraphics> rowGraphicsList = RowGraphicsBuilder.buildBackgroundRowGraphics(g, component);
        if (rowGraphicsList.isEmpty())
        {
            return;
        }

        final RowGraphics lastEntry = rowGraphicsList.get(rowGraphicsList.size() - 1);

        rowGraphicsList.forEach(rowGraphics -> {
            paintRow(rowGraphics, component, rowGraphics == lastEntry);
            rowGraphics.dispose();
        });
    }

    /**
     * Paints the background of a row.
     *
     * @param rowGraphics the rowGraphics instance which refers to the row to paint.
     * @param component   a reference to the area. This instance can be casted to the specific area implementation if needed.
     * @param isLastRow   indicates if this is the last row of the area background.
     */
    protected void paintRow(@NotNull final RowGraphics rowGraphics, @NotNull AreaComponent component, final boolean isLastRow)
    {
        rowGraphics.g.setColor(getRowBackground(rowGraphics.rowIndex));
        rowGraphics.g.getClipBounds(clipBounds);
        rowGraphics.g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
    }

    @NotNull
    protected Color getRowBackground(final int rowIndex)
    {
        Color color = null;
        if (colorProvider != null)
        {
            color = colorProvider.getRowBackground(rowIndex);
        }
        return color == null ? Color.WHITE : color;
    }
}
