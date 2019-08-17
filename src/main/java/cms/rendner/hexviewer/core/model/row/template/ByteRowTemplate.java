package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.geom.Dimension;
import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.core.model.row.template.element.HitInfo;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
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
     * @param builder the builder used to initialize the new instance.
     */
    private ByteRowTemplate(@NotNull final Builder builder)
    {
        super(builder.dimension, builder.font, builder.ascent);

        this.caretWidth = builder.caretWidth;
        this.elements = builder.elements;
    }

    /**
     * Returns a new builder for this class.
     *
     * @return the created builder.
     */
    @NotNull
    public static BuilderStepDimension newBuilder()
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
     * <p/>
     * All properties are required (mandatory) to build a valid row template. Therefore the builder uses a step based
     * approach to guarantee that "build" method can only be called if all properties are set.
     */
    public static class Builder implements
            BuilderStepDimension,
            BuilderStepFont,
            BuilderStepAscent,
            BuilderStepCaretWidth,
            BuilderStepElements,
            BuilderStepBuild
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
        private Font font;

        /**
         * The ascent to center an element vertically if painted into a {@link Graphics} object.
         */
        private int ascent;

        /**
         * Hide the constructor.
         */
        private Builder()
        {}

        @Override
        public ByteRowTemplate build()
        {
            return new ByteRowTemplate(this);
        }

        @Override
        public BuilderStepFont dimension(@NotNull final Dimension dimension)
        {
            this.dimension = dimension;
            return this;
        }

        @Override
        public BuilderStepFont dimension(final int width, final int height)
        {
            return dimension(new Dimension(width, height));
        }

        @Override
        public BuilderStepAscent font(@NotNull final Font font)
        {
            this.font = font;
            return this;
        }

        @Override
        public BuilderStepCaretWidth ascent(final int ascent)
        {
            this.ascent = ascent;
            return this;
        }

        @Override
        public BuilderStepElements caretWidth(final int caretWidth)
        {
            CheckUtils.checkMinValue(caretWidth, 1);
            this.caretWidth = caretWidth;
            return this;
        }

        @Override
        public BuilderStepBuild elements(@NotNull final List<Element> elements)
        {
            CheckUtils.checkMinValue(elements.size(), 1);
            this.elements = elements;
            return this;
        }
    }

    public interface BuilderStepDimension
    {
        /**
         * Sets the dimension of the row.
         *
         * @param dimension the dimension.
         * @return the next builder step.
         */
        BuilderStepFont dimension(@NotNull Dimension dimension);

        /**
         * Sets the dimension of the row. Shorthand for <code>dimension(new Dimension(width, height))</code>
         *
         * @param width  the width of the row, &gt;= 1.
         * @param height the height of the row, &gt;= 1.
         * @return the next builder step.
         */
        BuilderStepFont dimension(int width, int height);
    }

    public interface BuilderStepFont
    {
        /**
         * Sets the font to use to render the text of the rows.
         *
         * @param font the font to use.
         * @return the next builder step.
         */
        BuilderStepAscent font(@NotNull Font font);
    }

    public interface BuilderStepAscent
    {
        /**
         * Sets the ascent used to center an element vertically if painted into a {@link Graphics} object.
         *
         * @param ascent the ascent.
         * @return the next builder step.
         */
        BuilderStepCaretWidth ascent(int ascent);
    }

    public interface BuilderStepCaretWidth
    {
        /**
         * Sets the width of the caret which can be placed between the bytes of the row.
         *
         * @param caretWidth the width for the caret, &gt;= 1.
         * @return the next builder step.
         */
        BuilderStepElements caretWidth(int caretWidth);
    }

    public interface BuilderStepElements
    {
        /**
         * Sets the horizontal aligned elements for the row.
         *
         * @param elements the elements of the row, not empty - the list has to contain at least one element.
         * @return the next builder step.
         */
        BuilderStepBuild elements(@NotNull List<Element> elements);
    }

    public interface BuilderStepBuild
    {
        /**
         * Builds the configured row template instance.
         *
         * @return the created template instance.
         */
        ByteRowTemplate build();
    }
}
