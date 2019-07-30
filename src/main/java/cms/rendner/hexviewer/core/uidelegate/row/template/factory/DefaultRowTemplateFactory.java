package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.model.row.template.*;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsetValues;
import cms.rendner.hexviewer.core.model.row.template.elements.Element;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementDimension;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementPosition;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.core.JHexViewer;

import java.awt.*;
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

        final RowInsetValues rowInsets = context.getConfiguration().getRowInsets(AreaId.OFFSET);
        final ElementDimension elementDimension = createElementDimension(totalCharsCount);
        final List<IElement> elements = createOffsetRowElements(rowInsets, elementDimension);
        final IOffsetRowTemplate result = createOffsetRowTemplate(elements, rowInsets, totalCharsCount, onlyDigitsCount);

        return result;
    }

    @Override
    public IByteRowTemplate createHexTemplate(final JHexViewer hexViewer, final int bytesPerRow)
    {
        context = createFreshContext(hexViewer);

        final IRowTemplateConfiguration configuration = context.getConfiguration();
        final int caretWidth = resolveValue(configuration.getCaretWidth());
        final RowInsetValues rowInsets = configuration.getRowInsets(AreaId.HEX);
        final IElement.IDimension byteDimension = createElementDimension(2);
        final List<IElement> bytes = createHexRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        return result;
    }

    @Override
    public IByteRowTemplate createAsciiTemplate(final JHexViewer hexViewer, final int bytesPerRow)
    {
        context = createFreshContext(hexViewer);

        final IRowTemplateConfiguration configuration = context.getConfiguration();
        final int caretWidth = resolveValue(configuration.getCaretWidth());
        final RowInsetValues rowInsets = configuration.getRowInsets(AreaId.ASCII);
        final IElement.IDimension byteDimension = createElementDimension(1);
        final List<IElement> bytes = createAsciiRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        return result;
    }

    /**
     * Creates a list with only one element which is rendered row wise in the offset-view of the JHexViewer.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension for the elements to create.
     * @return list with one element.
     */
    protected List<IElement> createOffsetRowElements(final RowInsetValues rowInsets, final IElement.IDimension elementDimension)
    {
        final int x = resolveValue(rowInsets.getLeft());
        final List<IElement> result = new ArrayList<>(1);
        result.add(new Element(elementDimension, new ElementPosition(x, 0)));
        return result;
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
    protected IOffsetRowTemplate createOffsetRowTemplate(final List<IElement> elements, final RowInsetValues rowInsets, final int totalCharsCount, final int onlyDigitsCount)
    {
        final IElement lastElement = elements.get(elements.size() - 1);
        final FontMetrics fm = context.getFontMetrics();

        final int width = lastElement.right() + resolveValue(rowInsets.getRight());
        final int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
        final IRowTemplate.IDimension rowDimension = new RowDimension(width, height);

        final OffsetRowTemplate template = new OffsetRowTemplate(rowDimension, elements, totalCharsCount, onlyDigitsCount);
        template.setAscent(fm.getAscent());
        return template;
    }

    /**
     * Creates a list of elements which are rendered row wise in the ascii-view of the JHexViewer.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension for the elements to create.
     * @param caretWidth       the width for the caret.
     * @return list of elements.
     */
    protected List<IElement> createAsciiRowElements(final RowInsetValues rowInsets, final IElement.IDimension elementDimension, final int caretWidth)
    {
        final IRowTemplateConfiguration configuration = context.getConfiguration();
        final int bytesPerRow = configuration.getBytesPerRow();

        final List<IElement> result = new ArrayList<>();

        int x = resolveValue(rowInsets.getLeft()) + caretWidth;
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new ElementPosition(x, 0)));
            x += elementDimension.width();
            x += caretWidth;
        }

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
    protected List<IElement> createHexRowElements(final RowInsetValues rowInsets, final IElement.IDimension elementDimension, final int caretWidth)
    {
        final IRowTemplateConfiguration configuration = context.getConfiguration();

        final int bytesPerRow = configuration.getBytesPerRow();
        final int bytesPerGroup = configuration.getBytesPerGroup();
        final int spaceBetweenByteGroups = resolveValue(configuration.getSpaceBetweenGroups());

        final List<IElement> result = new ArrayList<>(bytesPerRow);

        int byteIndexInByteGroup = 0;
        int x = resolveValue(rowInsets.getLeft()) + caretWidth;
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new ElementPosition(x, 0)));
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
     * Creates a byte-row template.
     * This method calculates the dimension of the row template depending on the available <code>elements</code>,
     * <code>rowInset</code> and <code>caretWidth</code>. It also sets the ascent of the row.
     *
     * @param bytes      list of bytes to be used for the row.
     * @param rowInsets  the insets of the row.
     * @param caretWidth the width of the caret which can be placed in front of a byte.
     * @return the byte-row template.
     */
    protected IByteRowTemplate createByteRowTemplate(final List<IElement> bytes, final RowInsetValues rowInsets, final int caretWidth)
    {
        final IElement lastByte = bytes.get(bytes.size() - 1);
        final FontMetrics fm = context.getFontMetrics();

        // caret can be placed in front of the first element, for symmetry reasons caret width is also added to the end of the row
        final int width = lastByte.right() + resolveValue(rowInsets.getRight()) + caretWidth;
        final int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
        final IRowTemplate.IDimension rowDimension = new RowDimension(width, height);

        final ByteRowTemplate template = new ByteRowTemplate(rowDimension, bytes, caretWidth);
        template.setAscent(fm.getAscent());
        return template;
    }
}
