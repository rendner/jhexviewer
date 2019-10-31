package cms.rendner.hexviewer.model.rowtemplate.configuration;

import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import org.jetbrains.annotations.NotNull;

/**
 * A row template configuration defines the layout of a row.
 * Each row of the {@link cms.rendner.hexviewer.view.components.areas.offset.OffsetArea} is painted using a description
 * of a row, called a row-template. The layout of the rows can be changed during runtime by applying a new configuration
 * to the {@link cms.rendner.hexviewer.view.JHexViewer}.
 * <p/>
 * All row template configuration instances are immutable. To create a modified or new version use the builder
 * provided by the concrete instance.
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
    private final int minLeadingZeros;

    /**
     * Returns a new builder for this class.
     */
    @NotNull
    public static Builder newBuilder()
    {
        return new Builder();
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
     * Returns the number of minimal leading zeros, used to render left padded offset values.
     *
     * @return the number of minimal leading zeros, &gt;= 0.
     */
    public int minLeadingZeros()
    {
        return minLeadingZeros;
    }

    private OffsetRowTemplateConfiguration(@NotNull final Builder builder)
    {
        super();
        this.insets = builder.insets;
        this.minLeadingZeros = builder.minLeadingZeros;
    }

    /**
     * A builder can be used to set the desired values before creating an immutable row template configuration instance.
     */
    public static class Builder
    {
        private HInsets insets;

        private int minLeadingZeros = 4;

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
         * Sets the number of minimal zeros to render left padded offset values.
         *
         * @param value the new number of minimal zeros.
         * @return the builder instance.
         */
        public Builder minLeadingZeros(final int value)
        {
            CheckUtils.checkMinValue(value, 0);
            CheckUtils.checkMaxValue(value, 32);
            this.minLeadingZeros = value;
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
