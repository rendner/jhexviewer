package cms.rendner.hexviewer.common.rowtemplate.offset;

import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.IRowTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Describes the layout of a row of the offset-area.
 * The offset-area displays usually the offset in the data model of the first byte displayed by the row.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.model.data.IDataModel
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
     * @return the number of chars included in the element dimension for displaying a padded offset value.
     */
    int padSize();
}
