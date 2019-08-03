package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Allows easier access to some properties of the JHexViewer which are required to create row templates.
 *
 * @author rendner
 */
public final class Context
{
    /**
     * Used to build a row template.
     */
    @NotNull
    private final IRowTemplateConfiguration configuration;
    /**
     * Used to determine the width and height for the elements placed in a row template.
     */
    @NotNull
    private final FontMetrics fontMetrics;
    /**
     * The hex viewer component for which the row template to be build.
     */
    @NotNull
    private final JHexViewer hexViewer;

    /**
     * Creates a new context instance.
     *
     * @param hexViewer the component to use to create the context.
     */
    public Context(@NotNull final JHexViewer hexViewer)
    {
        super();

        this.fontMetrics = hexViewer.getFontMetrics(hexViewer.getFont());
        this.configuration = hexViewer.getRowTemplateConfiguration();
        this.hexViewer = hexViewer;
    }

    /**
     * @return the configuration from which the <code>IRowTemplates</code> are build.
     */
    @NotNull
    public IRowTemplateConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * @return the font metrics of the font object used in the JHexViewer to render the bytes and offset values.
     */
    @NotNull
    public FontMetrics getFontMetrics()
    {
        return fontMetrics;
    }

    /**
     * @return reference to the {@link JHexViewer} component.
     */
    @NotNull
    public JHexViewer getHexViewer()
    {
        return hexViewer;
    }
}
