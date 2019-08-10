package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.FixedValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementDimension;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * Abstract class, provides methods that can be helpful in creating row templates.
 *
 * @author rendner
 */
public abstract class AbstractRowTemplateFactory implements IRowTemplateFactory
{
    @Override
    public boolean shouldOffsetRowTemplateRecreated(@NotNull final JHexViewer hexViewer)
    {
        return hexViewer.getOffsetRowsView().template().map(rowTemplate -> {
            final int digitOffsetCharCount = computeCharCountForMaxOffsetAddress(hexViewer);
            final int totalOffsetCharCount = computeTotalCharCountForOffsetAddressRow(hexViewer, digitOffsetCharCount);
            return (rowTemplate.onlyDigitsCount() != digitOffsetCharCount ||
                    rowTemplate.totalCharsCount() != totalOffsetCharCount);
        }).orElse(Boolean.TRUE);
    }

    /**
     * Computes the number of required chars to display only the value for the max offset address.
     * This number should include separators between the digits (if used), but no additional prefix and suffix.
     * <p/>
     * The number of chars to display an address depends on the offset formatter registered to the JHexViewer.
     * For example the address 123456 (dec system):
     * <ul>
     * <li>formatted value in hex: "1E240", requires 5 chars</li>
     * <li>formatted value in hex: "1E240:", requires 5 chars - ":" is a suffix and doesn't count</li>
     * <li>formatted value in hex: "1E240h:", requires 5 chars - "h:" is a suffix and doesn't count</li>
     * <li>formatted value in dec: "123456", requires 6 chars</li>
     * <li>formatted value in dec: "123 456", requires 7 chars</li>
     * <li>formatted value in bin: "11110001001000000", requires 17 chars</li>
     * </ul>
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @return the number of required chars to display the max possible offset address without any prefix or suffix.
     */
    protected int computeCharCountForMaxOffsetAddress(@NotNull final JHexViewer hexViewer)
    {
        final IOffsetValueFormatter offsetValueFormatter = hexViewer.getOffsetValueFormatter();
        final int formattedDigitsCount = offsetValueFormatter.computeNumberOfCharsForAddress(hexViewer.lastPossibleCaretIndex());
        return Math.max(formattedDigitsCount, offsetValueFormatter.minNumberOfCharsForAddress());
    }

    /**
     * Computes to total number of chars used to render the max offset address, including suffix and prefix (if used).
     *
     * @param hexViewer      reference to the {@link JHexViewer} component.
     * @param requiredDigits the number of required chars to display only the number of the max offset address.
     * @return the number of chars used to render the max offset address.
     */
    protected int computeTotalCharCountForOffsetAddressRow(@NotNull final JHexViewer hexViewer, final int requiredDigits)
    {
        final IOffsetValueFormatter offsetValueFormatter = hexViewer.getOffsetValueFormatter();
        return offsetValueFormatter.format(requiredDigits, 0).length();
    }

    /**
     * Computes the width of a char.
     *
     * @param fontMetrics metrics of the font used to render the text of the rows.
     * @return the width of a char.
     */
    protected int computeCharWidth(@NotNull final FontMetrics fontMetrics)
    {
        return fontMetrics.stringWidth("X");
    }

    /**
     * Computes the height of a char.
     *
     * @param fontMetrics metrics of the font used to render the text of the rows.
     * @return the height of a char.
     */
    protected int computeCharHeight(@NotNull final FontMetrics fontMetrics)
    {
        return fontMetrics.getAscent() + fontMetrics.getDescent();
    }

    /**
     * Computes the height of a row.
     *
     * @param rowInsets   the row insets to use for the calculation.
     * @param fontMetrics metrics of the font used to render the text of the rows.
     * @return the height of the row.
     */
    protected int computeRowHeight(@NotNull final RowInsets rowInsets, @NotNull final FontMetrics fontMetrics)
    {
        final int heightWithoutInsets = fontMetrics.getAscent() + fontMetrics.getDescent() + fontMetrics.getLeading();
        return heightWithoutInsets + computeValue(rowInsets.top(), fontMetrics) + computeValue(rowInsets.bottom(), fontMetrics);
    }

    /**
     * Computes the width of a row.
     *
     * @param elementsInRow list of elements displayed in the row.
     * @param rowInsets     the insets for the row.
     * @param fontMetrics   metrics of the font used to render the text of the rows.
     * @return the width of the row.
     */
    protected int computeRowWidth(@NotNull final List<IElement> elementsInRow, @NotNull final RowInsets rowInsets, @NotNull final FontMetrics fontMetrics)
    {
        final IElement lastElement = elementsInRow.get(elementsInRow.size() - 1);
        return lastElement.right() + computeValue(rowInsets.right(), fontMetrics);
    }

    /**
     * Creates a dimension instance with the width and height of an element.
     *
     * @param charsPerElement number of chars/digits displayed inside the bounds of the element.
     * @param fontMetrics     metrics of the font used to render the text of the rows.
     * @return the calculated dimension.
     */
    @NotNull
    protected ElementDimension computeElementDimension(final int charsPerElement, @NotNull final FontMetrics fontMetrics)
    {
        return new ElementDimension(computeCharWidth(fontMetrics) * charsPerElement, computeCharHeight(fontMetrics));
    }

    /**
     * Calculates the final value of a <code>IValue</code>.
     * Some of the IValue implementations return a double instead of an int, such values will be converted into an int
     * value by using Math.round.
     *
     * @param value       the value to calculate the the real value.
     * @param fontMetrics metrics of the font used to render the text of the rows. Used to compute the final value for
     *                    <code>EMValue</code> instances.
     * @return the result, if <code>IValue</code> is <code>null</code> the result will be <code>0</code>.
     */
    protected int computeValue(@Nullable final IValue value, @NotNull final FontMetrics fontMetrics)
    {
        double result = 0.0d;

        if (value instanceof EMValue)
        {
            result = ((EMValue) value).resolve(computeCharHeight(fontMetrics));
        }
        else if (value instanceof FixedValue)
        {
            result = ((FixedValue) value).value();
        }

        return (int) Math.round(result);
    }
}
