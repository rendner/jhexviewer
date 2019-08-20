package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 *
 * @author rendner
 */
public class CaretLeftAction extends AbstractHexViewerAction
{
    private final boolean select;

    public CaretLeftAction(final boolean select)
    {
        super();
        this.select = select;
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).flatMap(JHexViewer::getCaret).ifPresent(caret -> {
            final int newDotIndex = caret.getDot().getIndex() - 1;

            if (select)
            {
                caret.moveDot(newDotIndex);
            }
            else
            {
                caret.setDot(newDotIndex);
            }
        });
    }
}
