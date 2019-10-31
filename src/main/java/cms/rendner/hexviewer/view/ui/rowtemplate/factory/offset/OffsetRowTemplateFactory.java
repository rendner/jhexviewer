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
                final int maxOffset = calculateMaxOffset(hexViewer);
                final int totalCharsCount = hexViewer.getOffsetArea().getValueFormatter().format(maxOffset).length();
                final int leadingZeros = Math.max(0, String.valueOf(maxOffset).length() - 1);

                final HInsets rowInsets = configuration.insets();
                final Dimension elementDimension = ComputeUtils.computeElementDimension(totalCharsCount, fontMetrics);
                final Element element = createRowElement(rowInsets, elementDimension, fontMetrics);
                final int width = ComputeUtils.computeRowWidth(Collections.singletonList(element), rowInsets, fontMetrics);
                final int height = ComputeUtils.computeRowHeight(fontMetrics);

                return OffsetRowTemplate.newBuilder()
                        .dimension(width, height)
                        .numberOfLeadingZeros(leadingZeros)
                        .fontMetrics(fontMetrics)
                        .element(element)
                        .buildUIResource();
            }).orElse(null);
        }

        configuration = null;
        return result;
    }

    private int calculateMaxOffset(@NotNull final JHexViewer hexViewer)
    {
        final int lastCaretIndex = hexViewer.getLastPossibleCaretIndex();
        final int minLeadingZeros = configuration.minLeadingZeros();
        final String lastIndexWithLeadingZeros = String.format("%0" + minLeadingZeros + "d", lastCaretIndex);
        return Integer.parseInt(lastIndexWithLeadingZeros.replaceAll("[0-9]", "9"));
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
