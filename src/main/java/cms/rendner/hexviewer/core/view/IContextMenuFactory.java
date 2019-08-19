package cms.rendner.hexviewer.core.view;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Factory for creating context sensitive popup menus.
 *
 * @author rendner
 * @see JHexViewer#setContextMenuFactory(IContextMenuFactory)
 */
public interface IContextMenuFactory
{
    /**
     * Called from the JHexViewer to generate a popup menu after the user right-clicked somewhere in the
     * JHexViewer component.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     * @param areaId    the id of the area the user clicked.
     * @param byteIndex the offset of the nearest byte at the right-click.
     * @return a popup menu or <code>null</code> if no popup menu should be shown.
     */
    @Nullable
    JPopupMenu create(@NotNull JHexViewer hexViewer, @NotNull AreaId areaId, int byteIndex);
}
