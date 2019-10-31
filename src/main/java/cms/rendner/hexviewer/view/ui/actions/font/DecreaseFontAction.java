package cms.rendner.hexviewer.view.ui.actions.font;

import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * @author rendner
 */
public class DecreaseFontAction extends AbstractHexViewerAction
{
    private final int step;
    private final int minimum;

    public DecreaseFontAction(final int step, final int minimum)
    {
        super();
        this.step = step;
        this.minimum = minimum;
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(hexViewer -> {
                    hexViewer.getRowContentFont().ifPresent(font -> {
                        hexViewer.setRowContentFont(font.deriveFont((float) Math.max(font.getSize() - step, minimum)));
                    });
                }
        );
    }
}
