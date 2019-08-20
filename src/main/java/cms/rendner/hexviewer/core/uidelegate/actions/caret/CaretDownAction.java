package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.geom.IndexPosition;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class CaretDownAction extends AbstractHexViewerAction
{
    private final boolean select;

    public CaretDownAction(final boolean select)
    {
        super();
        this.select = select;
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(hexViewer ->
            hexViewer.getCaret().ifPresent(caret ->
            {
                final IndexPosition dotPosition = caret.getDot();
                final int newDotIndex = dotPosition.getIndex() + hexViewer.bytesPerRow();

                if (select)
                {
                    caret.moveDot(newDotIndex, dotPosition.getBias());
                }
                else
                {
                    caret.setDot(newDotIndex);
                }
            })
        );
    }
}
