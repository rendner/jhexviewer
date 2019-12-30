package cms.rendner.hexviewer.view.ui.rowtemplate.factory.bytes;

import cms.rendner.hexviewer.common.geom.Dimension;
import cms.rendner.hexviewer.common.geom.Position;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.bytes.ByteRowTemplate;
import cms.rendner.hexviewer.common.rowtemplate.bytes.IByteRowTemplate;
import cms.rendner.hexviewer.model.rowtemplate.configuration.TextRowTemplateConfiguration;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.ui.rowtemplate.factory.utils.ComputeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating row templates based on a configuration for the text-area.
 *
 * @author rendner
 */
public class TextRowTemplateFactory
{
    /**
     * Configuration which is temporary stored during the template creation.
     */
    private TextRowTemplateConfiguration configuration;

    /**
     * Creates the row template which defines the layout of the rows for the text-area.
     *
     * @param hexViewer the hexViewer component to which the text-area belongs. Required to query additional properties
     *                  of the {@link JHexViewer}.
     * @return the layout template to render the text-area.
     */
    @Nullable
    public IByteRowTemplate createTemplate(@NotNull final JHexViewer hexViewer)
    {
        IByteRowTemplate result = null;
        configuration = hexViewer.getTextRowTemplateConfiguration().orElse(null);

        if (configuration != null)
        {
            result = hexViewer.getRowContentFont().map(font -> {
                final FontMetrics fontMetrics = hexViewer.getFontMetrics(font);
                final List<Element> elements = createRowElements(hexViewer, fontMetrics);
                return buildTemplate(elements, fontMetrics);
            }).orElse(null);
        }

        configuration = null;
        return result;
    }

    private ByteRowTemplate buildTemplate(@NotNull final List<Element> elements, @NotNull final FontMetrics fontMetrics)
    {
        // Caret can be placed in front of the first byte. This was taken into account in the calculation of the
        // x-position of the first byte, for symmetry reasons caret width is also added after the last byte
        final int caretWidth = ComputeUtils.computeValue(configuration.caretWidth(), fontMetrics);
        final int width = ComputeUtils.computeRowWidth(elements, configuration.insets(), fontMetrics) + caretWidth;
        final int height = ComputeUtils.computeRowHeight(fontMetrics);

        return ByteRowTemplate.newBuilder()
                .dimension(width, height)
                .fontMetrics(fontMetrics)
                .caretWidth(caretWidth)
                .elements(elements)
                .buildUIResource();
    }

    @NotNull
    private List<Element> createRowElements(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fontMetrics)
    {
        final Dimension byteDimension = ComputeUtils.computeElementDimension(1, fontMetrics);

        final int caretWidth = ComputeUtils.computeValue(configuration.caretWidth(), fontMetrics);
        final int bytesPerRow = hexViewer.getBytesPerRow();
        final HInsets rowInsets = configuration.insets();

        final List<Element> result = new ArrayList<>();

        int x = ComputeUtils.computeValue(rowInsets.left(), fontMetrics) + caretWidth;
        for (int i = 0; i < bytesPerRow; i++)
        {
            result.add(new Element(byteDimension, new Position(x, 0)));
            x += byteDimension.width();
            x += caretWidth;
        }

        return result;
    }
}
