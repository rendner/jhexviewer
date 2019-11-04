package cms.rendner.hexviewer.model.rowtemplate.configuration;

import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import org.jetbrains.annotations.NotNull;

/**
 * This row template configuration allows to alter the row template of the {@link cms.rendner.hexviewer.view.components.areas.offset.OffsetArea}.
 * <p/>
 * Each row of the area is painted using a description of a row, called a row template. The layout of the rows can be
 * changed during runtime by applying a new configuration to the {@link cms.rendner.hexviewer.view.JHexViewer}.
 * <p/>
 * Row template configuration instances are immutable. To create a modified or new version use the builder provided by
 * this class or instance:
 * <pre>
 *     // create a new builder
 *     OffsetRowTemplateConfiguration.newBuilder();
 *
 *     // create a builder pre-initialized with the state of the instance
 *     configurationInstance.toBuilder();
 * </pre>
 *
 * @author rendner
 */
public final class OffsetRowTemplateConfiguration
{
    /**
     * The insets which defines the trailing and leading space.
     */
    @NotNull
    private final HInsets insets;

    /**
     * The number of minimum leading zeros when displaying an offset address.
     */
    private final int minPadSize;

    /**
     * Returns a new builder for this class.
     */
    @NotNull
    public static Builder newBuilder()
    {
        return new Builder();
    }

    /**
     * Returns a new builder for this class initialized with the values of this instance.
     *
     * @return the new builder instance.
     */
    @NotNull
    public Builder toBuilder()
    {
        return new Builder()
                .insets(insets)
                .minPadSize(minPadSize);
    }

    /**
     * Returns the row insets of the area.
     *
     * @return the row insets of the area.
     */
    @NotNull
    public HInsets insets()
    {
        return insets;
    }

    /**
     * @return the minimum number of chars to include in the element dimension for displaying a padded offset value,
     * &gt;= 0 and &lt;= 32.
     */
    public int minPadSize()
    {
        return minPadSize;
    }

    private OffsetRowTemplateConfiguration(@NotNull final Builder builder)
    {
        super();
        this.insets = builder.insets;
        this.minPadSize = builder.minPadSize;
    }

    /**
     * A builder can be used to set the desired values before creating an immutable row template configuration instance.
     */
    public static class Builder
    {
        private HInsets insets;

        /**
         * The minimum number of chars included in the element dimension for displaying a padded offset value.
         */
        private int minPadSize = 4;

        private Builder()
        {
        }

        /**
         * Sets the insets which defines the trailing and leading space of the rows.
         *
         * @param value the new row insets.
         * @return the builder instance.
         */
        public Builder insets(@NotNull final HInsets value)
        {
            this.insets = value;
            return this;
        }

        /**
         * Sets the minimum number of chars to included in the element dimension for displaying a padded offset value
         *
         * @param value the minimum pad size to include in the element bounds, &gt;= 0 and &lt;= 32.
         * @return the builder instance.
         */
        public Builder minPadSize(final int value)
        {
            CheckUtils.checkMinValue(value, 0);
            CheckUtils.checkMaxValue(value, 32);
            this.minPadSize = value;
            return this;
        }

        /**
         * @return a new row template configuration instance with the configured values.
         */
        @NotNull
        public OffsetRowTemplateConfiguration build()
        {
            if (insets == null)
            {
                insets = new HInsets(15);
            }
            return new OffsetRowTemplateConfiguration(this);
        }
    }
}
