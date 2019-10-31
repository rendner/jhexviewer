package cms.rendner.hexviewer.view.ui.actions.caret;

import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.view.ui.container.common.ScrollDirection;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author rendner
 */
public class VerticalPageAction extends AbstractHexViewerAction
{
    private final int direction;
    private final boolean select;

    public VerticalPageAction(@MagicConstant(flags = {
            ScrollDirection.UP,
            ScrollDirection.DOWN}) final int direction, final boolean select)
    {
        super();
        this.direction = direction;
        this.select = select;
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(hexViewer ->
        {
            final Rectangle visibleRect = hexViewer.getVisibleRect();
            final int visibleRows = visibleRect.height / hexViewer.rowHeight();
            final int dirMultiplier = ScrollDirection.UP == direction ? -1 : 1;

            hexViewer.getCaret().ifPresent(caret -> {
                caret.moveCaretRelatively(dirMultiplier * (visibleRows * hexViewer.getBytesPerRow()), select, true);
            });
        });
    }
}
