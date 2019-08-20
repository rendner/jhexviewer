package cms.rendner.hexviewer.core.uidelegate.actions.others;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * Selects all bytes.
 * <p/>
 * The selection can only happen if a caret is provided by the JHexViewer.
 *
 * @author rendner
 */
public class SelectAllAction extends AbstractHexViewerAction
{
    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event)
                .flatMap(JHexViewer::getCaret)
                .ifPresent(ICaret::selectAll);
    }
}
