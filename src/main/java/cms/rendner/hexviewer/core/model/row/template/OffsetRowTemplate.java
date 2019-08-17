package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.geom.Dimension;
import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


/**
 * Describes the layout of a row from the offset-area.
 *
 * @author rendner
 */
public final class OffsetRowTemplate extends RowTemplate implements IOffsetRowTemplate
{
    /**
     * The element of the row.
     */
    @NotNull
    private final Element element;

    /**
     * Number of chars to display the formatted offset value.
     */
    private final int totalCharsCount;

    /**
     * Number of chars to display only the digits of the formatted offset value.
     */
    private final int onlyDigitsCount;

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param builder the builder used to initialize the new instance.
     */
    private OffsetRowTemplate(@NotNull final Builder builder)
    {
        super(builder.dimension, builder.font, builder.ascent);

        this.element = builder.element;
        this.totalCharsCount = builder.totalCharsCount;
        this.onlyDigitsCount = builder.onlyDigitsCount;
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

    @Override
    public @NotNull Element element()
    {
        return element;
    }

    @Override
    public int totalCharsCount()
    {
        return totalCharsCount;
    }

    @Override
    public int onlyDigitsCount()
    {
        return onlyDigitsCount;
    }

    /**
     * Builder to configure and create OffsetRowTemplate instances.
     * <p/>
     * All properties are required (mandatory) to build a valid row template. Therefore the builder uses a step based
     * approach to guarantee that "build" method can only be called if all properties are set.
     */
    public static class Builder implements
            BuilderStepDimension,
            BuilderStepFont,
            BuilderStepAscent,
            BuilderStepCharCount,
            BuilderStepElement,
            BuilderStepBuild
    {
        /**
         * The element of the row.
         */
        Element element;

        /**
         * Number of chars to display the formatted offset value.
         */
        int totalCharsCount;

        /**
         * Number of chars to display only the digits of the formatted offset value.
         */
        int onlyDigitsCount;

        /**
         * The dimension of the row.
         */
        Dimension dimension;

        /**
         * The font used to render the text of the rows.
         */
        Font font;

        /**
         * The ascent to center an element vertically if painted into a {@link Graphics} object.
         */
        int ascent;

        /**
         * Hide the constructor.
         */
        private Builder()
        {}

        @Override
        public OffsetRowTemplate build()
        {
            return new OffsetRowTemplate(this);
        }

        @Override
        public BuilderStepAscent font(@NotNull final Font font)
        {
            this.font = font;
            return this;
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
        public BuilderStepCharCount ascent(final int ascent)
        {
            this.ascent = ascent;
            return this;
        }

        @Override
        public BuilderStepBuild element(@NotNull final Element element)
        {
            this.element = element;
            return this;
        }

        @Override
        public BuilderStepElement charCounts(final int onlyDigitsCount, final int totalCharsCount)
        {
            CheckUtils.checkMinValue(totalCharsCount, 1);
            CheckUtils.checkMinValue(onlyDigitsCount, 1);
            CheckUtils.checkMinValue(totalCharsCount, onlyDigitsCount);

            this.onlyDigitsCount = onlyDigitsCount;
            this.totalCharsCount = totalCharsCount;
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
        BuilderStepCharCount ascent(int ascent);
    }

    public interface BuilderStepCharCount
    {
        /**
         * @param onlyDigitsCount the number of chars to display only the digits of the formatted offset value.
         *                        This value has to be &gt; 1 and &lt;= totalCharsCount.
         * @param totalCharsCount the number of chars to display the formatted offset value.
         *                        This value has to be &gt; 1 and &gt;= onlyDigitsCount.
         * @return the next builder step.
         */
        BuilderStepElement charCounts(int onlyDigitsCount, int totalCharsCount);
    }

    public interface BuilderStepElement
    {
        /**
         * Sets the element which displays the formatted offset value.
         *
         * @param element the element.
         * @return the next builder step.
         */
        BuilderStepBuild element(@NotNull Element element);
    }

    public interface BuilderStepBuild
    {
        /**
         * Builds the configured row template instance.
         *
         * @return the created template instance.
         */
        OffsetRowTemplate build();
    }
}
