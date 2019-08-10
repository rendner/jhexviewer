package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * Describes the layout of a row from the offset-area.
 *
 * @author rendner
 */
public class OffsetRowTemplate extends RowTemplate implements IOffsetRowTemplate
{
    /**
     * Number of chars to display the formatted offset value.
     */
    protected final int totalCharsCount;

    /**
     * Number of chars to display only the digits of the formatted offset value.
     */
    protected final int onlyDigitsCount;

    /**
     * Creates a new instance.
     *
     * @param font            the font used to render the text of the rows.
     * @param dimension       the dimension of the row.
     * @param elements        the elements of the row, list contains exact one entry.
     * @param totalCharsCount the number of chars to display the formatted offset value including suffix and prefix (if required).
     * @param onlyDigitsCount the number of chars to display only the digits of the formatted offset value without any
     *                        additional suffix or prefix.
     * @throws IllegalArgumentException if <code>dimension</code> or <code>elements</code> is <code>null</code>
     *                                  or <code>elements</code> contains less or more than <code>1</code> entry.
     */
    public OffsetRowTemplate(@NotNull Font font,
                             @NotNull final IRowTemplate.IDimension dimension,
                             @NotNull final List<IElement> elements,
                             final int totalCharsCount,
                             final int onlyDigitsCount)
    {
        super(font, dimension, elements);

        CheckUtils.checkMaxValue(elements.size(), 1);
        CheckUtils.checkMinValue(totalCharsCount, 1);
        CheckUtils.checkMinValue(onlyDigitsCount, 1);
        CheckUtils.checkMinValue(totalCharsCount, onlyDigitsCount);

        this.totalCharsCount = totalCharsCount;
        this.onlyDigitsCount = onlyDigitsCount;
    }

    @Override
    public int totalCharsCount()
    {
        return totalCharsCount;
    }

    @Override
    public int onlyDigitsCount()
    {
        return onlyDigitsCount;
    }
}
