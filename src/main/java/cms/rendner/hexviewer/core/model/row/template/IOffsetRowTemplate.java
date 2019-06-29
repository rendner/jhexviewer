package cms.rendner.hexviewer.core.model.row.template;

/**
 * Describes the layout of a row of the offset-area.
 * The offset area display usually the index of a line.
 *
 * @author rendner
 * @see IRowTemplate
 */
public interface IOffsetRowTemplate extends IRowTemplate
{
    /**
     * The number of elements in the row.
     *
     * @return number of elements, always 1.
     */
    int elementCount();

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
