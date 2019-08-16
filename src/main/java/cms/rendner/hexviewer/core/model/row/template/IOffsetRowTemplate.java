package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.element.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Describes the layout of a row of the offset-area.
 * The offset area displays usually the offset in the data model of the first byte displayed by the row.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.core.model.data.IDataModel
 */
public interface IOffsetRowTemplate extends IRowTemplate
{
    /**
     * Returns the element which displays the formatted offset value
     *
     * @return the element.
     */
    @NotNull
    Element element();

    /**
     * The number of chars used to display the offset value without any prefix or suffix.
     *
     * @return number of chars.
     */
    int onlyDigitsCount();

    /**
     * The number of chars to display the formatted offset value (including prefix and suffix).
     *
     * @return number of chars.
     */
    int totalCharsCount();
}
