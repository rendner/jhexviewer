package cms.rendner.hexviewer.core.view.areas;

import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.view.areas.properties.ProtectedPropertiesProvider;

/**
 * Used to display the offsets of the rows displayed in the {@link AreaId#HEX} and {@link AreaId#ASCII}.
 * <p/>
 * This view uses an IOffsetRowTemplate to layout the row bytes.
 *
 * @author rendner
 * @see IOffsetRowTemplate
 */
public final class OffsetRowsView extends RowBasedView<IOffsetRowTemplate>
{

    /**
     * Creates a new instance with the specified value.
     *
     * @param propertiesProvider used by the {@link cms.rendner.hexviewer.core.JHexViewer} to forward properties which
     *                           should not be accessible outside of this component.
     */
    public OffsetRowsView(final ProtectedPropertiesProvider propertiesProvider)
    {
        super(AreaId.OFFSET, propertiesProvider);
    }
}
