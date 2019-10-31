package cms.rendner.hexviewer.view.ui.actions.others;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * @author rendner
 */
public class SwitchAreaFocusAction extends AbstractHexViewerAction
{
    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(JHexViewer::toggleCaretFocusedArea);
    }
}
