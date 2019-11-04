package cms.rendner.hexviewer.view.ui.rowtemplate.factory.offset;

import cms.rendner.hexviewer.common.geom.Dimension;
import cms.rendner.hexviewer.common.geom.Position;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.common.rowtemplate.offset.OffsetRowTemplate;
import cms.rendner.hexviewer.model.rowtemplate.configuration.OffsetRowTemplateConfiguration;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.ui.rowtemplate.factory.utils.ComputeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collections;

/**
 * Factory for creating row templates based on a configuration for the offset-area.
 *
 * @author rendner
 */
public final class OffsetRowTemplateFactory
{
    /**
     * Configuration which is temporary stored during the template creation.
     */
    private OffsetRowTemplateConfiguration configuration;

    /**
     * Creates the row template which defines the layout of the rows for the offset-area.
     *
     * @param hexViewer the hexViewer component to which the offset-area belongs. Required to query additional properties
     *                  of the {@link JHexViewer}.
     * @return the layout template to render the offset-area.
     */
    @Nullable
    public IOffsetRowTemplate createTemplate(@NotNull final JHexViewer hexViewer)
    {
        IOffsetRowTemplate result = null;
        configuration = hexViewer.getOffsetRowTemplateConfiguration().orElse(null);

        if (configuration != null)
        {
            result = hexViewer.getRowContentFont().map(font -> {
                final FontMetrics fontMetrics = hexViewer.getFontMetrics(font);
                final int padSize = calculateRequiredPadSize(hexViewer);
                final int totalCharsCount = hexViewer.getOffsetArea().getValueFormatter().calculateFormattedValueLength(padSize, 1);
                final HInsets rowInsets = configuration.insets();
                final Dimension elementDimension = ComputeUtils.computeElementDimension(totalCharsCount, fontMetrics);
                final Element element = createRowElement(rowInsets, elementDimension, fontMetrics);
                final int width = ComputeUtils.computeRowWidth(Collections.singletonList(element), rowInsets, fontMetrics);
                final int height = ComputeUtils.computeRowHeight(fontMetrics);

                return OffsetRowTemplate.newBuilder()
                        .dimension(width, height)
                        .padSize(padSize)
                        .fontMetrics(fontMetrics)
                        .element(element)
                        .buildUIResource();
            }).orElse(null);
        }

        configuration = null;
        return result;
    }

    /**
     * Calculates the pad size depending on the size of the data model of the {@link JHexViewer}.
     *
     * @param hexViewer the hexViewer component to which the offset-area belongs.
     * @return the pad size.
     */
    private int calculateRequiredPadSize(@NotNull final JHexViewer hexViewer)
    {
        final int digitsOfLastCaretIndex = Integer.toHexString(hexViewer.getLastPossibleCaretIndex()).length();
        return Math.max(digitsOfLastCaretIndex, configuration.minPadSize());
    }

    /**
     * Creates the single element which displays the formatted offset value.
     *
     * @param rowInsets        the row insets.
     * @param elementDimension the dimension of the element to create.
     * @param fontMetrics      metrics of the font used to render the text of the row.
     * @return the element.
     */
    @NotNull
    private Element createRowElement(@NotNull final HInsets rowInsets, @NotNull final Dimension elementDimension, @NotNull final FontMetrics fontMetrics)
    {
        final int x = ComputeUtils.computeValue(rowInsets.left(), fontMetrics);
        return new Element(elementDimension, new Position(x, 0));
    }
}
