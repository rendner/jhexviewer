package cms.rendner.hexviewer.view.components.areas.offset;

import cms.rendner.hexviewer.common.data.formatter.offset.IOffsetFormatter;
import cms.rendner.hexviewer.common.data.formatter.offset.OffsetFormatter;
import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Component which displays the offset addresses of the current visible rows.
 *
 * @author rendner
 */
public final class OffsetArea extends Area<IOffsetRowTemplate, IOffsetColorProvider>
{
    /**
     * Constant used to determine when the <code>valueFormatter</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_VALUE_FORMATTER = "valueFormatter";

    /**
     * Used to transform the data of the data model of the {@link cms.rendner.hexviewer.view.JHexViewer} into displayable strings.
     */
    @NotNull
    private IOffsetFormatter valueFormatter = new OffsetFormatter(true, "h");

    public OffsetArea()
    {
        super(AreaId.OFFSET);
    }

    /**
     * Sets the new value formatter of the offset-area.
     * The formatter will be used to transform the data of the data model of the {@link cms.rendner.hexviewer.view.JHexViewer}
     * into displayable strings.
     * <p/>
     * Setting a new formatter results in a repaint of the component.
     * <p/>
     * A PropertyChange event {@link OffsetArea#PROPERTY_VALUE_FORMATTER} is fired when a new formatter is set.
     *
     * @param valueFormatter the new formatter, passing <code>null</code> results into an empty area
     *                       (no content will be rendered).
     */
    public void setValueFormatter(@NotNull final IOffsetFormatter valueFormatter)
    {
        if (this.valueFormatter != valueFormatter)
        {
            final IOffsetFormatter oldValue = this.valueFormatter;
            this.valueFormatter = valueFormatter;
            firePropertyChange(PROPERTY_VALUE_FORMATTER, oldValue, this.valueFormatter);
            repaint();
        }
    }

    /**
     * @return the formatter to transform the data of the data model of the {@link cms.rendner.hexviewer.view.JHexViewer}
     * into displayable strings.
     */
    @NotNull
    public IOffsetFormatter getValueFormatter()
    {
        return valueFormatter;
    }
}
