package cms.rendner.hexviewer.view.components.areas.common;

import cms.rendner.hexviewer.common.ranges.RowRange;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.IRowTemplate;
import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter;
import cms.rendner.hexviewer.view.ui.container.BorderlessJComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Optional;

/**
 * Abstract component which displays content rowwise.
 * <p/>
 * An area is rendered by an {@link IAreaPainter} which has to be set to show content at all.
 *
 * @param <T> the row-template describing the layout of the rows displayed by the area.
 * @param <P> the color provider used by several other classes to allow customizing of the used colors.
 * @author rendner
 */
public abstract class Area<
        T extends IRowTemplate,
        P extends IAreaColorProvider
        > extends BorderlessJComponent
{
    /**
     * Constant used to determine when the <code>painter</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_PAINTER = "painter";

    /**
     * Constant used to determine when the <code>colorProvider</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_COLOR_PROVIDER = "colorProvider";

    /**
     * Constant used to determine when the <code>rowTemplate</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_ROW_TEMPLATE = "rowTemplate";

    /**
     * Constant used to determine when the <code>rowCount</code> property has changed.
     */
    @NotNull
    public static final String PROPERTY_ROW_COUNT = "rowCount";

    /**
     * Constant representing an invalid index.
     */
    protected final static int INVALID_INDEX = IndexUtils.INVALID_INDEX;

    /**
     * Max possible component height.
     * Content which is located behind Integer.MAX_VALUE can't be rendered in Swing components.
     */
    private final static int MAX_HEIGHT = Integer.MAX_VALUE - 100;

    /**
     * Dummy row height which is used when no rowTemplate is available.
     */
    private final static int DUMMY_ROW_HEIGHT = 25;

    /**
     * Dummy row width which is used when no rowTemplate is available.
     */
    private final static int DUMMY_ROW_WIDTH = 100;

    /**
     * describes the layout of the rows of the area.
     */
    @Nullable
    private T rowTemplate;

    /**
     * Used by the painter and several other components to allow customizing of the used colors.
     */
    @Nullable
    private P colorProvider;

    /**
     * The id of the area.
     */
    @NotNull
    private final AreaId id;

    /**
     * Is used to paint the whole area component.
     */
    @Nullable
    private IAreaPainter painter;

    /**
     * The number of rows, displayed by this component, which contains data.
     */
    private int rowCount;

    /**
     * Creates a new instance with the provided values.
     *
     * @param id the id of the area.
     */
    protected Area(@NotNull final AreaId id)
    {
        this.id = id;
    }

    /**
     * @return The id of the area. Used to identify a specific area.
     */
    @NotNull
    public AreaId getId()
    {
        return id;
    }

    /**
     * Not implemented, throws an exception.
     *
     * @param comp        ignored
     * @param constraints ignored
     * @param index       ignored
     */
    @Override
    protected void addImpl(final Component comp, final Object constraints, final int index)
    {
        throw new UnsupportedOperationException("This instance can't have children. The content of this component is painted by an 'IAreaPainter' instance.");
    }

    /**
     * @return the number of rows &gt;= 1, displayed by this component, which contains data.
     */
    public int getRowCount()
    {
        return rowCount;
    }

    /**
     * Sets the number of rows of displayable content, &gt;= 1.
     * <p/>
     * Setting a new row count results in a revalidate and repaint of the component.
     * <p/>
     * A PropertyChange event {@link Area#PROPERTY_ROW_COUNT} is fired when a new row count is set.
     *
     * @param rowCount number of displayable rows.
     */
    public void setRowCount(final int rowCount)
    {
        CheckUtils.checkMinValue(rowCount, 1);
        if (this.rowCount != rowCount)
        {
            final int oldValue = this.rowCount;
            this.rowCount = rowCount;
            firePropertyChange(PROPERTY_ROW_COUNT, oldValue, this.rowCount);
            invalidate();
            revalidate();
            repaint();
        }
    }

    /**
     * Sets the new color provider of the area.
     * A color provider allows to exchange the colors used during the paint process.
     * <p/>
     * Setting a new provider results in a repaint of the component.
     * <p/>
     * A PropertyChange event {@link Area#PROPERTY_COLOR_PROVIDER} is fired when a new color provider is set.
     *
     * @param colorProvider the new color provider, passing <code>null</code> forces the installed ui-delegate to use
     *                      default colors for rendering the content of the area.
     */
    public void setColorProvider(@Nullable final P colorProvider)
    {
        if (this.colorProvider != colorProvider)
        {
            final P oldValue = this.colorProvider;
            this.colorProvider = colorProvider;
            firePropertyChange(PROPERTY_COLOR_PROVIDER, oldValue, this.colorProvider);
            repaint();
        }
    }

    /**
     * Returns the color provider of the area.
     *
     * @return the color provider used to color the rendered content of the area.
     */
    @NotNull
    public Optional<P> getColorProvider()
    {
        return Optional.ofNullable(colorProvider);
    }

    /**
     * @return the painter responsible for painting the content of the area.
     */
    @NotNull
    public Optional<IAreaPainter> getPainter()
    {
        return Optional.ofNullable(painter);
    }

    /**
     * Sets the new painter which is responsible for painting the content of the area component.
     * <p/>
     * Setting a new painter results in a repaint of the component.
     * <p/>
     * A PropertyChange event {@link Area#PROPERTY_PAINTER} is fired when a new painter is set.
     *
     * @param painter the new painter, if <code>null</code> no content can be drawn.
     */
    public void setPainter(@Nullable final IAreaPainter painter)
    {
        final IAreaPainter oldValue = this.painter;
        this.painter = painter;
        firePropertyChange(PROPERTY_PAINTER, oldValue, this.painter);
        repaint();
    }

    /**
     * Calculates the preferred size for rendering the data model of the area.
     *
     * @return the preferred size of the area.
     */
    @Override
    public Dimension getPreferredSize()
    {
        final int width = getRowWidth();
        long height = (getRowHeight() * rowCount);

        // check for overflow
        if (height < 1 || height > MAX_HEIGHT)
        {
            height = MAX_HEIGHT;
        }

        return new Dimension(width, (int) height);
    }

    /**
     * Damages a row of the area (marks a row for repainting).
     *
     * @param rowIndex the index of the row which should be damaged.
     */
    public void damageRow(final int rowIndex)
    {
        repaint(getRowRect(rowIndex));
    }

    /**
     * Returns a range of rows that intersect a rectangle.
     *
     * @param rectangle the rectangle to determine the intersecting rows.
     * @return the range of intersection.
     */
    @NotNull
    public RowRange getIntersectingRows(@NotNull final Rectangle rectangle)
    {
        if (rectangle.isEmpty())
        {
            return RowRange.INVALID;
        }

        final int topRowIndex = verticalLocationToRowIndex(rectangle.y);

        if (topRowIndex != INVALID_INDEX)
        {
            int bottomRowIndex = verticalLocationToRowIndex(rectangle.y + rectangle.height - 1);

            if (bottomRowIndex == INVALID_INDEX)
            {
                // in this case use the rowCount
                bottomRowIndex = Math.max(0, rowCount - 1);
            }

            return new RowRange(topRowIndex, bottomRowIndex);
        }

        return RowRange.INVALID;
    }

    /**
     * Returns the bounds of the requested row.
     * <p/>
     * This method always return the rect of the row even if the row specified by the rowIndex is outside of the
     * current component bounds.
     *
     * @param rowIndex the index of the row, &gt;= 0.
     * @return a rectangle with the bounds of the specified row.
     */
    @NotNull
    public final Rectangle getRowRect(final int rowIndex)
    {
        final int rowHeight = getRowHeight();
        return new Rectangle(
                0,
                rowIndex * rowHeight,
                getWidth(),
                rowHeight);
    }

    /**
     * Sets the new row-template.
     * The template describes the layout of the rows to paint.
     *
     * @param rowTemplate the new template, setting <code>null</code> will result in an empty area.
     */
    public void setRowTemplate(@Nullable final T rowTemplate)
    {
        if (this.rowTemplate != rowTemplate)
        {
            final T oldValue = this.rowTemplate;
            this.rowTemplate = rowTemplate;
            firePropertyChange(PROPERTY_ROW_TEMPLATE, oldValue, this.rowTemplate);
            revalidate();
            repaint();
        }
    }

    /**
     * Returns the row-template that describes the layout of the rows of the area.
     *
     * @return the row-template for the area.
     */
    @NotNull
    public Optional<T> getRowTemplate()
    {
        return Optional.ofNullable(rowTemplate);
    }

    /**
     * Returns the height of a single row.
     * <p/>
     * The height depends on the currently applied <code>rowTemplate</code>. If no <code>rowTemplate</code> is set
     * a default height of  {@link Area#DUMMY_ROW_HEIGHT} is returned.
     *
     * @return the height of a single row.
     */
    public final int getRowHeight()
    {
        return rowTemplate == null ? DUMMY_ROW_HEIGHT : rowTemplate.height();
    }

    /**
     * Returns the width of a single row.
     * <p/>
     * The width depends on the currently applied <code>rowTemplate</code>. If no <code>rowTemplate</code> is set
     * a default width of {@link Area#DUMMY_ROW_WIDTH} is returned.
     *
     * @return the width of a single row.
     */
    public final int getRowWidth()
    {
        return rowTemplate == null ? DUMMY_ROW_WIDTH : rowTemplate.width();
    }

    /**
     * Translates the position of an element of a row into the coordinates within the view.
     * <p/>
     * This method always return the translated position even if the row specified by the rowIndex is outside of the
     * current component bounds.
     *
     * @param rowIndex the target row index of the row in the component for the element.
     * @param element  the element inside the row specified by the rowIndex.
     * @return the translated coordinates.
     */
    @NotNull
    protected Rectangle translateIntoViewCoordinates(final int rowIndex, @NotNull final Element element)
    {
        final Rectangle rowRect = getRowRect(rowIndex);
        return new Rectangle(
                rowRect.x + element.x(),
                rowRect.y + element.y(),
                element.width(),
                element.height()
        );
    }

    /**
     * Translates the position of an element of a single row into the coordinates within the view.
     * <p/>
     * This method always return the translated position even if the row specified by the rowIndex is outside of the
     * current component bounds.
     *
     * @param rowIndex      the target row index of the row in the component for the element.
     * @param elementBounds the relative bounds of an element inside the row specified by the rowIndex.
     * @return the adjusted elementBounds parameter.
     */
    @NotNull
    protected Rectangle translateIntoViewCoordinates(final int rowIndex, @NotNull final Rectangle elementBounds)
    {
        final Rectangle rowRect = getRowRect(rowIndex);
        return new Rectangle(
                rowRect.x + elementBounds.x,
                rowRect.y + elementBounds.y,
                elementBounds.width,
                elementBounds.height
        );
    }

    /**
     * Returns the index of the row which contains the specified y coordinate.
     *
     * @param yLocation the y value to convert.
     * @return the index of the row, or <code>-1</code> if the specified y value was out of component bounds.
     */
    protected int verticalLocationToRowIndex(final int yLocation)
    {
        final int result = yLocation / getRowHeight();

        if (result < 0)
        {
            return INVALID_INDEX;
        }
        else if (result >= rowCount)
        {
            return INVALID_INDEX;
        }
        else
        {
            return result;
        }
    }

    @Override
    protected void paintComponent(@NotNull final Graphics g)
    {
        final Color bgColor = getBackground();
        if (bgColor != null)
        {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        super.paintComponent(g);

        if (painter != null)
        {
            painter.paint((Graphics2D) g);
        }
    }
}
