package cms.rendner.hexviewer.core.model.row.template.configuration;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.IValue;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsetValues;
import cms.rendner.hexviewer.utils.observer.IObservable;

/**
 * A configuration defines the layout of a row template.
 * Each row of the JHexViewer is created from a row template, which describes the
 * layout (spaces, number of bytes, etc.) of a row.
 * The layout can be changed during runtime.
 * <p>
 * A configuration instance provides method chaining:
 * <pre>
 *     hexViewer.getRowTemplateConfiguration()
 *     .setBytesPerGroup(2)
 *     .setBytesPerRow(16)
 *     .commit();
 * </pre>
 * At the end of a chain the <code>commit</code> method should be called to inform the JHexViewer about the changed
 * configuration. This will result in a recreating of the internal used row templates.
 *
 * @author rendner
 */
public interface IRowTemplateConfiguration extends IObservable<Object>
{
    /**
     * Returns the number of bytes which should be displayed in a row.
     *
     * @return number of bytes &gt;= 0.
     */
    int getBytesPerRow();

    /**
     * Sets the number of bytes which should be displayed in a row.
     *
     * @param value number of bytes &gt;= 0.
     * @return the configuration instance, to allow method chaining.
     */
    IRowTemplateConfiguration setBytesPerRow(int value);

    /**
     * @return number of bytes per group in the <code>{@link AreaId#HEX}</code>, &gt;= 1.
     */
    int getBytesPerGroup();

    /**
     * Sets the number of bytes which represents a group of bytes in the <code>{@link AreaId#HEX}</code> area.
     *
     * @param value number of bytes per group &gt;= 1.
     * @return the configuration instance, to allow method chaining.
     */
    IRowTemplateConfiguration setBytesPerGroup(int value);

    /**
     * @return the space between group of bytes.
     */
    IValue getSpaceBetweenGroups();

    /**
     * Sets the space between group of bytes.
     * Only the bytes of the <code>{@link AreaId#HEX}</code> area are grouped.
     * <p/>
     * In the most cases this value should depend on the used font-size, therefore an <code>{@link EMValue}</code>
     * should be used to specify the space.
     *
     * @param value the value.
     * @return the configuration instance, to allow method chaining.
     */
    IRowTemplateConfiguration setSpaceBetweenGroups(IValue value);

    /**
     * Returns the width of the caret which can be placed in front of a byte in the rows of the
     * <code>{@link AreaId#HEX}</code> and <code>{@link AreaId#ASCII}</code> area.
     *
     * @return the width of the caret.
     */
    IValue getCaretWidth();

    /**
     * Sets the width of the caret.
     * A caret can be placed in front of a byte in the rows of the
     * <code>{@link AreaId#HEX}</code> and <code>{@link AreaId#ASCII}</code> area.
     * <p/>
     * In the most cases the width depends on the used font-size, therefore an <code>{@link EMValue}</code>
     * should be used to specify the width.
     *
     * @param value the value which defines the width of the caret.
     * @return the configuration instance, to allow method chaining.
     */
    IRowTemplateConfiguration setCaretWidth(IValue value);

    /**
     * Sets the row insets of an area which defines the left and right space of a row.
     * <p/>
     * In the most cases this value should depend on the used font-size, therefore an <code>{@link EMValue}</code>
     * should be used to specify the insets.
     *
     * @param areaId the id of the area.
     * @param value  the row insets to use for the specified area.
     * @return the configuration instance, to allow method chaining.
     */
    IRowTemplateConfiguration setRowInset(AreaId areaId, RowInsetValues value);

    /**
     * Returns the row insets of an area by id.
     *
     * @param areaId the id of the requested area.
     * @return the row insets of the area.
     */
    RowInsetValues getRowInsets(AreaId areaId);

    /**
     * Commits all changes to the JHexViewer.
     */
    void commit();
}
