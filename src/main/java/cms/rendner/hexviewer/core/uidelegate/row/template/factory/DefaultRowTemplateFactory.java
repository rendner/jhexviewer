package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.*;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.model.row.template.elements.Element;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementDimension;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementPosition;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.view.areas.AreaId;

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
    @Override
    public IOffsetRowTemplate createOffsetTemplate(final JHexViewer hexViewer, final int totalCharsCount, final int onlyDigitsCount)
    {
        context = createFreshContext(hexViewer);

        final RowInsets rowInsets = context.getConfiguration().rowInsets(AreaId.OFFSET);
        final ElementDimension elementDimension = computeElementDimension(totalCharsCount);
        final List<IElement> elements = createOffsetRowElements(rowInsets, elementDimension);
        final IOffsetRowTemplate result = createOffsetRowTemplate(elements, rowInsets, totalCharsCount, onlyDigitsCount);

        return result;
    }

    @Override
    public IByteRowTemplate createHexTemplate(final JHexViewer hexViewer, final int bytesPerRow)
    {
        context = createFreshContext(hexViewer);

        final IRowTemplateConfiguration configuration = context.getConfiguration();
        final int caretWidth = computeValue(configuration.caretWidth());
        final RowInsets rowInsets = configuration.rowInsets(AreaId.HEX);
        final IElement.IDimension byteDimension = computeElementDimension(2);
        final List<IElement> bytes = createHexRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        return result;
    }

    @Override
    public IByteRowTemplate createAsciiTemplate(final JHexViewer hexViewer, final int bytesPerRow)
    {
        context = createFreshContext(hexViewer);

        final IRowTemplateConfiguration configuration = context.getConfiguration();
        final int caretWidth = computeValue(configuration.caretWidth());
        final RowInsets rowInsets = configuration.rowInsets(AreaId.ASCII);
        final IElement.IDimension byteDimension = computeElementDimension(1);
        final List<IElement> bytes = createAsciiRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

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
    protected IByteRowTemplate createByteRowTemplate(final List<IElement> bytes, final RowInsets rowInsets, final int caretWidth)
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
    protected IOffsetRowTemplate createOffsetRowTemplate(final List<IElement> elements, final RowInsets rowInsets, final int totalCharsCount, final int onlyDigitsCount)
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
    protected List<IElement> createOffsetRowElements(final RowInsets rowInsets, final IElement.IDimension elementDimension)
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
    protected List<IElement> createHexRowElements(final RowInsets rowInsets, final IElement.IDimension elementDimension, final int caretWidth)
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
    protected List<IElement> createAsciiRowElements(final RowInsets rowInsets, final IElement.IDimension elementDimension, final int caretWidth)
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
