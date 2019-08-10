package cms.rendner.hexviewer.core.view.areas;

import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.IPaintDelegate;
import cms.rendner.hexviewer.core.view.areas.properties.Property;
import cms.rendner.hexviewer.core.view.areas.properties.ProtectedPropertiesProvider;
import cms.rendner.hexviewer.core.view.geom.Range;
import cms.rendner.hexviewer.swing.BorderlessJComponent;
import cms.rendner.hexviewer.utils.observer.IObservable;
import cms.rendner.hexviewer.utils.observer.IObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Optional;

/**
 * View component which displays the rows of an area of the JHexViewer.
 * <p/>
 * This component doesn't allow to set an border or to add other components as children. The visual representation of
 * the component is painted by an IPaintDelegate which knows what content has to be painted. In order to paint the
 * component an IRowTemplate is required to determine the expected layout of the rows. The layout is taken into account
 * by the IPaintDelegate. Without an IRowTemplate and IPaintDelegate the component will stay empty.
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
    protected final static int MAX_HEIGHT = Integer.MAX_VALUE - 100;

    /**
     * Dummy row height which is used when no rowTemplate is available.
     */
    protected final static int DUMMY_ROW_HEIGHT = 25;

    /**
     * Dummy row width which is used when no rowTemplate is available.
     */
    protected final static int DUMMY_ROW_WIDTH = 100;

    /**
     * Constant for an invalid index.
     */
    protected final static int INVALID_INDEX = -1;

    /**
     * The id of the area to which is rendered by this view component.
     */
    @NotNull
    protected final AreaId id;

    /**
     * The template to use to render the bytes in rows.
     */
    @Nullable
    protected T rowTemplate;

    /**
     * The number of total rows provided by the view.
     */
    protected int rowCount;

    /**
     * The delegate used to paint the rows of this view.
     */
    private IPaintDelegate paintDelegate;

    /**
     * Used by the {@link cms.rendner.hexviewer.core.JHexViewer} to forward properties which
     * should not be accessible outside of this component.
     */
    @NotNull
    private final ProtectedPropertiesProvider propertiesProvider;

    /**
     * Creates a new instance with the specified values.
     *
     * @param id                 the id of the area to which is rendered by this view component.
     * @param propertiesProvider used by the {@link cms.rendner.hexviewer.core.JHexViewer} to forward properties which
     *                           should not be accessible outside of this component.
     */
    public RowBasedView(@NotNull final AreaId id, @NotNull final ProtectedPropertiesProvider propertiesProvider)
    {
        super();
        this.id = id;
        this.propertiesProvider = propertiesProvider;
        propertiesProvider.addObserver(new InternalHandler());
    }

    /**
     * Not implement, throws an exception.
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
     * @return the id of the area to which is rendered by this view component.
     */
    @NotNull
    public AreaId getId()
    {
        return id;
    }

    /**
     * @return the row template used to align the bytes in rows.
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
     * @return The number of total rows provided by the view.
     * This isn't the number of currently displayed rows.
     * For example this value can be 123456 but this view displays currently 5 rows at a time.
     */
    public int rowCount()
    {
        return rowCount;
    }

    /**
     * Returns the delegate which paints the rows of the view.
     *
     * @return the current paint delegate or <code>null</code> if no delegate was set.
     */
    @Nullable
    public IPaintDelegate getPaintDelegate()
    {
        return paintDelegate;
    }

    /**
     * Returns the bounds of a row.
     *
     * @param rowIndex the index of the row.
     * @return a rectangle with the bounds of the specified row.
     */
    @NotNull
    public Rectangle getRowRect(final int rowIndex)
    {
        return getRowRect(rowIndex, new Rectangle());
    }

    /**
     * Returns the bounds of a row.
     *
     * @param rowIndex the index of the row.
     * @param result   the result is applied to this rectangle.
     * @return the modified result which contains the bounds of the specified row.
     */
    @NotNull
    public Rectangle getRowRect(final int rowIndex, @NotNull final Rectangle result)
    {
        final int rowHeight = rowHeight();

        result.x = 0;
        result.y = rowIndex * rowHeight;
        result.width = getWidth();
        result.height = rowHeight;

        return result;
    }

    /**
     * Returns a range of rows that intersect a rectangle.
     *
     * @param rectangle the rectangle to determine the intersecting rows.
     * @return a range of intersection rows.
     */
    @NotNull
    public Range getRowRange(@NotNull final Rectangle rectangle)
    {
        return getRowRange(rectangle, new Range());
    }

    /**
     * Returns a range of rows that intersect a rectangle.
     *
     * @param rectangle the rectangle to determine the intersecting rows.
     * @param result    the result is applied to this rectangle.
     * @return the modified result which reports the intersection rows.
     */
    @NotNull
    public Range getRowRange(@NotNull final Rectangle rectangle, @NotNull final Range result)
    {
        result.invalidate();

        if (!rectangle.isEmpty())
        {
            final int topRowIndex = verticalLocationToRowIndex(rectangle.y);

            if (topRowIndex != INVALID_INDEX)
            {
                int bottomRowIndex = verticalLocationToRowIndex(rectangle.y + rectangle.height - 1);

                if (bottomRowIndex == INVALID_INDEX)
                {
                    // in this case use the rowCount
                    bottomRowIndex = Math.max(0, rowCount() - 1);
                }

                result.resize(topRowIndex, bottomRowIndex);
            }
        }

        return result;
    }

    /**
     * Returns the index of the row which contains the specified y coordinate.
     *
     * @param yLocation the y value to convert.
     * @return the index of the row, or <code>-1</code> if the specified y value was out of view bounds.
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
     * @return the current height of a single row in this view, &gt;= 1.
     */
    public int rowHeight()
    {
        return rowTemplate != null ? Math.max(1, rowTemplate.height()) : DUMMY_ROW_HEIGHT;
    }

    @NotNull
    @Override
    public Dimension getPreferredSize()
    {
        final Insets insets = getInsets();

        int width = (rowTemplate != null) ? rowTemplate.width() : DUMMY_ROW_WIDTH;
        width += insets.left + insets.right;

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
     * Sets the row template.
     * Setting this property results in a complete repaint.
     *
     * @param rowTemplate the template to use to align the bytes in rows, can be <code>null</code>.
     */
    protected void setRowTemplate(@Nullable final T rowTemplate)
    {
        this.rowTemplate = rowTemplate;
        revalidate();
        repaint();
    }

    /**
     * Set the number of total rows.
     * Setting this property in a complete repaint.
     *
     * @param newRowCount the number of total rows.
     */
    protected void setRowCount(final int newRowCount)
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
     * Sets the delegate which is responsible for painting the rows of the view.
     * Setting a delegate results in a complete repaint.
     *
     * @param newPaintDelegate the new delegate, can be <code>null</code>.
     */
    protected void setPaintDelegate(@Nullable final IPaintDelegate newPaintDelegate)
    {
        if (paintDelegate != newPaintDelegate)
        {
            paintDelegate = newPaintDelegate;
            repaint();
        }
    }

    /**
     * Updates the internal properties.
     *
     * @param changedDependency a changed property forwarded from the {@link cms.rendner.hexviewer.core.JHexViewer}.
     */
    protected void handleProtectedProperty(@NotNull final Property changedDependency)
    {
        switch (changedDependency.getName())
        {
            case Property.PAINT_DELEGATE:
            {
                setPaintDelegate((IPaintDelegate) changedDependency.getValue());
                break;
            }
            case Property.ROW_COUNT:
            {
                setRowCount((Integer) changedDependency.getValue());
                break;
            }
            case Property.ROW_TEMPLATE:
            {
                setRowTemplate((T) changedDependency.getValue());
                break;
            }
            default:
            {
                // ignore...
            }
        }
    }

    /**
     * Internal handler which listens for changes properties.
     */
    private class InternalHandler implements IObserver<Property>
    {
        @Override
        public void update(@NotNull final IObservable<Property> observable, @NotNull final Property property)
        {
            if (propertiesProvider == observable)
            {
                if (property.isTarget(id))
                {
                    handleProtectedProperty(property);
                }
            }
        }
    }
}
