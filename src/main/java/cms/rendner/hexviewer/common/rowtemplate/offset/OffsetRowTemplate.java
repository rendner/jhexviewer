package cms.rendner.hexviewer.common.rowtemplate.offset;

import cms.rendner.hexviewer.common.geom.Dimension;
import cms.rendner.hexviewer.common.rowtemplate.BaseRowTemplate;
import cms.rendner.hexviewer.common.rowtemplate.Element;
import cms.rendner.hexviewer.common.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.Objects;

/**
 * Describes the layout of a row which displays offset addresses.
 *
 * @author rendner
 */
public class OffsetRowTemplate extends BaseRowTemplate implements IOffsetRowTemplate
{
    /**
     * The element of the row.
     */
    @NotNull
    private final Element element;

    /**
     * Number of leading zeros to render left padded offset values.
     */
    private final int leadingZeros;

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param builder the builder used to initialize the new instance.
     */
    private OffsetRowTemplate(@NotNull final Builder builder)
    {
        super(builder.dimension, builder.fontMetrics);
        this.element = builder.element;
        this.leadingZeros = builder.leadingZeros;
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

    /**
     * @return the single element used to align an offset address inside the row.
     */
    @Override
    public @NotNull Element element()
    {
        return element;
    }

    /**
     * @return Number of leading zeros to render left padded offset values.
     */
    @Override
    public int numberOfLeadingZeros()
    {
        return leadingZeros;
    }

    /**
     * Builder which implements {@link UIResource}, to configure and create OffsetRowTemplate instances.
     * <p/>
     * All properties are required (mandatory) to build a valid row template.
     */
    public static class OffsetRowTemplateUIResource extends OffsetRowTemplate implements UIResource
    {
        /**
         * Hide the constructor.
         * Creates a new instance with all the values from a builder.
         *
         * @param builder the builder used to initialize the new instance.
         */
        private OffsetRowTemplateUIResource(@NotNull final Builder builder)
        {
            super(builder);
        }
    }

    /**
     * Builder to configure and create OffsetRowTemplate instances.
     * <p/>
     * All properties are required (mandatory) to build a valid row template.
     */
    public static class Builder
    {
        /**
         * The element of the row.
         */
        Element element;

        /**
         * The dimension of the row.
         */
        Dimension dimension;

        /**
         * The ascent to center an element vertically if painted into a {@link Graphics} object.
         */
        FontMetrics fontMetrics;

        /**
         * Number of leading zeros to render left padded offset values.
         */
        int leadingZeros;

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
        public OffsetRowTemplate build()
        {
            validate();
            return new OffsetRowTemplate(this);
        }

        /**
         * Builds the configured row template instance.
         *
         * @return the created template instance.
         */
        public OffsetRowTemplateUIResource buildUIResource()
        {
            validate();
            return new OffsetRowTemplateUIResource(this);
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
         * Sets the number of leading zeros.
         *
         * @param leadingZeros number of leading zeros to render left padded offset values.
         * @return the builder instance.
         */
        public Builder numberOfLeadingZeros(final int leadingZeros)
        {
            CheckUtils.checkMinValue(leadingZeros, 0);
            CheckUtils.checkMaxValue(leadingZeros, 32);
            this.leadingZeros = leadingZeros;
            return this;
        }

        /**
         * Sets the font metrics.
         *
         * @param fontMetrics the font metrics of the font used to render the offset addresses.
         * @return the builder instance.
         */
        public Builder fontMetrics(@NotNull final FontMetrics fontMetrics)
        {
            this.fontMetrics = fontMetrics;
            return this;
        }

        /**
         * Sets the elements for a row.
         * The element is used to paint the offset address at the expected position inside a row.
         *
         * @param element the element of the row.
         * @return the builder instance.
         */
        public Builder element(@NotNull final Element element)
        {
            this.element = element;
            return this;
        }

        private void validate()
        {
            Objects.requireNonNull(fontMetrics);
            Objects.requireNonNull(element);
            Objects.requireNonNull(dimension);
        }
    }
}
