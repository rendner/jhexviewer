package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.IElement;

import java.awt.*;
import java.util.List;


/**
 * Describes the layout of a row of bytes.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed in the JHexViewer.
 * For each of the two byte areas ({@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} and
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII}) of the JHexViewer a separate template
 * exists which describes the exact layout of the rows rendered by these two areas.
 *
 * @author rendner
 */
public class ByteRowTemplate extends RowTemplate implements IByteRowTemplate
{
    /**
     * The width of the caret which can be placed between the bytes of the row.
     */
    private final int caretWidth;

    /**
     * Creates a new instance.
     *
     * @param dimension  the dimension of the row.
     * @param elements   the elements which describe the position and bounds of the bytes of the row.
     * @param caretWidth the width of the caret which can be placed between the bytes of the row.
     * @throws IllegalArgumentException if <code>dimension</code> or <code>elements</code> is <code>null</code>
     *                                  Or <code>elements</code> is empty..
     */
    public ByteRowTemplate(final IRowTemplate.IDimension dimension, final List<IElement> elements, final int caretWidth)
    {
        super(dimension, elements);
        this.caretWidth = caretWidth;
    }

    @Override
    public Rectangle caretBounds(final int byteIndex, final Rectangle returnValue)
    {
        final IElement byteAfterCaret = elements.get(byteIndex);
        returnValue.x = byteAfterCaret.x() - caretWidth;
        returnValue.y = byteAfterCaret.y();
        returnValue.width = caretWidth;
        returnValue.height = byteAfterCaret.height();
        return returnValue;
    }
}
