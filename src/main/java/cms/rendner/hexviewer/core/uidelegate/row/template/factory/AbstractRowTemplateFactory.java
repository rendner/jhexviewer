package cms.rendner.hexviewer.core.uidelegate.row.template.factory;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.FixedValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.model.row.template.elements.ElementDimension;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.utils.CheckUtils;

import java.awt.*;
import java.util.List;

/**
 * This abstract class provides methods that can be helpful in creating row templates.
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
     * Computes the width of a char.
     * <p/>
     * The value is calculated by using the font metrics provided by the proper initialized <code>context</code>.
     * Note: This method requires that the context property was initialized.
     *
     * @return the width of a char.
     */
    protected int computeCharWidth()
    {
        return context.getFontMetrics().stringWidth("X");
    }

    /**
     * Computes the height of a char.
     * <p/>
     * The value is calculated by using the font metrics provided by the proper initialized <code>context</code>.
     * Note: This method requires that the context property was initialized.
     *
     * @return the height of a char.
     */
    protected int computeCharHeight()
    {
        final FontMetrics fm = context.getFontMetrics();
        return fm.getAscent() + fm.getDescent();
    }

    /**
     * Computes the height of a row.
     * <p/>
     * The value is calculated by using the font metrics provided by the proper initialized <code>context</code>.
     * Note: This method requires that the context property was initialized.
     *
     * @param rowInsets the row insets to use for the calculation
     * @return the height of the row.
     */
    protected int computeRowHeight(final RowInsets rowInsets)
    {
        CheckUtils.checkNotNull(rowInsets);
        final FontMetrics fm = context.getFontMetrics();
        final int heightWithoutInsets = fm.getAscent() + fm.getDescent() + fm.getLeading();
        return heightWithoutInsets + computeValue(rowInsets.top()) + computeValue(rowInsets.bottom());
    }

    /**
     * Computes the width of a row.
     * <p/>
     * The value is calculated by using the font metrics provided by the proper initialized <code>context</code>.
     * Note: This method requires that the context property was initialized.
     *
     * @param elementsInRow list of elements displayed in the row.
     * @param rowInsets the insets for the row.
     * @return the width of the row.
     */
    protected int computeRowWidth(final List<IElement> elementsInRow, final RowInsets rowInsets)
    {
        final IElement lastElement = elementsInRow.get(elementsInRow.size() - 1);
        return lastElement.right() + computeValue(rowInsets.right());
    }

    /**
     * Creates a dimension instance with the width and height of an element.
     * <p/>
     * The value is calculated by using the font metrics provided by the proper initialized <code>context</code>.
     * Note: This method requires that the context property was initialized.
     *
     * @param charsPerElement number of chars/digits displayed inside the bounds of the element.
     * @return the calculated dimension.
     */
    protected ElementDimension computeElementDimension(final int charsPerElement)
    {
        return new ElementDimension(computeCharWidth() * charsPerElement, computeCharHeight());
    }

    /**
     * Calculates the final value of a <code>IValue</code>.
     * Some of the IValue implementations return a double instead of an int, such values will be converted into an int
     * value by using Math.round.
     * <p/>
     * A EMValue is calculated by using the font metrics provided by the proper initialized <code>context</code>.
     * Note: This method requires that the context property was initialized.
     *
     * @param value the value to calculate the the real value.
     * @return the result, if <code>IValue</code> is <code>null</code> the result will be <code>0</code>.
     */
    protected int computeValue(final IValue value)
    {
        double result = 0.0d;

        if (value instanceof EMValue)
        {
            result = ((EMValue) value).resolve(computeCharHeight());
        }
        else if (value instanceof FixedValue)
        {
            result = ((FixedValue) value).value();
        }

        return (int) Math.round(result);
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
