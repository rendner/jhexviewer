package cms.rendner.hexviewer.core.uidelegate.actions.font;

import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.JHexViewer;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class IncreaseFontAction extends AbstractHexViewerAction
{
    private final float step;
    private final float maximum;

    public IncreaseFontAction(final float step, final float maximum)
    {
        super();
        this.step = step;
        this.maximum = maximum;
    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {
        final JHexViewer hexViewer = getHexViewer(event);
        if(hexViewer != null)
        {
            final Font font = hexViewer.getFont();
            if (font != null)
            {
                final float newFontSize = Math.min(maximum, font.getSize2D() + step);
                hexViewer.setFont(font.deriveFont(newFontSize));
            }
        }
    }
}
