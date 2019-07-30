package cms.rendner.hexviewer.core.model.row.template.configuration;

import cms.rendner.hexviewer.core.model.row.template.configuration.values.RowInsetValues;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;

/**
 * Provides a default configuration.
 *
 * @author rendner
 */
public class DefaultRowTemplateConfiguration extends AbstractRowTemplateConfiguration
{
    /**
     * Creates a new instance.
     */
    public DefaultRowTemplateConfiguration()
    {
        super();
        initializeWithDefaultValues();
    }

    /**
     * Initialize the properties with some default values.
     */
    private void initializeWithDefaultValues()
    {
        setCaretWidth(new EMValue(0.05d));
        setBytesPerGroup(2);
        setSpaceBetweenGroups(new EMValue(1.5d));
        final RowInsetValues rowInsets = new RowInsetValues(
                new EMValue(1.0d),
                new EMValue(1.0d));
        setRowInset(AreaId.OFFSET, rowInsets);
        setRowInset(AreaId.HEX, rowInsets);
        setRowInset(AreaId.ASCII, rowInsets);
    }
}
