package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class CaretUpAction extends AbstractHexViewerAction
{
    private final boolean select;

    public CaretUpAction(final boolean select)
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
            hexViewer.getCaret().ifPresent(caret ->
            {
                final int newDot = caret.getDot() - hexViewer.bytesPerRow();

                if (select)
                {
                    caret.moveDot(newDot, caret.getDotBias());
                }
                else
                {
                    caret.setDot(newDot, IndexPosition.Bias.Forward);
                }
            });
        }
    }
}
