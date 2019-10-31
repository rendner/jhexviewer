package cms.rendner.hexviewer.view.ui.actions.font;

import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * @author rendner
 */
public class IncreaseFontAction extends AbstractHexViewerAction
{
    private final int step;
    private final int maximum;

    public IncreaseFontAction(final int step, final int maximum)
    {
        super();
        this.step = step;
        this.maximum = maximum;
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(hexViewer -> {
                    hexViewer.getRowContentFont().ifPresent(font -> {
                        hexViewer.setRowContentFont(font.deriveFont((float) Math.min(font.getSize() + step, maximum)));
                    });
                }
        );
    }
}
