package cms.rendner.hexviewer.core.model.row.template.configuration;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsets;
import cms.rendner.hexviewer.core.view.areas.AreaId;

/**
 * A row template configuration defines the layout (e.g. spaces between bytes, number of bytes) of a row.
 * Each row of the JHexViewer is created from a row template.
 * The layout of the rows can be changed during runtime by applying a new configuration to the JHexViewer.
 * <p/>
 * All row template configuration instances are immutable. To create a modified or new version use the builder
 * provided by the concrete instance.
 *
 * @author rendner
 * @see DefaultRowTemplateConfiguration
 */
public interface IRowTemplateConfiguration<B extends IRowTemplateConfiguration.IBuilder>
{
    /**
     * Returns the number of bytes which should be displayed in a row.
     *
     * @return number of bytes &gt;= 0.
     */
    int bytesPerRow();

    /**
     * @return number of bytes per group in the <code>{@link AreaId#HEX}</code>, &gt;= 1.
     */
    int bytesPerGroup();

    /**
     * Returns the width of the caret which can be placed in front of a byte in the rows of the
     * <code>{@link AreaId#HEX}</code> and <code>{@link AreaId#ASCII}</code> area.
     *
     * @return the width of the caret.
     */
    IValue caretWidth();

    /**
     * @return the space between group of bytes.
     */
    IValue spaceBetweenGroups();

    /**
     * Returns the row insets of an area by id.
     *
     * @param areaId the id of the requested area, can't be <code>null</code>.
     * @return the row insets of the area.
     */
    RowInsets rowInsets(AreaId areaId);

    /**
     * @return a new builder which is initialized with the values of the row template configuration instance
     */
    B toBuilder();

    /**
     * A builder can be used to set the desired values before creating a immutable row template configuration instance.
     *
     * @param <B> the concrete class of the builder.
     * @param <C> the concrete class of the row template configuration produced by the concrete builder class.
     */
    interface IBuilder<B extends IBuilder, C extends IRowTemplateConfiguration>
    {
        /**
         * @return a new row template configuration instance with the configured values.
         */
        C build();

        /**
         * Sets the bytes per row.
         *
         * @param value number of bytes &gt;= 0.
         * @return the builder instance, to allow method chaining.
         * @see IRowTemplateConfiguration#bytesPerRow()
         */
        IBuilder<B, C> bytesPerRow(int value);

        /**
         * Sets the bytes per group.
         *
         * @param value number of bytes per group in the <code>{@link AreaId#HEX}</code>, &gt;= 1.
         * @return the builder instance, to allow method chaining.
         * @see IRowTemplateConfiguration#bytesPerGroup()
         */
        IBuilder<B, C> bytesPerGroup(int value);

        /**
         * Sets the space used to visually separate group of bytes.
         * <p/>
         * In the most cases the space should depend on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the space between group of bytes, can't be <code>null</code>.
         * @return the builder instance, to allow method chaining.
         * @see IRowTemplateConfiguration#spaceBetweenGroups()
         */
        IBuilder<B, C> spaceBetweenGroups(IValue value);

        /**
         * Sets the width for the caret. The caret is displayed in the
         * <code>{@link AreaId#HEX}</code> and <code>{@link AreaId#ASCII}</code> area.
         * <p/>
         * In the most cases the width should depend on the used font-size, therefore an <code>{@link EMValue}</code>
         * should be used to specify the value.
         *
         * @param value the width for the caret, can't be <code>null</code>.
         * @return the builder instance, to allow method chaining.
         */
        IBuilder<B, C> caretWidth(IValue value);

        /**
         * Sets the same row insets for all areas.
         *
         * @param value the row insets for all areas, can't be <code>null</code>.
         * @return the builder instance, to allow method chaining.
         */
        IBuilder<B, C> rowInsets(RowInsets value);

        /**
         * Sets row insets for an area.
         *
         * @param areaId the id of the area, can't be <code>null</code>.
         * @param value  the insets for the area, can't be <code>null</code>.
         * @return the builder instance, to allow method chaining.
         */
        IBuilder<B, C> rowInsets(AreaId areaId, RowInsets value);
    }
}
