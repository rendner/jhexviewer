package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValueContainer;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementDimension;
import cms.rendner.hexviewer.core.JHexViewer;

import java.awt.*;

/**
 * This abstract class provides default implementations for creating row templates.
 *
 * @author rendner
 */
public abstract class AbstractRowTemplateFactory implements IRowTemplateFactory
{
    /**
     * Context.
     */
    protected Context context;

    /**
     * Returns the width of a char for the font metrics of the <code>context</code>.
     * This method requires that the context property was initialized.
     *
     * @return the width of a char.
     */
    protected int getCharWidth()
    {
        return context.getFontMetrics().stringWidth("X");
    }

    /**
     * Returns the height of a char for the font metrics of the <code>context</code>.
     * This method requires that the context property was initialized.
     *
     * @return the height of a char.
     */
    protected int getCharHeight()
    {
        final FontMetrics fm = context.getFontMetrics();
        return fm.getAscent() + fm.getDescent();
    }

    /**
     * Creates a dimension instance with the width and height of an element.
     * This method requires that the context property was initialized.
     *
     * @param charsPerElement number of chars/digits displayed inside the bounds of the element.
     * @return the calculated dimension.
     */
    protected ElementDimension createElementDimension(final int charsPerElement)
    {
        return new ElementDimension(getCharWidth() * charsPerElement, getCharHeight());
    }

    /**
     * Converts a value container into a value.
     * This method requires that the context property was initialized.
     *
     * @param valueContainer the container which should be converted.
     * @return the result, if <code>valueContainer</code> is <code>null</code> the result will be <code>0</code>.
     */
    protected int convert(final IValueContainer valueContainer)
    {
        if (valueContainer == null)
        {
            return 0;
        }

        if (valueContainer instanceof EMValue)
        {
            final int calculatedValue = (int) Math.ceil(getCharHeight() * valueContainer.getValue());
            return (int) ((EMValue) valueContainer).getAcceptedValue(calculatedValue);
        }

        return (int) valueContainer.getValue();
    }

    /**
     * Initializes the temporary context which is used during the creation of row templates.
     * The context has to be recreated whenever a new row template has to be created to ensure that no outdated values
     * are used.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @return the actual context.
     */
    protected Context createFreshContext(final JHexViewer hexViewer)
    {
        final Font font = hexViewer.getFont();
        final FontMetrics fontMetrics = hexViewer.getFontMetrics(font);

        final Context result = new Context();
        result.setHexViewer(hexViewer);
        result.setConfiguration(hexViewer.getRowTemplateConfiguration());
        result.setFontMetrics(fontMetrics);
        return result;
    }
}
