package cms.rendner.hexviewer.core.uidelegate.actions.others;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.swing.scrollable.ScrollableContainer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class SwitchAreaFocusAction extends AbstractHexViewerAction
{
    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(hexViewer ->
        {
            final AreaId oldFocusedArea = hexViewer.getFocusedArea();
            hexViewer.setFocusedArea(getUnfocusedAreaId(oldFocusedArea));
            final AreaId newFocusedArea = hexViewer.getFocusedArea();

            final ByteRowsView rowsView = hexViewer.getByteRowsView(newFocusedArea);
            rowsView.scrollRectToVisible(computeScrollRect(hexViewer, rowsView));
        });
    }

    private AreaId getUnfocusedAreaId(final AreaId focusedAreaId)
    {
        return AreaId.HEX.equals(focusedAreaId) ? AreaId.ASCII : AreaId.HEX;
    }

    private Rectangle computeScrollRect(final JHexViewer hexViewer, final ByteRowsView rowsView)
    {
        final ScrollableContainer byteViewsContainer = hexViewer.getScrollableByteRowsContainer();
        final Rectangle visibleContainerRect = byteViewsContainer.getVisibleRect();

        final Rectangle result = rowsView.getRowRect(0);
        result.y = visibleContainerRect.y;
        result.width = Math.min(result.width, visibleContainerRect.width);

        return result;
    }
}
