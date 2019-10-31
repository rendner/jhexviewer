package cms.rendner.hexviewer.view.ui.actions.caret;

import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
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
        {
            hexViewer.getCaret().ifPresent(caret -> {
                caret.moveCaretRelatively(hexViewer.getBytesPerRow(), select, true);
            });
        });
    }
}
