package cms.rendner.hexviewer.core.view.areas;

import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementHitInfo;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;
import cms.rendner.hexviewer.core.view.areas.properties.Property;
import cms.rendner.hexviewer.core.view.areas.properties.ProtectedPropertiesProvider;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.IndexUtils;
import cms.rendner.hexviewer.utils.RectangleUtils;

import java.awt.*;

/**
 * Used to display the rows of bytes of the {@link AreaId#HEX} or {@link AreaId#ASCII}.
 * This area is focusable and provides method to get the index of a byte under the mouse position which is useful
 * to start a selection.
 * <p/>
 * This view uses an IByteRowTemplate to layout the row bytes.
 *
 * @author rendner
 * @see IByteRowTemplate
 */
public final class ByteRowsView extends RowBasedView<IByteRowTemplate>
{
    /**
     * Indicates if the view is focused or not.
     */
    private boolean focused;

    /**
     * Creates a new instance with the specified values.
     *
     * @param id                 the id of the area to which is rendered by this view component.
     * @param propertiesProvider used by the {@link cms.rendner.hexviewer.core.JHexViewer} to forward properties which
     *                           should not be accessible outside of this component.
     */
    public ByteRowsView(final AreaId id, final ProtectedPropertiesProvider propertiesProvider)
    {
        super(id, propertiesProvider);

        // to create synthetically during drag outside
        setAutoscrolls(true);
    }

    /**
     * @return <code>true</code> if the component is focused.
     */
    public boolean hasFocus()
    {
        return focused;
    }

    /**
     * Sets the focus state.
     * <p/>
     * This results in a complete repaint of the view.
     *
     * @param focused the new focused state.
     */
    protected void setFocus(final boolean focused)
    {
        if (this.focused != focused)
        {
            this.focused = focused;
            repaint();
        }
    }

    /**
     * Translates the position of an element of a single row into the coordinates within the view.
     *
     * @param rowIndex      the target row index of the row in the view for the element.
     * @param elementBounds the bounds of an element received from an IRowTemplate.
     *                      The result is returned in this Rectangle.
     * @return the adjusted elementBounds parameter.
     */
    public Rectangle translateIntoViewCoordinates(final int rowIndex, final Rectangle elementBounds)
    {
        CheckUtils.checkMinValue(rowIndex, 0);
        CheckUtils.checkNotNull(elementBounds);

        final Rectangle rowRect = getRowRect(rowIndex);
        elementBounds.x += rowRect.x;
        elementBounds.y += rowRect.y;
        return elementBounds;
    }

    /**
     * Returns the bounds for a specific byte.
     * The byte doesn't have to be visible, it can be outside of the current view.
     *
     * @param byteIndex the index of the byte in the view. The value has to be &gt;= 0.
     * @return the bounds in the view, never <code>null</code>.
     * @thows IllegalArgumentException if byteIndex is smaller than 0.
     */
    public Rectangle getByteRect(final int byteIndex)
    {
        return getByteRect(byteIndex, new Rectangle());
    }

    /**
     * Returns the bounds for a specific byte.
     * The byte doesn't have to be visible, it can be outside of the current view.
     *
     * @param byteIndex   the index of the byte in the view. The value has to be &gt;= 0.
     * @param returnValue this rect will be filled with the result.
     * @return the <code>returnValue</code> object.
     * @thows IllegalArgumentException if byteIndex is smaller than 0 or returnValue is <code>null</code>.
     */
    public Rectangle getByteRect(final int byteIndex, final Rectangle returnValue)
    {
        CheckUtils.checkNotNull(returnValue);

        if (rowTemplate == null)
        {
            RectangleUtils.setEmpty(returnValue);
        }
        else
        {
            CheckUtils.checkMinValue(byteIndex, 0);

            final int bytesPerRow = bytesPerRow();
            final int rowIndex = IndexUtils.byteIndexToRowIndex(byteIndex, bytesPerRow);
            final int indexInRow = IndexUtils.byteIndexToIndexInRow(byteIndex, bytesPerRow);
            final IElement byteElement = rowTemplate.element(indexInRow);
            return translateIntoViewCoordinates(rowIndex, byteElement.toRectangle(returnValue));
        }

        return returnValue;
    }

    /**
     * @return the number of bytes per row.
     */
    public int bytesPerRow()
    {
        return rowTemplate == null ? 0 : rowTemplate.elementCount();
    }

    /**
     * Returns the bounds for the caret at a specific index.
     * The index doesn't have to be visible, it can be outside of the current view.
     *
     * @param caretIndex the index of the caret in the view. The value has to be &gt;= 0.
     * @return the bounds in the view, never <code>null</code>.
     * @thows IllegalArgumentException if caretIndex is smaller than 0.
     */
    public Rectangle getCaretRect(final int caretIndex)
    {
        return getCaretRect(caretIndex, new Rectangle());
    }

    /**
     * Returns the bounds for the caret at a specific index.
     * The index doesn't have to be visible, it can be outside of the current view.
     *
     * @param caretIndex  the index of the caret in the view. The value has to be &gt;= 0.
     * @param returnValue this rect will be filled with the result.
     * @return the <code>returnValue</code> object.
     * @thows IllegalArgumentException if caretIndex is smaller than 0 or returnValue is <code>null</code>.
     */
    public Rectangle getCaretRect(final int caretIndex, final Rectangle returnValue)
    {
        if (rowTemplate == null)
        {
            RectangleUtils.setEmpty(returnValue);
        }
        else
        {
            CheckUtils.checkMinValue(caretIndex, 0);

            final int bytesPerRow = bytesPerRow();
            final int indexInRowTemplate = IndexUtils.byteIndexToIndexInRow(caretIndex, bytesPerRow);
            rowTemplate.caretBounds(indexInRowTemplate, returnValue);
            return translateIntoViewCoordinates(IndexUtils.byteIndexToRowIndex(caretIndex, bytesPerRow), returnValue);
        }

        return returnValue;
    }

    /**
     * Does a hit test on the specified location.
     *
     * @param x the x position for the hit test.
     * @param y the y position for the hit test.
     * @return a hit info instance if there was a byte under the position, or <code>null</code> if the position
     * wasn't valid (outside of the view).
     */
    public ByteHitInfo locationToByteHit(final int x, final int y)
    {
        if (rowTemplate != null)
        {
            final int rowIndex = verticalLocationToRowIndex(y);

            if (rowIndex != INVALID_INDEX && rowTemplate.containsX(x))
            {
                final int offsetForFirstByteInRow = (rowIndex * rowTemplate.elementCount());
                final ElementHitInfo hitInfo = rowTemplate.hitTest(x);
                return convert(hitInfo, rowTemplate, offsetForFirstByteInRow);
            }
        }

        return null;
    }

    /**
     * Converts a local element hit info into a global byte hit info.
     *
     * @param templateHitInfo         the hit info retrieved from a IByteRowTemplate.
     * @param rowTemplate             the template which provided the hit info.
     * @param offsetForFirstByteInRow the offset for the first byte of the row which was tested.
     * @return the hit info for the byte, never <code>null</code>.
     */
    protected ByteHitInfo convert(final ElementHitInfo templateHitInfo, final IByteRowTemplate rowTemplate, final int offsetForFirstByteInRow)
    {
        final IndexPosition.Bias bias = templateHitInfo.insertionIndex() >= rowTemplate.elementCount() ? IndexPosition.Bias.Backward : IndexPosition.Bias.Forward;
        final IndexPosition position = new IndexPosition(templateHitInfo.insertionIndex() + offsetForFirstByteInRow, bias);
        return new ByteHitInfo(templateHitInfo.index() + offsetForFirstByteInRow, position);
    }

    @Override
    protected void handleProtectedProperty(final Property changedDependency)
    {
        super.handleProtectedProperty(changedDependency);

        final String type = changedDependency.getName();
        if (Property.FOCUS.equals(type))
        {
            setFocus((Boolean) changedDependency.getValue());
        }
    }

    /**
     * Represents a byte hit information in the layout.
     */
    public static class ByteHitInfo
    {
        /**
         * The index of the byte in the view.
         */
        private final int index;

        /**
         * Describes if the hit was before the byte or after the byte.
         */
        private final IndexPosition insertionPosition;

        /**
         * Creates a new instance with the specified values.
         *
         * @param index             the index of the byte in the view.
         * @param insertionPosition describes where the caret should be inserted (before or after the byte).
         */
        public ByteHitInfo(final int index, final IndexPosition insertionPosition)
        {
            super();
            this.index = index;
            this.insertionPosition = insertionPosition;
        }

        /**
         * @return the position where the caret should be placed.
         */
        public IndexPosition getInsertionPosition()
        {
            return insertionPosition;
        }

        /**
         * @return the index of the byte in the view
         */
        public int getIndex()
        {
            return index;
        }

        /**
         * @return the index of the byte in the view prefixed with the name of the class.
         */
        @Override
        public String toString()
        {
            return getClass().getSimpleName() + "[index:" + index + "]";
        }
    }
}
