package cms.rendner.hexviewer.core.uidelegate.actions.font;

import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 *
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
        getHexViewer(event).ifPresent(hexViewer ->
                hexViewer.getRowTemplateConfiguration()
                        .ifPresent(rowConfiguration -> hexViewer.setRowTemplateConfiguration(
                                rowConfiguration.toBuilder()
                                        .decreaseFontSize(step, minimum)
                                        .build()
                                )
                        )
        );
    }
}
