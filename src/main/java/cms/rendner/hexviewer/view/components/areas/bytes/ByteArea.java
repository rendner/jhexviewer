package cms.rendner.hexviewer.view.components.areas.bytes;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.bytes.HitInfo;
import cms.rendner.hexviewer.common.rowtemplate.bytes.IByteRowTemplate;
import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

/**
 * Abstract component which displays the data model of the {@link cms.rendner.hexviewer.view.JHexViewer} rowwise.
 * <p/>
 * The layout of the displayed rows are described by an {@link IByteRowTemplate} which is mandatory to allow
 * rendering the data model of the {@link cms.rendner.hexviewer.view.JHexViewer}.
 *
 * @author rendner
 */
public abstract class ByteArea extends Area<IByteRowTemplate, IByteColorProvider, IValueFormatter>
{
    /**
     * Sole constructor, for invocation by subclass constructors.
     *
     * @param id             the id of the area, {@link AreaId#HEX} or {@link AreaId#ASCII}.
     * @param valueFormatter the formatter used to convert the bytes of the data model of the {@link cms.rendner.hexviewer.view.JHexViewer} into displayable strings.
     */
    protected ByteArea(@NotNull final AreaId id, @NotNull final IValueFormatter valueFormatter)
    {
        super(id, valueFormatter);
    }

    /**
     * Returns the bounds for a specific byte.
     * The byte doesn't have to be visible, it can be outside of the current component bounds.
     *
     * @param byteIndex the index of the byte in the view, &gt;= 0.
     * @return the bounds in the view, the result will be empty if no row-template is installed.
     */
    @NotNull
    public Rectangle getByteRect(final int byteIndex)
    {
        CheckUtils.checkMinValue(byteIndex, 0);

        final IByteRowTemplate rowTemplate = getRowTemplate().orElse(null);
        if (rowTemplate != null)
        {
            final int bytesPerRow = rowTemplate.elementCount();
            final int rowIndex = IndexUtils.byteIndexToRowIndex(byteIndex, bytesPerRow);
            final int indexInRow = IndexUtils.byteIndexToIndexInRow(byteIndex, bytesPerRow);
            if (rowIndex != IndexUtils.INVALID_INDEX && indexInRow != IndexUtils.INVALID_INDEX)
            {
                final Element byteElement = rowTemplate.element(indexInRow);
                return translateIntoViewCoordinates(rowIndex, byteElement);
            }
        }

        return new Rectangle();
    }

    /**
     * Returns the bounds for the caret at a specific index.
     * The index doesn't have to be visible, it can be outside of the current view.
     *
     * @param caretIndex the index of the caret in the view, &gt;= 0.
     * @return the bounds of the caret, the result will be empty if no row-template is installed.
     */
    @NotNull
    public Rectangle getCaretRect(final int caretIndex)
    {
        final IByteRowTemplate rowTemplate = getRowTemplate().orElse(null);
        if (rowTemplate != null)
        {
            CheckUtils.checkMinValue(caretIndex, 0);

            final int bytesPerRow = rowTemplate.elementCount();
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
     * @return a hit info instance if the position was inside the component bounds.
     */
    @NotNull
    public Optional<HitInfo> hitTest(final int x, final int y)
    {
        final IByteRowTemplate rowTemplate = getRowTemplate().orElse(null);

        if (rowTemplate != null)
        {
            final int rowIndex = verticalLocationToRowIndex(y);

            if (rowIndex != INVALID_INDEX && rowTemplate.containsX(x))
            {
                final HitInfo hitInfo = rowTemplate.hitTest(x);
                final int offsetOfFirstByteInRow = (rowIndex * rowTemplate.elementCount());
                return Optional.of(
                        new HitInfo(
                                hitInfo.index() + offsetOfFirstByteInRow,
                                hitInfo.isLeadingEdge(),
                                hitInfo.wasInside()
                        )
                );
            }
        }

        return Optional.empty();
    }
}
