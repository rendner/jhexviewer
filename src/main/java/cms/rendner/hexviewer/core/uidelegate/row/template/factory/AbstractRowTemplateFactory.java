package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.FixedValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
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
     * The minimal final value for an <code>EMValue</code>.
     */
    protected int mimEmValue = 1;

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
     * Calculates the final value of a <code>IValue</code>.
     *
     * @param value the value to calculate the the real value.
     * @return the result, if <code>IValue</code> is <code>null</code> the result will be <code>0</code>.
     */
    protected int resolveValue(final IValue value)
    {
        if (value instanceof EMValue)
        {
            final int emValue = (int) Math.ceil(((EMValue)value).resolve(getCharHeight()));
            return Math.max(mimEmValue, emValue);
        }
        else if(value instanceof FixedValue)
        {
            return (int) ((FixedValue) value).getValue();
        }

        return 0;
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
