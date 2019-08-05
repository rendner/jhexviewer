package cms.rendner.hexviewer.core.uidelegate.rows;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.RowBasedView;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * A paint delegate allows to overwrite and customize the painting of the three areas of the {@link JHexViewer} and is
 * usually installed by the UI.
 * <p/>
 * An implementation of this interface has to paint the whole RowBasedView of an area which normally includes
 * the following things:
 * <pre>
 * <ul>
 *     <li>background</li>
 *     <li>row templates to align the data</li>
 *     <li>data to display (e.g. bytes or offset addresses)</li>
 *     <li>highlighted ranges</li>
 *     <li>selection</li>
 *     <li>the caret</li>
 * </ul>
 * </pre>
 *
 * @author rendner
 * @see AreaId
 * @see RowBasedView
 */
public interface IPaintDelegate
{
    /**
     * This method is automatically invoked when this paint delegate instance is being installed on the specified hexViewer.
     *
     * @param hexViewer the component where this paint delegate is being installed. Can't be <code>null</code>.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * This method is automatically invoked when this paint delegate instance is being removed for the specified component.
     * This method should undo the configuration performed in install, being careful to leave the JHexViewer instance
     * in a clean state (no extraneous listeners, etc.).
     *
     * @param hexViewer the component where this paint delegate is being removed; this argument is often ignored,
     *                  but might be used if the paint delegate is stateless and shared by multiple components.
     *                  Can't be <code>null</code>.
     */
    void uninstall(@NotNull JHexViewer hexViewer);

    /**
     * Paints the specified view which represents an area in the hexViewer.
     * This method is automatically invoked whenever the rowsView has to be painted.
     *
     * @param g        the Graphics context in which to paint.
     * @param rowsView the component being painted.
     */
    void paint(@NotNull Graphics g, @NotNull RowBasedView<? extends IRowTemplate> rowsView);

    /**
     * Sets a color provider to be used to color the rows of a view.
     * The provider is used during the repaint of a row to determine which color should be used to render the foreground
     * or background. The provider can be set to <code>null</code> to force the delegate to use default colors.
     *
     * @param id               the id of the area for which the provider should be used.
     * @param newColorProvider the new provider.
     */
    void setRowColorProvider(@NotNull AreaId id, @Nullable IRowColorProvider newColorProvider);
}
