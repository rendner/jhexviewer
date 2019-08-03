package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Factory which creates row templates which are used to render rows in the row based views of the JHexViewer.
 *
 * @author rendner
 */
public interface IRowTemplateFactory
{
    /**
     * Creates the row template which defines the layout of the rows for the offset-view.
     *
     * @param hexViewer       reference to the {@link JHexViewer} component.
     * @param totalCharsCount the number of chars to display the formatted offset value including suffix and prefix (if required).
     * @param onlyDigitsCount the number of chars to display only the digits of the formatted offset value without any
     *                        additional suffix or prefix.
     * @return the layout template for the offset-view of the JHexViewer.
     */
    @NotNull
    IOffsetRowTemplate createOffsetTemplate(@NotNull JHexViewer hexViewer, int totalCharsCount, int onlyDigitsCount);

    /**
     * Creates the row template which defines the layout of the rows for the hex-view.
     *
     * @param hexViewer   reference to the {@link JHexViewer} component.
     * @param bytesPerRow the number of bytes displayed in the row.
     * @return the layout template for the hex-view of the JHexViewer.
     */
    @NotNull
    IByteRowTemplate createHexTemplate(@NotNull JHexViewer hexViewer, int bytesPerRow);

    /**
     * Creates the row template which defines the layout of the rows for the ascii-view.
     *
     * @param hexViewer   reference to the {@link JHexViewer} component.
     * @param bytesPerRow the number of bytes displayed in the row.
     * @return the layout template for the ascii-view of the JHexViewer.
     */
    @NotNull
    IByteRowTemplate createAsciiTemplate(@NotNull JHexViewer hexViewer, int bytesPerRow);
}
