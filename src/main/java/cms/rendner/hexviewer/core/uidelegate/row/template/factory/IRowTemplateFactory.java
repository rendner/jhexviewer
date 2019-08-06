package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

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
     * @param context         used to create the offset row template, provides access to the JHexViewer, the row template configuration and the font metrics object.
     * @param totalCharsCount the number of chars to display the formatted offset value including suffix and prefix (if required).
     * @param onlyDigitsCount the number of chars to display only the digits of the formatted offset value without any
     *                        additional suffix or prefix.
     * @return the layout template for the offset-view of the JHexViewer.
     */
    @NotNull
    IOffsetRowTemplate createOffsetTemplate(@NotNull TemplateFactoryContext context, int totalCharsCount, int onlyDigitsCount);

    /**
     * Creates the row template which defines the layout of the rows for the hex-view.
     *
     * @param context used to create the hex row template, provides access to the JHexViewer, the row template configuration and the font metrics object.
     * @return the layout template for the hex-view of the JHexViewer.
     */
    @NotNull
    IByteRowTemplate createHexTemplate(@NotNull TemplateFactoryContext context);

    /**
     * Creates the row template which defines the layout of the rows for the ascii-view.
     *
     * @param context used to create the ascii row template, provides access to the JHexViewer, the row template configuration and the font metrics object.
     * @return the layout template for the ascii-view of the JHexViewer.
     */
    @NotNull
    IByteRowTemplate createAsciiTemplate(@NotNull TemplateFactoryContext context);
}
