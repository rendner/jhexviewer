package cms.rendner.hexviewer.common.rowtemplate.bytes;

import cms.rendner.hexviewer.common.geom.Dimension;
import cms.rendner.hexviewer.common.rowtemplate.BaseRowTemplate;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.rowtemplate.bytes.hit.RowHitInfo;
import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import org.jetbrains.annotations.NotNull;

import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Describes the layout of a row of bytes.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed in a {@link ByteArea}.
 *
 * @author rendner
 */
public class ByteRowTemplate extends BaseRowTemplate implements IByteRowTemplate
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
     * @param builder the builder used to initialize the new instance.
     */
    private ByteRowTemplate(@NotNull final Builder builder)
    {
        super(builder.dimension, builder.fontMetrics);

        this.caretWidth = builder.caretWidth;
        this.elements = builder.elements;
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
    public RowHitInfo hitTest(final int xPosition)
    {
        final int elementIndex = elementIndexForXPosition(xPosition);
        final Element element = elements.get(elementIndex);

        final int halfWidth = element.width() / 2;
        final boolean isLeadingEdge = xPosition < (element.right() - halfWidth);
        final boolean wasInside = element.containsX(xPosition);

        return new RowHitInfo(elementIndex, isLeadingEdge, wasInside);
    }

    /**
     * Checks which element is under the position.
     *
     * @param xPosition the x position which should be checked.
     * @return the index of the element which intersects with the position.
     */
    private int elementIndexForXPosition(final int xPosition)
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
     * Builder which implements {@link UIResource}, to configure and create ByteRowTemplate instances.
     * <p/>
     * All properties are required (mandatory) to build a valid row template.
     */
    public static class ByteRowTemplateUIResource extends ByteRowTemplate implements UIResource
    {
        /**
         * Hide the constructor.
         * Creates a new instance with all the values from a builder.
         *
         * @param builder the builder used to initialize the new instance.
         */
        private ByteRowTemplateUIResource(@NotNull final Builder builder)
        {
            super(builder);
        }
    }

    /**
     * Builder to configure and create ByteRowTemplate instances.
     * <p/>
     * All properties are required (mandatory) to build a valid row template.
     */
    public static class Builder
    {
        /**
         * The elements of the row.
         * The number of minimum entries of this property is <code>1</code>.
         */
        private List<Element> elements;

        /**
         * The width of the caret which can be placed between the bytes of the row.
         */
        private int caretWidth = 1;

        /**
         * The dimension of the row.
         */
        private Dimension dimension;

        /**
         * The font used to render the text of the rows.
         */
        private FontMetrics fontMetrics;

        /**
         * Hide the constructor.
         */
        private Builder()
        {
        }

        /**
         * Builds the configured row template instance.
         *
         * @return the created template instance.
         */
        public ByteRowTemplate build()
        {
            validate();
            return new ByteRowTemplate(this);
        }

        /**
         * Builds the configured row template instance.
         *
         * @return the created template instance.
         */
        public ByteRowTemplateUIResource buildUIResource()
        {
            validate();
            return new ByteRowTemplateUIResource(this);
        }

        /**
         * Sets the dimension of the row.
         *
         * @param dimension the dimension.
         * @return the builder instance.
         */
        public Builder dimension(@NotNull final Dimension dimension)
        {
            this.dimension = dimension;
            return this;
        }

        /**
         * Sets the dimension of the row. Shorthand for <code>dimension(new Dimension(width, height))</code>
         *
         * @param width  the width of the row, &gt;= 1.
         * @param height the height of the row, &gt;= 1.
         * @return the builder instance.
         */
        public Builder dimension(final int width, final int height)
        {
            return dimension(new Dimension(width, height));
        }

        /**
         * Sets the font metrics.
         *
         * @param fontMetrics the font metrics of the font used to render byte values.
         * @return the builder instance.
         */
        public Builder fontMetrics(@NotNull final FontMetrics fontMetrics)
        {
            this.fontMetrics = fontMetrics;
            return this;
        }

        /**
         * Sets the width of the caret which can be placed between the bytes of a row.
         *
         * @param caretWidth the width for the caret, &gt;= 1.
         * @return the builder instance.
         */
        public Builder caretWidth(final int caretWidth)
        {
            CheckUtils.checkMinValue(caretWidth, 1);
            this.caretWidth = caretWidth;
            return this;
        }

        /**
         * Sets the horizontal aligned elements for a row.
         * These elements are used to paint the byte values at the expected position inside a row.
         *
         * @param elements the elements of the row, not empty - the list has to contain at least one element.
         * @return the builder instance.
         */
        public Builder elements(@NotNull final List<Element> elements)
        {
            CheckUtils.checkMinValue(elements.size(), 1);
            this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
            return this;
        }

        private void validate()
        {
            Objects.requireNonNull(fontMetrics);
            Objects.requireNonNull(elements);
            Objects.requireNonNull(dimension);
        }
    }
}
