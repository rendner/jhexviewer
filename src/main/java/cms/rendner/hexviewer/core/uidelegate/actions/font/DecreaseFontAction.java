package cms.rendner.hexviewer.core.uidelegate.actions.font;

import cms.rendner.hexviewer.core.uidelegate.actions.AbstractHexViewerAction;
import cms.rendner.hexviewer.core.JHexViewer;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public class DecreaseFontAction extends AbstractHexViewerAction
{
    private final float step;
    private final float minimum;

    public DecreaseFontAction(final float step, final float minimum)
    {
        super();
        this.step = step;
        this.minimum = minimum;
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
                final float newFontSize = Math.max(minimum, font.getSize2D() - step);
                hexViewer.setFont(font.deriveFont(newFontSize));
            }
        }
    }
}
