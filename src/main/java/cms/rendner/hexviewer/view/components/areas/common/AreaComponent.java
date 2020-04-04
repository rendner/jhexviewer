package cms.rendner.hexviewer.view.components.areas.common;

import cms.rendner.hexviewer.common.ranges.RowRange;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.components.BorderlessJComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter;
import cms.rendner.hexviewer.view.ui.areas.AreaComponentUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * This component acts as base class for the {@code cms.rendner.hexviewer.view.components.areas.common.Area}.
 * <p/>
 * To provide an easy customizable painting mechanism for the areas, the default row based behavior is moved into this class.
 * Painters {@link IAreaPainter} (used to paint an area) expect an area component instead of an area. This simplifies the
 * method signatures of these painters when overwriting the painting behavior, since the user doesn't have to specify
 * the generic types of an Area.
 *
 * @author rendner
 */
public abstract class AreaComponent extends BorderlessJComponent
{
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
     * The id of the area to which the component belongs.
     */
    @NotNull
    private final AreaId areaId;

    /**
     * Creates a new instance.
     *
     * @param areaId The id of the area to which the component belongs.
     */
    public AreaComponent(final @NotNull AreaId areaId)
    {
        this.areaId = areaId;
        updateUI();
    }

    /**
     * @return The id of the area. Used to identify a specific area.
     */
    @NotNull
    public AreaId getAreaId()
    {
        return areaId;
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
     * * Returns a range of rows that intersect an area.
     *
     * @param y      the y position to determine the intersecting rows.
     * @param height the height of the area to determine the intersecting rows.
     * @return the range of intersection.
     */
    @NotNull
    public RowRange getIntersectingRows(final int y, final int height)
    {
        final int topRowIndex = verticalLocationToRowIndex(y);

        if (topRowIndex != INVALID_INDEX)
        {
            int bottomRowIndex = verticalLocationToRowIndex(y + height);

            if (bottomRowIndex == INVALID_INDEX)
            {
                // in this case use the rowCount
                bottomRowIndex = Math.max(0, getRowCount() - 1);
            }

            return new RowRange(topRowIndex, bottomRowIndex);
        }

        return RowRange.INVALID;
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

        return getIntersectingRows(rectangle.y, rectangle.height);
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
        long height = (getRowHeight() * getRowCount());

        // check for overflow
        if (height < 1 || height > MAX_HEIGHT)
        {
            height = MAX_HEIGHT;
        }

        return new Dimension(width, (int) height);
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
     * @return the height of a single row.
     */
    public abstract int getRowHeight();

    /**
     * @return the width of a single row.
     */
    public abstract int getRowWidth();

    /**
     * @return the number of rows &gt;= 1, displayed by this component.
     */
    public abstract int getRowCount();

    /**
     * @return the painter responsible for painting the content of the area.
     */
    @Nullable
    public abstract IAreaPainter getPainter();

    /**
     * Returns the suffix used to construct the name of the L&amp;F class used to
     * render this component.
     *
     * @return the string "AreaComponentUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @NotNull
    @Override
    public String getUIClassID()
    {
        return "AreaComponentUI";
    }

    public AreaComponentUI getUI()
    {
        return (AreaComponentUI) ui;
    }

    /**
     * Resets the UI property to a value from the current look-and-feel.
     * If no UI was installed to the {@link UIManager}, the {@link AreaComponentUI}
     * will be used.
     *
     * @see #setUI
     * @see UIManager#getLookAndFeel
     * @see UIManager#getUI
     */
    @Override
    public void updateUI()
    {
        if (UIManager.get(getUIClassID()) == null)
        {
            // if no ui is installed, install the default one
            UIManager.put(getUIClassID(), AreaComponentUI.class.getName());
        }

        setUI(UIManager.getUI(this));
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
        else if (result >= getRowCount())
        {
            return INVALID_INDEX;
        }
        else
        {
            return result;
        }
    }

    /**
     * Not implemented, throws an exception.
     *
     * @param comp        ignored
     * @param constraints ignored
     * @param index       ignored
     */
    @Override
    protected final void addImpl(final Component comp, final Object constraints, final int index)
    {
        throw new UnsupportedOperationException("This instance can't have children. The content of this component is painted by an 'IAreaPainter' instance.");
    }
}
