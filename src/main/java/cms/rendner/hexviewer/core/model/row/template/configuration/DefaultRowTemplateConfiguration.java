package cms.rendner.hexviewer.core.model.row.template.configuration;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.ObjectUtils;

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
     * The insets for the rows mapped by area.
     */
    private final EnumMap<AreaId, RowInsets> rowInsets = new EnumMap<>(AreaId.class);

    /**
     * The space between two byte groups.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private final IValue spaceBetweenGroups;

    /**
     * The width for the caret.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private final IValue caretWidth;

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
    public static Builder newBuilder()
    {
        return new Builder();
    }

    /**
     * Returns a new builder for this class with all the values from a row template configuration.
     * @param source the configuration used to initialize the new builder.
     * @return the created builder.
     */
    public static Builder newBuilder(final IRowTemplateConfiguration source)
    {
        return new Builder(source);
    }

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     * @param source the builder used to initialize the new instance.
     */
    private DefaultRowTemplateConfiguration(final Builder source)
    {
        super();

        caretWidth = source.caretWidth;
        bytesPerRow = source.bytesPerRow;
        bytesPerGroup = source.bytesPerGroup;
        spaceBetweenGroups = source.spaceBetweenGroups;
        rowInsets.putAll(source.rowInsets);
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

    @Override
    public IValue caretWidth()
    {
        return caretWidth;
    }

    @Override
    public IValue spaceBetweenGroups()
    {
        return spaceBetweenGroups;
    }

    @Override
    public RowInsets rowInsets(final AreaId areaId)
    {
        CheckUtils.checkNotNull(areaId);
        return rowInsets.get(areaId);
    }

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
        private EnumMap<AreaId, RowInsets> rowInsets = new EnumMap<>(AreaId.class);

        /**
         * The space between two byte groups.
         */
        private IValue spaceBetweenGroups;

        /**
         * The width for the caret.
         */
        private IValue caretWidth;

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
        private Builder(final IRowTemplateConfiguration source)
        {
            super();

            caretWidth = source.caretWidth();
            bytesPerRow = source.bytesPerRow();
            bytesPerGroup = source.bytesPerGroup();
            spaceBetweenGroups = source.spaceBetweenGroups();
            rowInsets.put(AreaId.OFFSET, source.rowInsets(AreaId.OFFSET));
            rowInsets.put(AreaId.HEX, source.rowInsets(AreaId.HEX));
            rowInsets.put(AreaId.ASCII, source.rowInsets(AreaId.ASCII));
        }

        @Override
        public Builder bytesPerRow(final int value)
        {
            CheckUtils.checkMinValue(value, 1);
            bytesPerRow = value;
            return this;
        }

        @Override
        public Builder bytesPerGroup(final int value)
        {
            CheckUtils.checkMinValue(value, 1);
            bytesPerGroup = value;
            return this;
        }

        @Override
        public Builder spaceBetweenGroups(final IValue value)
        {
            CheckUtils.checkNotNull(value);
            spaceBetweenGroups = value;
            return this;
        }

        @Override
        public Builder caretWidth(final IValue value)
        {
            CheckUtils.checkNotNull(value);
            caretWidth = value;
            return this;
        }

        @Override
        public Builder rowInsets(final RowInsets value)
        {
            CheckUtils.checkNotNull(value);
            rowInsets.put(AreaId.OFFSET, value);
            rowInsets.put(AreaId.HEX, value);
            rowInsets.put(AreaId.ASCII, value);
            return this;
        }

        @Override
        public Builder rowInsets(final AreaId areaId, final RowInsets value)
        {
            CheckUtils.checkNotNull(areaId);
            CheckUtils.checkNotNull(value);
            rowInsets.put(areaId, value);
            return this;
        }

        @Override
        public DefaultRowTemplateConfiguration build()
        {
            final RowInsets defaultInsets = RowInsets.newBuilder()
                    .horizontal(new EMValue(1.0d)).build();

            rowInsets.putIfAbsent(AreaId.OFFSET, defaultInsets);
            rowInsets.putIfAbsent(AreaId.HEX, defaultInsets);
            rowInsets.putIfAbsent(AreaId.ASCII, defaultInsets);

            caretWidth = ObjectUtils.ifNotNullOtherwise(caretWidth, new EMValue(0.05d));
            spaceBetweenGroups = ObjectUtils.ifNotNullOtherwise(spaceBetweenGroups, new EMValue(1.5d));

            return new DefaultRowTemplateConfiguration(this);
        }
    }
}
