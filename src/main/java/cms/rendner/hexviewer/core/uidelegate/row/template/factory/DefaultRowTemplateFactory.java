package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.model.row.template.*;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.model.row.template.elements.Element;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementDimension;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementPosition;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of a row template factory which supports all properties
 * of the <code>{@link IRowTemplateConfiguration}</code>.
 *
 * @author rendner
 */
public class DefaultRowTemplateFactory extends AbstractRowTemplateFactory
{
    @NotNull
    @Override
    public IOffsetRowTemplate createOffsetTemplate(@NotNull final TemplateFactoryContext context, final int totalCharsCount, final int onlyDigitsCount)
    {
        this.context = context;

        final RowInsets rowInsets = context.getConfiguration().rowInsets(AreaId.OFFSET);
        final ElementDimension elementDimension = computeElementDimension(totalCharsCount);
        final List<IElement> elements = createOffsetRowElements(rowInsets, elementDimension);
        final IOffsetRowTemplate result = createOffsetRowTemplate(elements, rowInsets, totalCharsCount, onlyDigitsCount);

        this.context = null;

        return result;
    }

    @NotNull
    @Override
    public IByteRowTemplate createHexTemplate(@NotNull final TemplateFactoryContext context)
    {
        this.context = context;

        final int caretWidth = computeValue(context.getConfiguration().caretWidth());
        final RowInsets rowInsets = context.getConfiguration().rowInsets(AreaId.HEX);
        final IElement.IDimension byteDimension = computeElementDimension(2);
        final List<IElement> bytes = createHexRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        this.context = null;

        return result;
    }

    @NotNull
    @Override
    public IByteRowTemplate createAsciiTemplate(@NotNull final TemplateFactoryContext context)
    {
        this.context = context;

        final int caretWidth = computeValue(context.getConfiguration().caretWidth());
        final RowInsets rowInsets = context.getConfiguration().rowInsets(AreaId.ASCII);
        final IElement.IDimension byteDimension = computeElementDimension(1);
        final List<IElement> bytes = createAsciiRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        this.context = null;

        return result;
    }

    /**
     * Creates a byte-row template.
     * This method calculates the dimension of the row template depending on the available <code>elements</code>,
     * <code>rowInset</code> and <code>caretWidth</code>. It also sets the ascent of the row.
     *
     * @param bytes      list of bytes to be used for the row.
     * @param rowInsets  the insets of the row.
     * @param caretWidth the width of the caret which can be placed in front of a byte.
     * @return the byte-row template.
     */
    @NotNull
    protected IByteRowTemplate createByteRowTemplate(@NotNull final List<IElement> bytes, @NotNull final RowInsets rowInsets, final int caretWidth)
    {
        // Caret can be placed in front of the first byte. This was taken into account in the calculation of the
        // x-position of the first byte, for symmetry reasons caret width is also added after the last byte
        final int width = computeRowWidth(bytes, rowInsets) + caretWidth;
        final int height = computeRowHeight(rowInsets);
        final IRowTemplate.IDimension rowDimension = new RowDimension(width, height);

        final ByteRowTemplate template = new ByteRowTemplate(rowDimension, bytes, caretWidth);
        template.setAscent(context.getFontMetrics().getAscent());
        return template;
    }

    /**
     * Creates the row template which defines the layout of the rows for the offset-view.
     *
     * @param elements        the elements for the row, list contains exact one entry.
     * @param rowInsets       the insets for the row.
     * @param totalCharsCount the number of chars to display the formatted offset value including suffix and prefix (if required).
     * @param onlyDigitsCount the number of chars to display only the digits of the formatted offset value without any
     *                        additional suffix or prefix.
     * @return the layout template for the offset-view of the JHexViewer.
     */
    @NotNull
    protected IOffsetRowTemplate createOffsetRowTemplate(@NotNull final List<IElement> elements, @NotNull final RowInsets rowInsets, final int totalCharsCount, final int onlyDigitsCount)
    {
        final int width = computeRowWidth(elements, rowInsets);
        final int height = computeRowHeight(rowInsets);
        final IRowTemplate.IDimension rowDimension = new RowDimension(width, height);

        final OffsetRowTemplate template = new OffsetRowTemplate(rowDimension, elements, totalCharsCount, onlyDigitsCount);
        template.setAscent(context.getFontMetrics().getAscent());
        return template;
    }

    /**
     * Creates a list with only one element which is rendered row wise in the offset-view of the JHexViewer.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension for the elements to create.
     * @return list with one element.
     */
    @NotNull
    protected List<IElement> createOffsetRowElements(@NotNull final RowInsets rowInsets, @NotNull final IElement.IDimension elementDimension)
    {
        final int x = computeValue(rowInsets.left());
        final int y = computeValue(rowInsets.top());
        final List<IElement> result = new ArrayList<>(1);
        result.add(new Element(elementDimension, new ElementPosition(x, y)));
        return result;
    }

    /**
     * Creates a list of elements which are rendered row wise in the hex-view of the JHexViewer.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension for the elements to create.
     * @param caretWidth       the width for the caret.
     * @return list of elements.
     */
    @NotNull
    protected List<IElement> createHexRowElements(@NotNull final RowInsets rowInsets, @NotNull final IElement.IDimension elementDimension, final int caretWidth)
    {
        final IRowTemplateConfiguration configuration = context.getConfiguration();

        final int bytesPerRow = configuration.bytesPerRow();
        final int bytesPerGroup = configuration.bytesPerGroup();
        final int spaceBetweenByteGroups = computeValue(configuration.spaceBetweenGroups());

        final List<IElement> result = new ArrayList<>(bytesPerRow);

        int byteIndexInByteGroup = 0;
        int x = computeValue(rowInsets.left()) + caretWidth;
        final int y = computeValue(rowInsets.top());
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new ElementPosition(x, y)));
            x += elementDimension.width();
            x += caretWidth;

            if (++byteIndexInByteGroup >= bytesPerGroup)
            {
                byteIndexInByteGroup = 0;
                x += spaceBetweenByteGroups;
            }
        }

        return result;
    }

    /**
     * Creates a list of elements which are rendered row wise in the ascii-view of the JHexViewer.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension for the elements to create.
     * @param caretWidth       the width for the caret.
     * @return list of elements.
     */
    @NotNull
    protected List<IElement> createAsciiRowElements(@NotNull final RowInsets rowInsets, @NotNull final IElement.IDimension elementDimension, final int caretWidth)
    {
        final IRowTemplateConfiguration configuration = context.getConfiguration();
        final int bytesPerRow = configuration.bytesPerRow();

        final List<IElement> result = new ArrayList<>();

        int x = computeValue(rowInsets.left()) + caretWidth;
        final int y = computeValue(rowInsets.top());
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new ElementPosition(x, y)));
            x += elementDimension.width();
            x += caretWidth;
        }

        return result;
    }
}
