package cms.rendner.hexviewer.core.view.areas;

import cms.rendner.hexviewer.core.geom.Range;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.IPaintDelegate;
import cms.rendner.hexviewer.swing.BorderlessJComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;

/**
 * View component which displays the rows of an area of the JHexViewer.
 * <p/>
 * This component doesn't allow to set a border or to add other components as children. The visual representation of
 * the component is painted by an IPaintDelegate which knows what content has to be painted. In order to paint the
 * component an IRowTemplate is required which describes the layout of the rows to be rendered. The layout is taken into
 * account by the IPaintDelegate. Without an IRowTemplate or IPaintDelegate the component will stay empty.
 *
 * @author rendner
 * @see IPaintDelegate
 * @see IRowTemplate
 * @see cms.rendner.hexviewer.core.JHexViewer
 */
public abstract class RowBasedView<T extends IRowTemplate> extends BorderlessJComponent
{
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
     * Constant for an invalid index.
     */
    protected final static int INVALID_INDEX = -1;

    /**
     * The id of the area to which is rendered by this component.
     */
    @NotNull
    private final AreaId id;

    /**
     * Used to guard the access to the internal api.
     */
    @NotNull
    private final Object internalApiAccessToken;

    /**
     * The api instance which allows to set hidden properties which are not accessible from outside.
     * To access this api, an access token is required which is only known by the owner which created the component.
     */
    @Nullable
    protected RowBasedView.InternalApi internalApi;

    /**
     * Describes the layout of a row.
     */
    @Nullable
    protected T rowTemplate;

    /**
     * Number of rows that can be shown.
     * This value is used to calculate the final height of the component.
     */
    private int rowCount;

    /**
     * The delegate used to paint the rows of this view.
     */
    private IPaintDelegate paintDelegate;

    /**
     * Creates a new instance with the specified values.
     *
     * @param id                     the id of the area to which is rendered by this component.
     * @param internalApiAccessToken the token to allow access to the internal api.
     */
    public RowBasedView(@NotNull final AreaId id, @NotNull final Object internalApiAccessToken)
    {
        super();
        this.id = id;
        this.internalApiAccessToken = internalApiAccessToken;
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
        throw new UnsupportedOperationException("This instance can't have children. The content of this component is painted by an IPaintDelegate.");
    }

    /**
     * @return the id of the area rendered by this component.
     */
    @NotNull
    public AreaId getId()
    {
        return id;
    }

    /**
     * @return the row template that describes the layout of a row.
     */
    @NotNull
    public Optional<T> template()
    {
        return Optional.ofNullable(rowTemplate);
    }

    /**
     * @return <code>true</code> if a template is available, otherwise <code>false</code>.
     */
    public boolean hasTemplate()
    {
        return rowTemplate != null;
    }

    /**
     * @return The number of total rows provided by the component.
     */
    public int rowCount()
    {
        return rowCount;
    }

    /**
     * Returns the delegate used to paint the rows of the component.
     *
     * @return the current paint delegate.
     */
    @NotNull
    public Optional<IPaintDelegate> getPaintDelegate()
    {
        return Optional.ofNullable(paintDelegate);
    }

    /**
     * Returns the bounds of a row.
     * <p/>
     * This method always return the rect of the row even if the row specified by the rowIndex is outside of the
     * current component bounds.
     *
     * @param rowIndex the index of the row, &gt;= 0.
     * @return a rectangle with the bounds of the specified row.
     */
    @NotNull
    public Rectangle getRowRect(final int rowIndex)
    {
        final int rowHeight = rowHeight();
        return new Rectangle(
                0,
                rowIndex * rowHeight,
                getWidth(),
                rowHeight);
    }

    /**
     * Returns a range of rows that intersect a rectangle.
     *
     * @param rectangle the rectangle to determine the intersecting rows.
     * @return the range of intersection.
     */
    @NotNull
    public Range getIntersectingRows(@NotNull final Rectangle rectangle)
    {
        if (rectangle.isEmpty())
        {
            return Range.INVALID;
        }

        final int topRowIndex = verticalLocationToRowIndex(rectangle.y);

        if (topRowIndex != INVALID_INDEX)
        {
            int bottomRowIndex = verticalLocationToRowIndex(rectangle.y + rectangle.height - 1);

            if (bottomRowIndex == INVALID_INDEX)
            {
                // in this case use the rowCount
                bottomRowIndex = Math.max(0, rowCount() - 1);
            }

            return new Range(topRowIndex, bottomRowIndex);
        }

        return Range.INVALID;
    }

    /**
     * Returns the index of the row which contains the specified y coordinate.
     *
     * @param yLocation the y value to convert.
     * @return the index of the row, or <code>-1</code> if the specified y value was out of component bounds.
     */
    public int verticalLocationToRowIndex(final int yLocation)
    {
        final int result = yLocation / rowHeight();

        if (result < 0)
        {
            return INVALID_INDEX;
        }
        else if (result >= rowCount())
        {
            return INVALID_INDEX;
        }
        else
        {
            return result;
        }
    }

    /**
     * @return the current height of a single row in this component, &gt;= 1.
     */
    public int rowHeight()
    {
        return rowTemplate != null ? Math.max(1, rowTemplate.height()) : DUMMY_ROW_HEIGHT;
    }

    /**
     * @return the current width of a single row in this component, &gt;= 1.
     */
    public int rowWidth()
    {
        return rowTemplate != null ? Math.max(1, rowTemplate.width()) : DUMMY_ROW_WIDTH;
    }

    @NotNull
    @Override
    public Dimension getPreferredSize()
    {
        final Insets insets = getInsets();

        final int width = rowWidth() + insets.left + insets.right;

        long height = (rowHeight() * rowCount) + insets.top + insets.bottom;
        // check for overflow
        if (height < 1 || height > MAX_HEIGHT)
        {
            height = MAX_HEIGHT;
        }

        return new Dimension(width, (int) height);
    }

    @Override
    protected void paintChildren(@NotNull final Graphics g)
    {
        super.paintChildren(g);

        if (paintDelegate != null)
        {
            if (rowTemplate != null)
            {
                g.setFont(rowTemplate.font());
            }
            paintDelegate.paint(g, this);
        }
    }

    /**
     * Returns the internal api if the access token is valid.
     *
     * @param internalApiAccessToken the token to check if the internal api can be accessed.
     * @return the internal api.
     * @throws IllegalArgumentException if the token isn't authorized to access the internal api.
     */
    @NotNull
    RowBasedView.InternalApi getGuardedInternalApi(@NotNull final Object internalApiAccessToken) throws IllegalArgumentException
    {
        if (this.internalApiAccessToken != internalApiAccessToken)
        {
            throw new IllegalArgumentException("The used token isn't authorized to access the internal api.");
        }

        return Objects.requireNonNull(internalApi);
    }

    /**
     * Internal api.
     *
     * @param rowTemplate the template that describes the layout of a row, can be <code>null</code>.
     * @see InternalApi#setRowTemplate(IRowTemplate)
     */
    void setRowTemplate(@Nullable final T rowTemplate)
    {
        this.rowTemplate = rowTemplate;
        revalidate();
        repaint();
    }

    /**
     * Internal api.
     *
     * @param newRowCount the number of total rows.
     * @see InternalApi#setRowCount(int)
     */
    void setRowCount(final int newRowCount)
    {
        if (rowCount != newRowCount)
        {
            rowCount = newRowCount;
            invalidate();
            revalidate();
            repaint();
        }
    }

    /**
     * Internal api.
     *
     * @param newPaintDelegate the new delegate, can be <code>null</code>.
     * @see InternalApi#setPaintDelegate(IPaintDelegate)
     */
    void setPaintDelegate(@Nullable final IPaintDelegate newPaintDelegate)
    {
        if (paintDelegate != newPaintDelegate)
        {
            paintDelegate = newPaintDelegate;
            repaint();
        }
    }

    /**
     * Allows to set new values for hidden properties.
     * <p/>
     * This concept was implemented to hide the setter methods for important properties. And allow classes from other
     * packages to modify these hidden properties, if they are entitled to do so.
     * Without a guarded access the user could change some of these properties directly which would result in an
     * unexpected state of the JHexViewer. For example, the property "rowCount" has to have the same value for all three
     * row based components displayed by the JHexViewer. This can't be guaranteed if the user can modify this property
     * directly.
     *
     * @author rendner
     */
    public static class InternalApi<V extends RowBasedView<T>, T extends IRowTemplate>
    {
        /**
         * The component to access.
         */
        @NotNull
        protected final V rowView;

        /**
         * Creates a new instance.
         *
         * @param rowView the component to access.
         */
        protected InternalApi(@NotNull final V rowView)
        {
            this.rowView = rowView;
        }

        /**
         * Sets the row template which describes the layout of a row
         * <p/>
         * Setting this property results in a complete repaint.
         *
         * @param rowTemplate the new row layout, can be <code>null</code>.
         */
        public void setRowTemplate(@Nullable final T rowTemplate)
        {
            rowView.setRowTemplate(rowTemplate);
        }

        /**
         * Set the number of total rows.
         * Used to calculate the final height of the component.
         * <p/>
         * Setting this property in a complete repaint.
         *
         * @param rowCount the number of total rows.
         */
        public void setRowCount(final int rowCount)
        {
            rowView.setRowCount(rowCount);
        }

        /**
         * Sets the delegate which is responsible for painting the rows of the component.
         * <p/>
         * Setting a delegate results in a complete repaint.
         *
         * @param newPaintDelegate the new delegate, can be <code>null</code>.
         */
        public void setPaintDelegate(@Nullable final IPaintDelegate newPaintDelegate)
        {
            rowView.setPaintDelegate(newPaintDelegate);
        }
    }
}
