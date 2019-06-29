package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
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
    public void actionPerformed(final ActionEvent event)
    {
        final JHexViewer hexViewer = getHexViewer(event);
        if (hexViewer != null)
        {
            final ICaret caret = hexViewer.getCaret();

            if (caret != null)
            {
                final int newDot = caret.getDot() - 1;

                if (select)
                {
                    caret.moveDot(newDot, IndexPosition.Bias.Forward);
                }
                else
                {
                    caret.setDot(newDot, IndexPosition.Bias.Forward);
                }
            }
        }
    }
}
