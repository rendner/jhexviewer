package cms.rendner.hexviewer.core.model.row.template.configuration;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.FontUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumMap;

/**
 * Default implementation of a row template configuration.
 * <p/>
 * To create a new template configuration, use the builder provided by this class:
 * <pre>
 * <code>
 *     IRowTemplateConfiguration newRowLayout = DefaultRowTemplateConfiguration.newBuilder()
 *     .bytesPerGroup(2)
 *     .bytesPerRow(16)
 *     .build();
 *
 *     hexViewer.setRowTemplateConfiguration(newRowLayout);
 * <code>
 * </pre>
 *
 * @author rendner
 */
public final class DefaultRowTemplateConfiguration implements IRowTemplateConfiguration<DefaultRowTemplateConfiguration.Builder>
{
    /**
     * The name of the font created by this configuration.
     */
    @NotNull
    private static final String DEFAULT_FONT_NAME = FontUtils.getMonospacedFontName();

    /**
     * The insets for the rows mapped by area.
     */
    @NotNull
    private final EnumMap<AreaId, RowInsets> rowInsets = new EnumMap<>(AreaId.class);

    /**
     * The space between two byte groups.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    @NotNull
    private final IValue spaceBetweenGroups;

    /**
     * The width for the caret.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    @NotNull
    private final IValue caretWidth;

    /**
     * The font used to render the text of the rows.
     */
    @NotNull
    private final Font font;

    /**
     * Number of bytes in a group of bytes.
     */
    private final int bytesPerGroup;

    /**
     * The number of bytes displayed in a single row.
     */
    private final int bytesPerRow;

    /**
     * Returns a new builder for this class.
     */
    @NotNull
    public static Builder newBuilder()
    {
        return new Builder();
    }

    /**
     * Returns a new builder for this class with all the values from a row template configuration.
     *
     * @param source the configuration used to initialize the new builder.
     * @return the created builder.
     */
    @NotNull
    public static Builder newBuilder(@NotNull final IRowTemplateConfiguration source)
    {
        return new Builder(source);
    }

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param source the builder used to initialize the new instance.
     */
    private DefaultRowTemplateConfiguration(@NotNull final Builder source)
    {
        super();

        font = new Font(DEFAULT_FONT_NAME, Font.PLAIN, source.fontSize);
        caretWidth = source.caretWidth;
        bytesPerRow = source.bytesPerRow;
        bytesPerGroup = source.bytesPerGroup;
        rowInsets.putAll(source.rowInsets);
        spaceBetweenGroups = source.spaceBetweenGroups;
    }

    @Override
    public int bytesPerRow()
    {
        return bytesPerRow;
    }

    @Override
    public int bytesPerGroup()
    {
        return bytesPerGroup;
    }

    @NotNull
    @Override
    public IValue caretWidth()
    {
        return caretWidth;
    }

    @NotNull
    @Override
    public IValue spaceBetweenGroups()
    {
        return spaceBetweenGroups;
    }

    @NotNull
    @Override
    public RowInsets rowInsets(@NotNull final AreaId areaId)
    {
        return rowInsets.get(areaId);
    }

    @NotNull
    @Override
    public Font font()
    {
        return font;
    }

    @NotNull
    @Override
    public Builder toBuilder()
    {
        return new Builder(this);
    }

    /**
     * Builder to configure and create a new configuration instance.
     */
    public final static class Builder implements IRowTemplateConfiguration.IBuilder<Builder, DefaultRowTemplateConfiguration>
    {
        /**
         * The insets for the rows mapped by area.
         */
        @NotNull
        private final EnumMap<AreaId, RowInsets> rowInsets = new EnumMap<>(AreaId.class);

        /**
         * The space between two byte groups.
         */
        @NotNull
        private IValue spaceBetweenGroups = new EMValue(1.5d);

        /**
         * The width for the caret.
         */
        @NotNull
        private IValue caretWidth = new EMValue(0.05d);

        /**
         * The font size for rendering text in the rows.
         */
        private int fontSize = 14;

        /**
         * Number of bytes in a group of bytes.
         */
        private int bytesPerGroup = 2;

        /**
         * The number of bytes displayed in a single row.
         */
        private int bytesPerRow = 1;

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
         * @param source the configuration to initialize the new instance.
         */
        private Builder(@NotNull final IRowTemplateConfiguration source)
        {
            super();

            fontSize = source.font().getSize();
            caretWidth = source.caretWidth();
            bytesPerRow = source.bytesPerRow();
            bytesPerGroup = source.bytesPerGroup();
            spaceBetweenGroups = source.spaceBetweenGroups();
            rowInsets.put(AreaId.OFFSET, source.rowInsets(AreaId.OFFSET));
            rowInsets.put(AreaId.HEX, source.rowInsets(AreaId.HEX));
            rowInsets.put(AreaId.ASCII, source.rowInsets(AreaId.ASCII));
        }

        @NotNull
        @Override
        public Builder bytesPerRow(final int value)
        {
            CheckUtils.checkMinValue(value, 1);
            bytesPerRow = value;
            return this;
        }

        @NotNull
        @Override
        public Builder bytesPerGroup(final int value)
        {
            CheckUtils.checkMinValue(value, 1);
            bytesPerGroup = value;
            return this;
        }

        @NotNull
        @Override
        public Builder spaceBetweenGroups(@NotNull final IValue value)
        {
            spaceBetweenGroups = value;
            return this;
        }

        @NotNull
        @Override
        public Builder caretWidth(@NotNull final IValue value)
        {
            caretWidth = value;
            return this;
        }

        @NotNull
        @Override
        public Builder rowInsets(@NotNull final RowInsets value)
        {
            rowInsets.put(AreaId.OFFSET, value);
            rowInsets.put(AreaId.HEX, value);
            rowInsets.put(AreaId.ASCII, value);
            return this;
        }

        @NotNull
        @Override
        public Builder rowInsets(@NotNull final AreaId areaId, @NotNull final RowInsets value)
        {
            rowInsets.put(areaId, value);
            return this;
        }

        @NotNull
        @Override
        public Builder fontSize(final int size) throws IllegalArgumentException
        {
            CheckUtils.checkMinValue(size, 1);
            fontSize = size;
            return this;
        }

        @NotNull
        @Override
        public Builder increaseFontSize(final int bySize, final int maxSize) throws IllegalArgumentException
        {
            CheckUtils.checkMinValue(bySize, 1);
            CheckUtils.checkMinValue(maxSize, 1);
            return fontSize(Math.min(maxSize, fontSize + bySize));
        }

        @NotNull
        @Override
        public Builder decreaseFontSize(final int bySize, final int minSize) throws IllegalArgumentException
        {
            CheckUtils.checkMinValue(bySize, 1);
            CheckUtils.checkMinValue(minSize, 1);
            return fontSize(Math.max(minSize, fontSize - bySize));
        }

        @NotNull
        @Override
        public DefaultRowTemplateConfiguration build()
        {
            final RowInsets defaultInsets = RowInsets.newBuilder()
                    .horizontal(new EMValue(1.0d)).build();

            rowInsets.putIfAbsent(AreaId.OFFSET, defaultInsets);
            rowInsets.putIfAbsent(AreaId.HEX, defaultInsets);
            rowInsets.putIfAbsent(AreaId.ASCII, defaultInsets);

            return new DefaultRowTemplateConfiguration(this);
        }
    }
}
