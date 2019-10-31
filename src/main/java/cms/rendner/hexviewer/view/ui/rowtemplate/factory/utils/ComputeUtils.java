package cms.rendner.hexviewer.view.ui.rowtemplate.factory.utils;

import cms.rendner.hexviewer.common.geom.Dimension;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.EMValue;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.FixedValue;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.IValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

/**
 * Utility methods to compute values required for building a row template.
 *
 * @author rendner
 */
public class ComputeUtils
{
    /**
     * Computes the width of a char.
     *
     * @param fontMetrics metrics of the font used to render the text of the rows.
     * @return the width of a char.
     */
    public static int computeCharWidth(@NotNull final FontMetrics fontMetrics)
    {
        return fontMetrics.stringWidth("X");
    }

    /**
     * Computes the height of a char.
     *
     * @param fontMetrics metrics of the font used to render the text of the rows.
     * @return the height of a char.
     */
    public static int computeCharHeight(@NotNull final FontMetrics fontMetrics)
    {
        return fontMetrics.getAscent() + fontMetrics.getDescent();
    }

    /**
     * Computes the height of a row.
     *
     * @param fontMetrics metrics of the font used to render the text of the rows.
     * @return the height of the row.
     */
    public static int computeRowHeight(@NotNull final FontMetrics fontMetrics)
    {
        return fontMetrics.getAscent() + fontMetrics.getDescent() + fontMetrics.getLeading();
    }

    /**
     * Computes the width of a row.
     *
     * @param elementsInRow list of elements displayed in the row.
     * @param rowInsets     the insets for the row.
     * @param fontMetrics   metrics of the font used to render the text of the rows.
     * @return the width of the row.
     */
    public static int computeRowWidth(@NotNull final List<Element> elementsInRow, @NotNull final HInsets rowInsets, @NotNull final FontMetrics fontMetrics)
    {
        final Element lastElement = elementsInRow.get(elementsInRow.size() - 1);
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
    public static Dimension computeElementDimension(final int charsPerElement, @NotNull final FontMetrics fontMetrics)
    {
        return new Dimension(computeCharWidth(fontMetrics) * charsPerElement, computeCharHeight(fontMetrics));
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
    public static int computeValue(@Nullable final IValue value, @NotNull final FontMetrics fontMetrics)
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

    /**
     * Hide constructor.
     */
    private ComputeUtils()
    {
    }
}
