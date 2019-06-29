package cms.rendner.hexviewer.core.view.areas.properties;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.IPaintDelegate;
import cms.rendner.hexviewer.utils.observer.Observable;

/**
 * Forwards new values for hidden properties of the areas.
 * <p/>
 * This concept was implemented to hide the setter methods for important properties from the areas.
 * Otherwise the user could change some of these properties directly on the internal areas which would result in an
 * unexpected behaviour. For example, the property "rowCount" has to have the same value for all areas. This can't be
 * guaranteed if the user can modify this property directly.
 *
 * @author rendner
 */
public final class ProtectedPropertiesProvider extends Observable<Property>
{
    /**
     * Forwards the focus state to a specific area.
     *
     * @param target   the area to notify.
     * @param newValue the new focus state.
     */
    public void forwardFocus(final AreaId target, final boolean newValue)
    {
        notifyArea(target, Property.FOCUS, newValue);
    }

    /**
     * Forwards a row template to a specific area.
     *
     * @param target   the area to notify.
     * @param newValue the new row template.
     */
    public void forwardRowTemplate(final AreaId target, final IRowTemplate newValue)
    {
        notifyArea(target, Property.ROW_TEMPLATE, newValue);
    }

    /**
     * Forwards the row count to all areas.
     *
     * @param newValue the new row count.
     */
    public void forwardRowCount(final int newValue)
    {
        notifyAreas(Property.ROW_COUNT, newValue);
    }

    /**
     * Forwards a paint delegate to all areas.
     *
     * @param newValue the new delegate.
     */
    public void forwardPaintDelegate(final IPaintDelegate newValue)
    {
        notifyAreas(Property.PAINT_DELEGATE, newValue);
    }

    /**
     * Notifies all areas that a property should be updated.
     *
     * @param name  the name of the property to update.
     * @param value the new value for the property.
     */
    private void notifyAreas(final String name, final Object value)
    {
        setChangedAndNotifyObservers(
                new Property(name, value)
        );
    }

    /**
     * Notifies an area that a property should be updated.
     *
     * @param target the area to notify.
     * @param name   the name of the property to update.
     * @param value  the new value for the property.
     */
    private void notifyArea(final AreaId target, final String name, final Object value)
    {
        setChangedAndNotifyObservers(
                new Property(target, name, value)
        );
    }
}
