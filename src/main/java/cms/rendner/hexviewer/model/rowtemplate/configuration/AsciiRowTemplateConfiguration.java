package cms.rendner.hexviewer.model.rowtemplate.configuration;

import cms.rendner.hexviewer.model.rowtemplate.configuration.values.EMValue;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.HInsets;
import cms.rendner.hexviewer.model.rowtemplate.configuration.values.IValue;
import org.jetbrains.annotations.NotNull;

/**
 * This row template configuration allows to alter the row template of the {@link cms.rendner.hexviewer.view.components.areas.bytes.AsciiArea}.
 * <p/>
 * Each row of the area is painted using a description of a row, called a row template. The layout of the rows can be
 * changed during runtime by applying a new configuration to the {@link cms.rendner.hexviewer.view.JHexViewer}.
 * <p/>
 * Row template configuration instances are immutable. To create a modified or new version use the builder provided by
 * this class or instance:
 * <pre>
 *     // create a new builder
 *     AsciiRowTemplateConfiguration.newBuilder();
 *
 *     // create a builder pre-initialized with the state of the instance
 *     configurationInstance.toBuilder();
 * </pre>
 *
 * @author rendner
 */
public final class AsciiRowTemplateConfiguration
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
                .caretWidth(caretWidth)
                .insets(insets);
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
     * Returns the width of the caret.
     *
     * @return the width of the caret displayed by the area.
     */
    @NotNull
    public IValue caretWidth()
    {
        return caretWidth;
    }

    private AsciiRowTemplateConfiguration(@NotNull final Builder builder)
    {
        super();
        this.insets = builder.insets;
        this.caretWidth = builder.caretWidth;
    }

    /**
     * A builder can be used to set the desired values before creating an immutable row template configuration instance.
     */
    public final static class Builder
    {
        private HInsets insets;
        private IValue caretWidth;

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
         * @return a new row template configuration instance with the configured values.
         */
        @NotNull
        public AsciiRowTemplateConfiguration build()
        {
            if (insets == null)
            {
                insets = new HInsets(15);
            }
            if (caretWidth == null)
            {
                caretWidth = new EMValue(0.08d);
            }
            return new AsciiRowTemplateConfiguration(this);
        }
    }
}
