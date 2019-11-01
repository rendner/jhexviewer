package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphics;
import cms.rendner.hexviewer.view.components.areas.common.painter.graphics.RowGraphicsBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * Paints the area background rowwise.
 * <p/>
 * This class uses the background colors provided by the {@link cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider#getRowBackground(int)}
 * of the area to paint the background rowwise.
 *
 * @param <A> the type of the area which is painted by this class.
 */
public class RowBasedBackgroundPainter<A extends Area<?, ?, ?>> implements IAreaBackgroundPainter
{
    /**
     * Is re-used for fetching the bounds of a Graphics2D object.
     */
    @NotNull
    private final Rectangle clipBounds = new Rectangle();

    /**
     * The area to be painted by this instance.
     */
    @NotNull
    protected final A area;

    /**
     * Updated on every paint call - provides colors for rendering the offset addresses.
     */
    private IAreaColorProvider colorProvider;

    /**
     * Creates a new instance responsible for painting the background for this area.
     * <p/>
     * Creating an instance of this class doesn't automatically registers this instance for painting the area background.
     *
     * @param area the area to be painted.
     */
    public RowBasedBackgroundPainter(@NotNull final A area)
    {
        super();
        this.area = area;
    }

    @Override
    public void paint(final @NotNull Graphics2D g)
    {
        colorProvider = area.getColorProvider().orElse(null);

        final boolean canPaint = colorProvider != null;
        if (!canPaint)
        {
            return;
        }

        final List<RowGraphics> rowGraphicsList = RowGraphicsBuilder.buildBackgroundRowGraphics(g, area);
        if (rowGraphicsList.isEmpty())
        {
            return;
        }

        final RowGraphics lastEntry = rowGraphicsList.get(rowGraphicsList.size() - 1);

        rowGraphicsList.forEach(rowGraphics -> {
            paintRow(rowGraphics, rowGraphics == lastEntry);
            rowGraphics.dispose();
        });
    }

    /**
     * Paints the background of a row.
     *
     * @param rowGraphics the rowGraphics instance which belongs to the row to paint.
     * @param isLastRow   indicates if this is the last row of the area background.
     */
    protected void paintRow(@NotNull final RowGraphics rowGraphics, final boolean isLastRow)
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
        return color == null ? Color.white : color;
    }
}
