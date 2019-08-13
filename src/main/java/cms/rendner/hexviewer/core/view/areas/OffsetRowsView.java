package cms.rendner.hexviewer.core.view.areas;

import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import org.jetbrains.annotations.NotNull;

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
     * @param internalApiAccessToken the token to guard the access to the internal api.
     *                               Passing this token to the {@link OffsetRowsView#getInternalApi(Object)} allows to
     *                               call the method without an exception.
     */
    public OffsetRowsView(@NotNull final Object internalApiAccessToken)
    {
        super(AreaId.OFFSET, internalApiAccessToken);
        internalApi = new InternalApi(this);
    }

    /**
     * Allows access to the internal api.
     * <p/>
     * Note:
     * If the passed token doesn't match with the expected token an IllegalArgumentException will be thrown.
     * This method should only be called by the {@link cms.rendner.hexviewer.core.JHexViewer}.
     *
     * @param internalApiAccessToken the token to access the internal api.
     * @return the internal api.
     */
    @NotNull
    public OffsetRowsView.InternalApi getInternalApi(@NotNull final Object internalApiAccessToken)
    {
        return (InternalApi) getGuardedInternalApi(internalApiAccessToken);
    }

    /**
     * Allows to set the hidden properties.
     *
     * @see RowBasedView.InternalApi
     */
    public static class InternalApi extends RowBasedView.InternalApi<OffsetRowsView, IOffsetRowTemplate>
    {
        /**
         * Creates a new instance.
         *
         * @param rowView the view to access.
         */
        InternalApi(@NotNull final OffsetRowsView rowView)
        {
            super(rowView);
        }
    }
}
