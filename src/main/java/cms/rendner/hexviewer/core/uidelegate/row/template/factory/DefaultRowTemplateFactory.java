package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.ByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.OffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.model.row.template.element.Dimension;
import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.core.model.row.template.element.Position;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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
        final Dimension elementDimension = computeElementDimension(totalCharsCount, fm);
        final Element element = createOffsetRowElement(rowInsets, elementDimension);
        final int width = computeRowWidth(Collections.singletonList(element), rowInsets, fm);
        final int height = computeRowHeight(rowInsets, fm);

        final IOffsetRowTemplate result = OffsetRowTemplate.newBuilder()
                .setFont(configuration.font())
                .setAscent(fm.getAscent())
                .setDimension(width, height)
                .setElement(element)
                .setOnlyDigitsCount(onlyDigitsCount)
                .setTotalCharsCount(totalCharsCount)
                .build();

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
        final Dimension byteDimension = computeElementDimension(2, fm);
        final List<Element> bytes = createHexRowElements(rowInsets, byteDimension, caretWidth);
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
        final Dimension byteDimension = computeElementDimension(1, fm);
        final List<Element> bytes = createAsciiRowElements(rowInsets, byteDimension, caretWidth);
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
    protected IByteRowTemplate createByteRowTemplate(@NotNull final List<Element> bytes, @NotNull final RowInsets rowInsets, final int caretWidth)
    {
        // Caret can be placed in front of the first byte. This was taken into account in the calculation of the
        // x-position of the first byte, for symmetry reasons caret width is also added after the last byte
        final int width = computeRowWidth(bytes, rowInsets, fm) + caretWidth;
        final int height = computeRowHeight(rowInsets, fm);

        return ByteRowTemplate.newBuilder()
                .setFont(configuration.font())
                .setAscent(fm.getAscent())
                .setCaretWidth(caretWidth)
                .setDimension(width, height)
                .setElements(bytes)
                .build();
    }

    /**
     * Creates the single element which displays the formatted offset value.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension of the element to create.
     * @return the element.
     */
    @NotNull
    protected Element createOffsetRowElement(@NotNull final RowInsets rowInsets, @NotNull final Dimension elementDimension)
    {
        final int x = computeValue(rowInsets.left(), fm);
        final int y = computeValue(rowInsets.top(), fm);
        return new Element(elementDimension, new Position(x, y));
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
    protected List<Element> createHexRowElements(@NotNull final RowInsets rowInsets, @NotNull final Dimension elementDimension, final int caretWidth)
    {
        final int bytesPerRow = configuration.bytesPerRow();
        final int bytesPerGroup = configuration.bytesPerGroup();
        final int spaceBetweenByteGroups = computeValue(configuration.spaceBetweenGroups(), fm);

        final List<Element> result = new ArrayList<>(bytesPerRow);

        int byteIndexInByteGroup = 0;
        int x = computeValue(rowInsets.left(), fm) + caretWidth;
        final int y = computeValue(rowInsets.top(), fm);
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new Position(x, y)));
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
    protected List<Element> createAsciiRowElements(@NotNull final RowInsets rowInsets, @NotNull final Dimension elementDimension, final int caretWidth)
    {
        final int bytesPerRow = configuration.bytesPerRow();

        final List<Element> result = new ArrayList<>();

        int x = computeValue(rowInsets.left(), fm) + caretWidth;
        final int y = computeValue(rowInsets.top(), fm);
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(elementDimension, new Position(x, y)));
            x += elementDimension.width();
            x += caretWidth;
        }

        return result;
    }
}
