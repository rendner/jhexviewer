package cms.rendner.hexviewer.core.uidelegate.actions.font;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;

import java.awt.event.ActionEvent;

/**
 * // todo: add comment
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
    public void actionPerformed(final ActionEvent event)
    {
        final JHexViewer hexViewer = getHexViewer(event);
        if (hexViewer != null)
        {
            hexViewer.getRowTemplateConfiguration().ifPresent(rowConfiguration -> hexViewer.setRowTemplateConfiguration(
                    rowConfiguration.toBuilder()
                            .decreaseFontSize(step, minimum)
                            .build()
                    )
            );
        }
    }
}
