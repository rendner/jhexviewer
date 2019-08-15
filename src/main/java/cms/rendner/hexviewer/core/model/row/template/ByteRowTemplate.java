package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import org.jetbrains.annotations.NotNull;

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
     * @param font       the font used to render the text of the rows.
     * @param dimension  the dimension of the row.
     * @param elements   non empty list of elements which describe the position and bounds of the bytes of the row.
     * @param caretWidth the width of the caret which can be placed between the bytes of the row.
     */
    public ByteRowTemplate(@NotNull Font font, @NotNull final IRowTemplate.IDimension dimension, @NotNull final List<IElement> elements, final int caretWidth)
    {
        super(font, dimension, elements);
        this.caretWidth = caretWidth;
    }

    @NotNull
    @Override
    public Rectangle caretBounds(final int byteIndex)
    {
        final IElement byteAfterCaret = elements.get(byteIndex);
        return new Rectangle(
                byteAfterCaret.x() - caretWidth,
                byteAfterCaret.y(),
                caretWidth,
                byteAfterCaret.height()
        );
    }
}
