package cms.rendner.hexviewer.core.model.row.template.configuration.values;

import cms.rendner.hexviewer.utils.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The insets of a row.
 * An instance of this class can't be created directly, instead the builder should be used.
 * <pre>
 * <code>
 *     RowInsets rowInsets = RowInsets.newBuilder()
 *       .horizontal(new FixedValue(1.5d)
 *       .build();
 * </code>
 * </pre>
 * Instances of RowInsets are immutable, use a builder to create a modified version:
 * <pre>
 * <code>
 *     RowInsets modifiedRowInsets = rowInsets.toBuilder()
 *       .left(new FixedValue(1.5d)
 *       .build();
 * </code>
 * </pre>
 *
 * @author rendner
 */
public final class RowInsets
{
    /**
     * The left inset of a row.
     */
    @NotNull
    private final IValue left;

    /**
     * The right inset of a row.
     */
    @NotNull
    private final IValue right;

    /**
     * The top inset of a row.
     */
    @NotNull
    private final IValue top;

    /**
     * The top inset of a row.
     */
    @NotNull
    private final IValue bottom;

    /**
     * Returns a new builder for this class.
     */
    @NotNull
    public static Builder newBuilder()
    {
        return new Builder();
    }

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param source the builder used to initialize the new instance.
     */
    private RowInsets(@NotNull final Builder source)
    {
        super();

        left = source.left;
        right = source.right;
        top = source.top;
        bottom = source.bottom;
    }

    /**
     * Returns a builder containing all the values of this instance.
     */
    @NotNull
    public Builder toBuilder()
    {
        return new Builder(this);
    }

    /**
     * Returns the value for the right inset of a row.
     *
     * @return the right value.
     */
    @NotNull
    public IValue right()
    {
        return right;
    }

    /**
     * Returns the value for the left inset of a row.
     *
     * @return the left value.
     */
    @NotNull
    public IValue left()
    {
        return left;
    }

    /**
     * Returns the value for the top inset of a row.
     *
     * @return the top value.
     */
    @NotNull
    public IValue top()
    {
        return top;
    }

    /**
     * Returns the value for the bottom inset of a row.
     *
     * @return the bottom value.
     */
    @NotNull
    public IValue bottom()
    {
        return bottom;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof RowInsets))
        {
            return false;
        }
        RowInsets rowInsets = (RowInsets) o;
        return left.equals(rowInsets.left) &&
                right.equals(rowInsets.right) &&
                top.equals(rowInsets.top) &&
                bottom.equals(rowInsets.bottom);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(left, right, top, bottom);
    }

    /**
     * Builder to configure and create a new RowInsets instance.
     */
    public static final class Builder
    {
        /**
         * The left inset of a row.
         */
        private IValue left;

        /**
         * The right inset of a row.
         */
        private IValue right;

        /**
         * The top inset of a row.
         */
        private IValue top;

        /**
         * The top inset of a row.
         */
        private IValue bottom;

        /**
         * Hide the constructor.
         * Creates a new builder.
         */
        private Builder()
        {
            super();
        }

        /**
         * Hide the constructor.
         * Creates a new builder containing all the values of the source.
         *
         * @param source the insets are used to initialize the new instance.
         */
        private Builder(@NotNull final RowInsets source)
        {
            super();

            this.left = source.left;
            this.right = source.right;
            this.top = source.top;
            this.bottom = source.bottom;
        }

        /**
         * Sets the top right.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the inset.
         *
         * @param value the value for the right inset.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder right(@NotNull final IValue value)
        {
            right = value;
            return this;
        }

        /**
         * Sets the left inset.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the inset.
         *
         * @param value the value for the left inset.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder left(@NotNull final IValue value)
        {
            left = value;
            return this;
        }

        /**
         * Sets the top inset.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the value for the top inset.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder top(@NotNull final IValue value)
        {
            top = value;
            return this;
        }

        /**
         * Sets the bottom inset.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the value for the bottom inset.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder bottom(@NotNull final IValue value)
        {
            bottom = value;
            return this;
        }

        /**
         * Sets the left and right inset.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the value for the left and right inset.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder horizontal(@NotNull final IValue value)
        {
            return left(value).right(value);
        }

        /**
         * Sets the top and bottom inset.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the value for the top and bottom inset.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder vertical(@NotNull final IValue value)
        {
            return top(value).bottom(value);
        }

        /**
         * Sets all insets (top, left, right and bottom) to the value.
         * <p/>
         * In the most cases the inset depends on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the value to set.
         * @return the builder instance, to allow method chaining.
         */
        @NotNull
        public Builder all(@NotNull final IValue value)
        {
            return horizontal(value).vertical(value);
        }

        /**
         * Creates a new RowInsets instance with the specified values.
         * All unspecified insets are initialized with fixed value of <code>0</code>.
         *
         * @return the created instance.
         */
        @NotNull
        public RowInsets build()
        {
            final IValue defaultValue = new FixedValue();

            left = ObjectUtils.ifNotNullOtherwise(left, defaultValue);
            right = ObjectUtils.ifNotNullOtherwise(right, defaultValue);
            top = ObjectUtils.ifNotNullOtherwise(top, defaultValue);
            bottom = ObjectUtils.ifNotNullOtherwise(bottom, defaultValue);

            return new RowInsets(this);
        }
    }
}
