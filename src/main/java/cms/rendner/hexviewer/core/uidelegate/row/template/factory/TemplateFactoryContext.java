package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Provides access to the JHexViewer, the row template configuration and the font metrics object.
 * The font metrics object can be used to determine the height and width for the row template to create.
 * <p/>
 * The context has to be recreated whenever a new row template has to be created to ensure that no outdated values
 * are used.
 *
 * @author rendner
 */
public final class TemplateFactoryContext
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
     * @param hexViewer     the component to use to create the context.
     * @param configuration the configuration from which the row templates are build.
     * @param fontMetrics   the font metrics of the font object to calculate the row templates.
     */
    public TemplateFactoryContext(@NotNull final JHexViewer hexViewer, @NotNull IRowTemplateConfiguration configuration, @NotNull FontMetrics fontMetrics)
    {
        super();

        this.hexViewer = hexViewer;
        this.configuration = configuration;
        this.fontMetrics = fontMetrics;
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
