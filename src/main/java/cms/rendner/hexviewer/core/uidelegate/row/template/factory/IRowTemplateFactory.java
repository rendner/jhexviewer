package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Factory which creates row templates used to render the row based content of the JHexViewer.
 * Each row in the JHexViewer is painted by using a row template which describes how the elements in a row are aligned.
 *
 * @author rendner
 */
public interface IRowTemplateFactory
{
    /**
     * Determines if the row template for the offset area should be recreated.
     * This method is called by the JHexViewer to determine that the row template of the offset can display the max
     * possible offset address. The max possible offset address depends on the size of the IDataModel
     * displayed by the JHexViewer.
     * <p/>
     * This method should also take into account the <code>IOffsetValueFormatter</code> used to format the text displayed
     * by the offset-area.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @return <code>true</code> if the row template should be recreated, otherwise <code>false</code>
     * @see cms.rendner.hexviewer.core.model.data.IDataModel
     * @see cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter
     */
    boolean shouldOffsetRowTemplateRecreated(@NotNull JHexViewer hexViewer);

    /**
     * Creates the row template which defines the layout of the rows for the offset-view.
     *
     * @param hexViewer     reference to the {@link JHexViewer} component.
     * @param fontMetrics   metrics of the font used to render the text of the rows. To calculate the width, height and position of the row elements.
     * @param configuration describes the layout of the row template to be created.
     * @return the layout template for the offset-view of the JHexViewer.
     */
    @NotNull
    IOffsetRowTemplate createOffsetTemplate(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fontMetrics, @NotNull final IRowTemplateConfiguration configuration);

    /**
     * Creates the row template which defines the layout of the rows for the hex-view.
     *
     * @param hexViewer     reference to the {@link JHexViewer} component.
     * @param fontMetrics   metrics of the font used to render the text of the rows. To calculate the width, height and position of the row elements.
     * @param configuration describes the layout of the row template to be created.
     * @return the layout template for the hex-view of the JHexViewer.
     */
    @NotNull
    IByteRowTemplate createHexTemplate(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fontMetrics, @NotNull final IRowTemplateConfiguration configuration);

    /**
     * Creates the row template which defines the layout of the rows for the ascii-view.
     *
     * @param hexViewer     reference to the {@link JHexViewer} component.
     * @param fontMetrics   metrics of the font used to render the text of the rows. To calculate the width, height and position of the row elements.
     * @param configuration describes the layout of the row template to be created.
     * @return the layout template for the ascii-view of the JHexViewer.
     */
    @NotNull
    IByteRowTemplate createAsciiTemplate(@NotNull final JHexViewer hexViewer, @NotNull final FontMetrics fontMetrics, @NotNull final IRowTemplateConfiguration configuration);
}
