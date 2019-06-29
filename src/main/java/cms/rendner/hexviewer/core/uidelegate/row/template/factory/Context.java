package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import cms.rendner.hexviewer.core.JHexViewer;

import java.awt.*;

/**
 * Allows easier access to some properties of the JHexViewer which are required to create row templates.
 *
 * @author rendner
 */
public class Context
{
    /**
     * The configuration to be used to build a template.
     */
    private IRowTemplateConfiguration configuration;
    /**
     * The font metrics to build a row template.
     */
    private FontMetrics fontMetrics;
    /**
     * The hex viewer component for which the row template to be build..
     */
    private JHexViewer hexViewer;

    /**
     * @return the configuration from which the <code>IRowTemplates</code> are build.
     */
    public IRowTemplateConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Sets the configuration from which the <code>IRowTemplates</code> are build.
     *
     * @param configuration the configuration to create row templates.
     */
    public void setConfiguration(final IRowTemplateConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * @return the font metrics of the font object used in the JHexViewer to render the bytes and offset values.
     */
    public FontMetrics getFontMetrics()
    {
        return fontMetrics;
    }

    /**
     * Set the font metrics to use to create IRowTemplate objects.
     *
     * @param fontMetrics font metrics of the font object used in the JHexViewer to render the bytes and offset values.
     */
    public void setFontMetrics(final FontMetrics fontMetrics)
    {
        this.fontMetrics = fontMetrics;
    }

    /**
     * @return reference to the {@link JHexViewer} component.
     */
    public JHexViewer getHexViewer()
    {
        return hexViewer;
    }

    /**
     * Sets the JHexViewer component.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    public void setHexViewer(final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
    }
}
