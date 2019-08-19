package cms.rendner.hexviewer.core.view.areas;

import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.core.model.row.template.element.HitInfo;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.IndexUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

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
     * @param id                     the id of the area to which is rendered by this view component.
     * @param internalApiAccessToken the token to allow access to the owner api.
     */
    public ByteRowsView(@NotNull final AreaId id, @NotNull final Object internalApiAccessToken)
    {
        super(id, internalApiAccessToken);
        internalApi = new InternalApi(this);

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
     * Translates the position of an element of a single row into the coordinates within the view.
     *
     * @param rowIndex      the target row index of the row in the view for the element.
     * @param elementBounds the relative bounds of an element inside the row specified by the rowIndex.
     * @return the adjusted elementBounds parameter.
     */
    @NotNull
    public Rectangle translateIntoViewCoordinates(final int rowIndex, @NotNull final Rectangle elementBounds)
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
     * Translates the position of an element of a row into the coordinates within the view.
     *
     * @param rowIndex the target row index of the row in the view for the element.
     * @param element  the element inside the row specified by the rowIndex.
     * @return the translated coordinates.
     */
    @NotNull
    public Rectangle translateIntoViewCoordinates(final int rowIndex, @NotNull Element element)
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
     * Returns the bounds for a specific byte.
     * The byte doesn't have to be visible, it can be outside of the current view.
     *
     * @param byteIndex the index of the byte in the view. The value has to be &gt;= 0.
     * @return the bounds in the view, the result will be empty if no row-template is installed.
     */
    @NotNull
    public Rectangle getByteRect(final int byteIndex)
    {
        if (rowTemplate != null)
        {
            final int bytesPerRow = bytesPerRow();
            final int rowIndex = IndexUtils.byteIndexToRowIndex(byteIndex, bytesPerRow);
            final int indexInRow = IndexUtils.byteIndexToIndexInRow(byteIndex, bytesPerRow);
            final Element byteElement = rowTemplate.element(indexInRow);
            return translateIntoViewCoordinates(rowIndex, byteElement);
        }

        return new Rectangle();
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
     * @return the bounds of the caret, the result will be empty if no row-template is installed.
     */
    @NotNull
    public Rectangle getCaretRect(final int caretIndex)
    {
        if (rowTemplate != null)
        {
            CheckUtils.checkMinValue(caretIndex, 0);

            final int bytesPerRow = bytesPerRow();
            final int indexInRowTemplate = IndexUtils.byteIndexToIndexInRow(caretIndex, bytesPerRow);
            final Rectangle caretBounds = rowTemplate.caretBounds(indexInRowTemplate);
            return translateIntoViewCoordinates(IndexUtils.byteIndexToRowIndex(caretIndex, bytesPerRow), caretBounds);
        }

        return new Rectangle();
    }

    /**
     * Does a hit test on the specified location.
     *
     * @param x the x position for the hit test.
     * @param y the y position for the hit test.
     * @return a hit info instance if there was a byte under the position, is empty if the position
     * wasn't valid (outside of the view). The index of the line which contains
     * the byte can be determined by the helper methods of the {@link cms.rendner.hexviewer.core.JHexViewer}.
     * @see cms.rendner.hexviewer.core.JHexViewer#byteIndexToRowIndex(int)
     * @see cms.rendner.hexviewer.core.JHexViewer#byteIndexToIndexInRow(int)
     * @see cms.rendner.hexviewer.core.JHexViewer#rowIndexToByteIndex(int)
     */
    @NotNull
    public Optional<HitInfo> hitTest(final int x, final int y)
    {
        if (rowTemplate != null)
        {
            final int rowIndex = verticalLocationToRowIndex(y);

            if (rowIndex != INVALID_INDEX && rowTemplate.containsX(x))
            {
                final HitInfo hitInfo = rowTemplate.hitTest(x);
                final int offsetForFirstByteInRow = (rowIndex * rowTemplate.elementCount());
                return Optional.of(
                        new HitInfo(
                                hitInfo.index() + offsetForFirstByteInRow,
                                hitInfo.isLeadingEdge(),
                                hitInfo.wasInside()
                        )
                );
            }
        }

        return Optional.empty();
    }

    /**
     * Allows access to the internal api.
     * <p/>
     * Note:
     * If the passed token doesn't match with the expected token an IllegalArgumentException will be thrown.
     * This method should only be called by the {@link cms.rendner.hexviewer.core.JHexViewer}.
     *
     * @param internalApiAccessToken the token to access the internal api.
     * @return the internal api.
     */
    @NotNull
    public ByteRowsView.InternalApi getInternalApi(@NotNull final Object internalApiAccessToken)
    {
        return (InternalApi) getGuardedInternalApi(internalApiAccessToken);
    }

    /**
     * Internal api.
     *
     * @param focused the new focused state.
     * @see InternalApi#setFocus(boolean)
     */
    private void setFocus(final boolean focused)
    {
        if (this.focused != focused)
        {
            this.focused = focused;
            repaint();
        }
    }

    /**
     * Allows to set the hidden properties.
     *
     * @see RowBasedView.InternalApi
     */
    public static class InternalApi extends RowBasedView.InternalApi<ByteRowsView, IByteRowTemplate>
    {
        /**
         * Creates a new instance.
         *
         * @param rowView the view to access.
         */
        InternalApi(@NotNull final ByteRowsView rowView)
        {
            super(rowView);
        }

        /**
         * Sets the focus state.
         * <p/>
         * This results in a complete repaint of the view.
         *
         * @param focused the new focused state.
         */
        public void setFocus(final boolean focused)
        {
            rowView.setFocus(focused);
        }
    }
}
