package cms.rendner.hexviewer.core.model.row.template.configuration;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.*;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.utils.observer.Observable;
import cms.rendner.hexviewer.utils.CheckUtils;

/**
 * Abstract implementation which defines some default methods to allow subclasses only specify the desired values.
 *
 * @author rendner
 */
public abstract class AbstractRowTemplateConfiguration extends Observable<Object> implements IRowTemplateConfiguration
{
    /**
     * The text insets of the offset area.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private RowInsetValues offsetRowInsets = new RowInsetValues();

    /**
     * The text insets of the hex area.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private RowInsetValues hexRowInsets = new RowInsetValues();

    /**
     * The text insets of the ascii area.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private RowInsetValues asciiRowInsets = new RowInsetValues();

    /**
     * Number of bytes in a group of bytes.
     */
    private int bytesPerGroup;

    /**
     * The space between two byte groups.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private IValue spaceBetweenGroups = new FixedValue(0);

    /**
     * The width for the caret.
     * In the most cases this value should depend on the used font-size, therefore a <code>{@link EMValue}</code>
     * should be used to specify the width.
     */
    private IValue caretWidth = new FixedValue(1);

    /**
     * The number of bytes displayed in a single row.
     */
    private int bytesPerRow = 16;

    @Override
    public IValue getCaretWidth()
    {
        return caretWidth;
    }

    @Override
    public IRowTemplateConfiguration setCaretWidth(final IValue value)
    {
        CheckUtils.checkNotNull(value);

        if (!value.equals(caretWidth))
        {
            caretWidth = value;
            setChanged();
        }

        return this;
    }

    @Override
    public int getBytesPerGroup()
    {
        return bytesPerGroup;
    }

    @Override
    public IRowTemplateConfiguration setBytesPerGroup(final int value)
    {
        CheckUtils.checkMinValue(value, 1);

        if (bytesPerGroup != value)
        {
            bytesPerGroup = value;
            setChanged();
        }

        return this;
    }

    @Override
    public IValue getSpaceBetweenGroups()
    {
        return spaceBetweenGroups;
    }

    @Override
    public IRowTemplateConfiguration setSpaceBetweenGroups(final IValue value)
    {
        CheckUtils.checkNotNull(value);

        if (!value.equals(spaceBetweenGroups))
        {
            spaceBetweenGroups = value;
            setChanged();
        }

        return this;
    }

    @Override
    public int getBytesPerRow()
    {
        return bytesPerRow;
    }

    @Override
    public IRowTemplateConfiguration setBytesPerRow(final int value)
    {
        CheckUtils.checkMinValue(value, 1);

        if (bytesPerRow != value)
        {
            bytesPerRow = value;
            setChanged();
        }

        return this;
    }

    @Override
    public IRowTemplateConfiguration setRowInset(final AreaId areaId, final RowInsetValues value)
    {
        CheckUtils.checkNotNull(areaId);
        CheckUtils.checkNotNull(value);

        final RowInsetValues rowInsetValues = getRowInsets(areaId);

        if (!value.equals(rowInsetValues))
        {
            setRowInsetsInternal(areaId, value);
            setChanged();
        }

        return this;
    }

    @Override
    public RowInsetValues getRowInsets(final AreaId areaId)
    {
        CheckUtils.checkNotNull(areaId);
        RowInsetValues result = null;

        switch (areaId)
        {
            case OFFSET:
            {
                result = offsetRowInsets;
                break;
            }

            case HEX:
            {
                result = hexRowInsets;
                break;
            }

            case ASCII:
            {
                result = asciiRowInsets;
                break;
            }
        }

        return result;
    }

    @Override
    public void commit()
    {
        notifyObservers();
    }

    /**
     * Sets the row inset for an area without dispatching a changed event.
     *
     * @param areaId the ide of the area, can't be <code>null</code>.
     * @param value  the new row inset value, can't be <code>null</code>.
     */
    protected void setRowInsetsInternal(final AreaId areaId, final RowInsetValues value)
    {
        CheckUtils.checkNotNull(areaId);
        CheckUtils.checkNotNull(value);

        switch (areaId)
        {
            case OFFSET:
            {
                offsetRowInsets = value;
                break;
            }

            case HEX:
            {
                hexRowInsets = value;
                break;
            }

            case ASCII:
            {
                asciiRowInsets = value;
                break;
            }
        }
    }
}
