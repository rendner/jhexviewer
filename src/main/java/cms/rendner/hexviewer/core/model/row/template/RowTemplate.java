package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.ElementHitInfo;
import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * Describes the layout of a row.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed in the JHexViewer.
 * For each of the areas ({@link cms.rendner.hexviewer.core.view.areas.AreaId#OFFSET},
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} and
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII}) of the JHexViewer a separate template exists
 * which describes the exact layout of the rows rendered by these areas.
 *
 * @author rendner
 */
public abstract class RowTemplate implements IRowTemplate
{
    /**
     * The elements of the row.
     * The number of minimum entries of this property is <code>1</code>.
     */
    @NotNull
    protected final List<IElement> elements;
    /**
     * The dimension of the row.
     */
    @NotNull
    private final IRowTemplate.IDimension dimension;

    /**
     * The font used to render the text of the rows.
     */
    @NotNull
    private final Font font;
    /**
     * The ascent to center an element vertically if painted into a {@link Graphics} object.
     */
    private int ascent;

    /**
     * Creates a new instance.
     *
     * @param font      the font used to render the text of the rows.
     * @param dimension the dimension of the row.
     * @param elements  the elements of the row, not empty - the list has to contain at least one element.
     */
    public RowTemplate(@NotNull final Font font, @NotNull final IRowTemplate.IDimension dimension, @NotNull final List<IElement> elements)
    {
        super();

        CheckUtils.checkMinValue(elements.size(), 1);

        this.font = font;
        this.dimension = dimension;
        this.elements = Collections.unmodifiableList(elements);
    }

    @NotNull
    @Override
    public ElementHitInfo hitTest(final int xPosition)
    {
        return hitTest(xPosition, new ElementHitInfo());
    }

    @NotNull
    @Override
    public ElementHitInfo hitTest(final int xPosition, @NotNull final ElementHitInfo returnValue)
    {
        final int elementIndex = elementIndexForXPosition(xPosition);
        final IElement element = elements.get(elementIndex);

        final int halfWidth = element.width() / 2;
        final boolean isLeadingEdge = xPosition < (element.right() - halfWidth);
        final boolean wasInside = element.containsX(xPosition);

        returnValue.fillWith(elementIndex, isLeadingEdge, wasInside);
        return returnValue;
    }

    @Override
    public int width()
    {
        return dimension.width();
    }

    @Override
    public int leftInset()
    {
        return elements.get(0).x();
    }

    @Override
    public int rightInset()
    {
        return width() - elements.get(elements.size() - 1).right();
    }

    @Override
    public int height()
    {
        return dimension.height();
    }

    @Override
    public int elementCount()
    {
        return elements.size();
    }

    @NotNull
    @Override
    public IElement element(final int index)
    {
        return elements.get(index);
    }

    @Override
    public int ascent()
    {
        return ascent;
    }

    /**
     * Sets the ascent to center an element vertically if painted into a {@link Graphics} object.
     *
     * @param value the ascent.
     */
    public void setAscent(final int value)
    {
        ascent = value;
    }

    @NotNull
    @Override
    public Font font()
    {
        return font;
    }

    @Override
    public boolean contains(@NotNull final Point position)
    {
        return containsX(position.x) && containsY(position.y);
    }

    @Override
    public boolean containsX(final int xPosition)
    {
        return xPosition >= 0 && xPosition < width();
    }

    @Override
    public boolean containsY(final int yPosition)
    {
        return yPosition >= 0 && yPosition < height();
    }

    @NotNull
    @Override
    public Rectangle elementBounds(final int firstElementIndex, final int lastElementIndex, @NotNull final Rectangle returnValue)
    {
        final IElement firstElement = elements.get(firstElementIndex);
        final IElement lastElement = elements.get(lastElementIndex);

        returnValue.x = firstElement.x();
        returnValue.y = firstElement.y();
        returnValue.width = lastElement.right() - returnValue.x;
        returnValue.height = lastElement.height();

        return returnValue;
    }

    /**
     * Checks which element is under the position.
     *
     * @param xPosition the x position which should be checked.
     * @return the index of the element which intersects with the position.
     */
    protected int elementIndexForXPosition(final int xPosition)
    {
        final int lastElementIndex = elements.size() - 1;

        for (int i = 0; i < lastElementIndex; i++)
        {
            final IElement nextElement = elements.get(i + 1);

            final boolean positionIsBeforeNextElement = xPosition < nextElement.x();

            if (positionIsBeforeNextElement)
            {
                return i;
            }
        }

        return lastElementIndex;
    }
}
