package cms.rendner.hexviewer.core.uidelegate.actions.font;

import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 *
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
        getHexViewer(event).ifPresent(hexViewer ->
                hexViewer.getRowTemplateConfiguration()
                        .ifPresent(rowConfiguration ->
                                hexViewer.setRowTemplateConfiguration(
                                        rowConfiguration.toBuilder()
                                                .increaseFontSize(step, maximum)
                                                .build()
                                )
                        )
        );
    }
}
