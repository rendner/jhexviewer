package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.core.model.row.template.element.HitInfo;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describes the layout of a row of bytes.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed in the JHexViewer.
 * For each of the two byte areas ({@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} and
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII}) of the JHexViewer a separate template
 * exists which describes the exact layout of the rows rendered by these two areas.
 *
 * @author rendner
 */
public final class ByteRowTemplate extends RowTemplate implements IByteRowTemplate
{
    /**
     * The elements of the row.
     * The number of minimum entries of this property is <code>1</code>.
     */
    @NotNull
    private final List<Element> elements;

    /**
     * The width of the caret which can be placed between the bytes of the row.
     */
    private final int caretWidth;

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param source the builder used to initialize the new instance.
     */
    private ByteRowTemplate(@NotNull final Builder source)
    {
        super(source);
        this.caretWidth = source.caretWidth;
        this.elements = source.elements;
    }

    /**
     * Returns a new builder for this class.
     *
     * @return the created builder.
     */
    @NotNull
    public static Builder newBuilder()
    {
        return new Builder();
    }

    @NotNull
    @Override
    public Rectangle caretBounds(final int byteIndex)
    {
        final Element byteAfterCaret = elements.get(byteIndex);
        return new Rectangle(
                byteAfterCaret.x() - caretWidth,
                byteAfterCaret.y(),
                caretWidth,
                byteAfterCaret.height()
        );
    }

    @NotNull
    @Override
    public Rectangle elementBounds(final int firstElementIndex, final int lastElementIndex)
    {
        final Element firstElement = elements.get(firstElementIndex);
        final Element lastElement = elements.get(lastElementIndex);
        return new Rectangle(
                firstElement.x(),
                firstElement.y(),
                lastElement.right() - firstElement.x(),
                lastElement.height());
    }

    @Override
    public int elementCount()
    {
        return elements.size();
    }

    @NotNull
    @Override
    public Element element(final int index)
    {
        return elements.get(index);
    }

    @NotNull
    @Override
    public HitInfo hitTest(final int xPosition)
    {
        final int elementIndex = elementIndexForXPosition(xPosition);
        final Element element = elements.get(elementIndex);

        final int halfWidth = element.width() / 2;
        final boolean isLeadingEdge = xPosition < (element.right() - halfWidth);
        final boolean wasInside = element.containsX(xPosition);

        return new HitInfo(elementIndex, isLeadingEdge, wasInside);
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
            final Element nextElement = elements.get(i + 1);

            final boolean positionIsBeforeNextElement = xPosition < nextElement.x();

            if (positionIsBeforeNextElement)
            {
                return i;
            }
        }

        return lastElementIndex;
    }

    /**
     * Builder to configure and create ByteRowTemplate instances.
     */
    public static class Builder extends RowTemplate.Builder<Builder>
    {
        /**
         * The elements of the row.
         * The number of minimum entries of this property is <code>1</code>.
         */
        protected List<Element> elements;

        /**
         * The width of the caret which can be placed between the bytes of the row.
         */
        private int caretWidth = 1;

        /**
         * Hide the constructor.
         * Creates a new builder.
         */
        private Builder()
        {
            super();
        }

        @Override
        protected Builder getThis()
        {
            return this;
        }

        /**
         * Sets the width of the caret which can be placed between the bytes of the row.
         *
         * @param caretWidth the width for the caret, &gt;= 1.
         * @return the builder instance, to allow method chaining.
         */
        public Builder setCaretWidth(final int caretWidth)
        {
            CheckUtils.checkMinValue(caretWidth, 1);
            this.caretWidth = caretWidth;
            return this;
        }

        /**
         * Sets the horizontal aligned elements for the row.
         *
         * @param elements the elements of the row, not empty - the list has to contain at least one element.
         * @return the builder instance, to allow method chaining.
         */
        public Builder setElements(@NotNull final List<Element> elements)
        {
            CheckUtils.checkMinValue(elements.size(), 1);
            this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
            return getThis();
        }

        /**
         * @return a new instance with the configured values.
         */
        public ByteRowTemplate build()
        {
            CheckUtils.checkNotNull(elements);
            CheckUtils.checkMinValue(elements.size(), 1);
            return new ByteRowTemplate(this);
        }
    }
}
