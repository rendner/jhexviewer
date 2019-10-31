package cms.rendner.hexviewer.model.rowtemplate.configuration;

import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.EMValue;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.IValue;
import org.jetbrains.annotations.NotNull;

/**
 * A row template configuration defines the layout of a row.
 * Each row of the {@link cms.rendner.hexviewer.view.components.areas.bytes.HexArea} is painted using a description of a
 * row, called a row-template. The layout of the rows can be changed during runtime by applying a new configuration to
 * the {@link cms.rendner.hexviewer.view.JHexViewer}.
 * <p/>
 * All row template configuration instances are immutable. To create a modified or new version use the builder
 * provided by the concrete instance.
 *
 * @author rendner
 */
public final class HexRowTemplateConfiguration
{
    /**
     * The insets which defines the trailing and leading space.
     */
    @NotNull
    private final HInsets insets;

    /**
     * Defines the width of the caret displayed by the area.
     */
    @NotNull
    private final IValue caretWidth;

    /**
     * Number of bytes grouped together.
     */
    private final int bytesPerGroup;

    /**
     * The space between group of bytes.
     */
    @NotNull
    private final IValue spaceBetweenGroups;

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
     * @return the insets which defines the trailing and leading space.
     */
    @NotNull
    public HInsets insets()
    {
        return insets;
    }

    /**
     * @return number of bytes per group, &gt;= 1.
     */
    public int bytesPerGroup()
    {
        return bytesPerGroup;
    }

    /**
     * Returns the width of the caret.
     *
     * @return the width of the caret displayed by the area.
     */
    @NotNull
    public IValue caretWidth()
    {
        return caretWidth;
    }

    /**
     * Returns the space between groups of bytes.
     *
     * @return the space between groups of bytes.
     */
    public IValue spaceBetweenGroups()
    {
        return spaceBetweenGroups;
    }

    private HexRowTemplateConfiguration(@NotNull final Builder builder)
    {
        super();
        this.insets = builder.insets;
        this.caretWidth = builder.caretWidth;
        this.bytesPerGroup = builder.bytesPerGroup;
        this.spaceBetweenGroups = builder.spaceBetweenGroups;
    }

    /**
     * A builder can be used to set the desired values before creating an immutable row template configuration instance.
     */
    public final static class Builder
    {
        private HInsets insets;
        private IValue caretWidth;
        private IValue spaceBetweenGroups;

        private int bytesPerGroup = 1;

        private Builder()
        {
        }

        /**
         * Sets the insets which defines the trailing and leading space of the rows.
         *
         * @param value the new row insets.
         * @return the builder instance.
         */
        @NotNull
        public Builder insets(@NotNull final HInsets value)
        {
            this.insets = value;
            return this;
        }

        /**
         * Sets the width of the caret displayed by the area.
         *
         * @param value the new caret width.
         * @return the builder instance.
         */
        @NotNull
        public Builder caretWidth(@NotNull final IValue value)
        {
            caretWidth = value;
            return this;
        }

        /**
         * Sets the number of bytes per group.
         * Groups of bytes can be separated by extra space ({@link #spaceBetweenGroups}).
         *
         * @param value number of bytes per group, &gt;= 0.
         * @return the builder instance.
         */
        @NotNull
        public Builder bytesPerGroup(final int value)
        {
            CheckUtils.checkMinValue(value, 0);
            this.bytesPerGroup = value;
            return this;
        }

        /**
         * Sets the space between groups of bytes.
         *
         * @param value space between groups of bytes.
         * @return the builder instance.
         */
        @NotNull
        public Builder spaceBetweenGroups(@NotNull final IValue value)
        {
            spaceBetweenGroups = value;
            return this;
        }

        /**
         * @return a new row template configuration instance with the configured values.
         */
        @NotNull
        public HexRowTemplateConfiguration build()
        {
            if (insets == null)
            {
                insets = new HInsets(15);
            }
            if (spaceBetweenGroups == null)
            {
                spaceBetweenGroups = new EMValue(1.0d);
            }
            if (caretWidth == null)
            {
                caretWidth = new EMValue(0.08d);
            }
            return new HexRowTemplateConfiguration(this);
        }
    }
}
