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
import org.jetbrains.annotations.NotNull;

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
    /**
     * Metrics of the font used to render the text of the rows. To calculate the width, height and position of the row elements.
     */
    private FontMetrics fm;
    /**
     * Describes the layout of the row template to be created.
     */
    private IRowTemplateConfiguration configuration;

    @NotNull
    @Override
    public IOffsetRowTemplate createOffsetTemplate(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fm, @NotNull final IRowTemplateConfiguration configuration)
    {
        this.fm = fm;
        this.configuration = configuration;

        final int onlyDigitsCount = computeCharCountForMaxOffsetAddress(hexViewer);
        final int totalCharsCount = computeTotalCharCountForOffsetAddressRow(hexViewer, onlyDigitsCount);

        final RowInsets rowInsets = configuration.rowInsets(AreaId.OFFSET);
        final ElementDimension elementDimension = computeElementDimension(totalCharsCount, fm);
        final List<IElement> elements = createOffsetRowElements(rowInsets, elementDimension);
        final IOffsetRowTemplate result = createOffsetRowTemplate(elements, rowInsets, totalCharsCount, onlyDigitsCount);

        this.fm = null;
        this.configuration = null;

        return result;
    }

    @NotNull
    @Override
    public IByteRowTemplate createHexTemplate(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fm, @NotNull final IRowTemplateConfiguration configuration)
    {
        this.fm = fm;
        this.configuration = configuration;

        final int caretWidth = computeValue(configuration.caretWidth(), fm);
        final RowInsets rowInsets = configuration.rowInsets(AreaId.HEX);
        final IElement.IDimension byteDimension = computeElementDimension(2, fm);
        final List<IElement> bytes = createHexRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        this.fm = null;
        this.configuration = null;

        return result;
    }

    @NotNull
    @Override
    public IByteRowTemplate createAsciiTemplate(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fm, @NotNull final IRowTemplateConfiguration configuration)
    {
        this.fm = fm;
        this.configuration = configuration;

        final int caretWidth = computeValue(configuration.caretWidth(), fm);
        final RowInsets rowInsets = configuration.rowInsets(AreaId.ASCII);
        final IElement.IDimension byteDimension = computeElementDimension(1, fm);
        final List<IElement> bytes = createAsciiRowElements(rowInsets, byteDimension, caretWidth);
        final IByteRowTemplate result = createByteRowTemplate(bytes, rowInsets, caretWidth);

        this.fm = null;
        this.configuration = null;

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
        final int width = computeRowWidth(bytes, rowInsets, fm) + caretWidth;
        final int height = computeRowHeight(rowInsets, fm);
        final IRowTemplate.IDimension rowDimension = new RowDimension(width, height);

        final ByteRowTemplate template = new ByteRowTemplate(configuration.font(), rowDimension, bytes, caretWidth);
        template.setAscent(fm.getAscent());
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
        final int width = computeRowWidth(elements, rowInsets, fm);
        final int height = computeRowHeight(rowInsets, fm);
        final IRowTemplate.IDimension rowDimension = new RowDimension(width, height);

        final OffsetRowTemplate template = new OffsetRowTemplate(configuration.font(), rowDimension, elements, totalCharsCount, onlyDigitsCount);
        template.setAscent(fm.getAscent());
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
        final int x = computeValue(rowInsets.left(), fm);
        final int y = computeValue(rowInsets.top(), fm);
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
        final int bytesPerRow = configuration.bytesPerRow();
        final int bytesPerGroup = configuration.bytesPerGroup();
        final int spaceBetweenByteGroups = computeValue(configuration.spaceBetweenGroups(), fm);

        final List<IElement> result = new ArrayList<>(bytesPerRow);

        int byteIndexInByteGroup = 0;
        int x = computeValue(rowInsets.left(), fm) + caretWidth;
        final int y = computeValue(rowInsets.top(), fm);
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
        final int bytesPerRow = configuration.bytesPerRow();

        final List<IElement> result = new ArrayList<>();

        int x = computeValue(rowInsets.left(), fm) + caretWidth;
        final int y = computeValue(rowInsets.top(), fm);
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new ElementPosition(x, y)));
            x += elementDimension.width();
            x += caretWidth;
        }

        return result;
    }
}
