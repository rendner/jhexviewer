package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.element.Element;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;


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
     * @param source the builder used to initialize the new instance.
     */
    private OffsetRowTemplate(@NotNull final Builder source)
    {
        super(source);
        this.element = source.element;
        this.totalCharsCount = source.totalCharsCount;
        this.onlyDigitsCount = source.onlyDigitsCount;
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
     */
    public static class Builder extends RowTemplate.Builder<Builder>
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
         * Sets the number of chars to display the formatted offset value including suffix and prefix (if required).
         *
         * @param totalCharsCount the number of chars displayed by the one element of the row template.
         * @return the builder instance, to allow method chaining.
         */
        public Builder setTotalCharsCount(final int totalCharsCount)
        {
            this.totalCharsCount = totalCharsCount;
            return this;
        }

        /**
         * Sets the number of chars to display only the digits of the formatted offset value without any
         * additional suffix or prefix.
         *
         * @param onlyDigitsCount the number of chars to display only the digits of the formatted offset value.
         * @return the builder instance, to allow method chaining.
         */
        public Builder setOnlyDigitsCount(final int onlyDigitsCount)
        {
            this.onlyDigitsCount = onlyDigitsCount;
            return this;
        }

        /**
         * Sets the element which displays the formatted offset value.
         *
         * @param element the element.
         * @return the builder instance, to allow method chaining.
         */
        public Builder setElement(@NotNull final Element element)
        {
            this.element = element;
            return getThis();
        }

        /**
         * @return a new instance with the configured values.
         */
        public OffsetRowTemplate build()
        {
            CheckUtils.checkNotNull(element);
            CheckUtils.checkMinValue(totalCharsCount, 1);
            CheckUtils.checkMinValue(onlyDigitsCount, 1);
            CheckUtils.checkMinValue(totalCharsCount, onlyDigitsCount);

            return new OffsetRowTemplate(this);
        }
    }
}
