package cms.rendner.hexviewer.view.ui.actions.others;

import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
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
        getHexViewer(event).ifPresent(hexViewer ->
        {
            hexViewer.getCaret().ifPresent(caret -> {
                caret.moveCaret(0, false, false);
                caret.moveCaretRelatively(Integer.MAX_VALUE, true, false);
            });
        });
    }
}
