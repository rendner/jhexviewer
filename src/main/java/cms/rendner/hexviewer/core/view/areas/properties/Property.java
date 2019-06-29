package cms.rendner.hexviewer.core.view.areas.properties;

import cms.rendner.hexviewer.core.view.areas.AreaId;

/**
 * Describes a property which should be updated for a RowBasedView.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.core.view.areas.RowBasedView
 */
public final class Property
{
    /**
     * Constant used to determine when the <code>rowCount</code> property should be updated.
     */
    public static final String ROW_COUNT = "rowCount";

    /**
     * Constant used to determine when the <code>rowTemplate</code> property should be updated.
     */
    public static final String ROW_TEMPLATE = "rowTemplate";

    /**
     * Constant used to determine when the <code>paintDelegate</code> property should be updated.
     */
    public static final String PAINT_DELEGATE = "paintDelegate";

    /**
     * Constant used to determine when the <code>focus</code> property should be updated.
     */
    public static final String FOCUS = "focus";

    /**
     * The name of the property to update.
     */
    private final String name;

    /**
     * The area to which this property belongs.
     */
    private final AreaId target;

    /**
     * The new value.
     */
    private final Object value;

    /**
     * Creates a new instance with the specified arguments.
     * The target will be set to <code>null</code>, which means the property for all areas should be updated.
     *
     * @param name  the name of the property to update.
     * @param value the new value for the property.
     */
    public Property(final String name, final Object value)
    {
        this(null, name, value);
    }

    /**
     * Creates a new instance with the specified arguments.
     *
     * @param target the id of the area to update.
     * @param name   the name of the property to update.
     * @param value  the new value for the property.
     */
    public Property(final AreaId target, final String name, final Object value)
    {
        super();

        this.name = name;
        this.target = target;
        this.value = value;
    }

    /**
     * @return the new property value.
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Checks if an area should update the mentioned property.
     *
     * @param id the id to check.
     * @return <code>true</code> if the area should update the mentioned property.
     */
    public boolean isTarget(final AreaId id)
    {
        return target == null || target.equals(id);
    }

    /**
     * @return the name of the property to update.
     */
    public String getName()
    {
        return name;
    }
}
