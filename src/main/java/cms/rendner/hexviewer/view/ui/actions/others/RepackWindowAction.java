package cms.rendner.hexviewer.view.ui.actions.others;

import cms.rendner.hexviewer.view.ui.actions.AbstractHexViewerAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author rendner
 */
public class RepackWindowAction extends AbstractHexViewerAction
{
    @Override
    public void actionPerformed(@NotNull final ActionEvent event)
    {
        getHexViewer(event).ifPresent(hexViewer -> {
            final Window window = SwingUtilities.getWindowAncestor(hexViewer);
            if (window != null)
            {
                if (window instanceof JFrame)
                {
                    final JFrame frame = (JFrame) window;
                    if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH)
                    {
                        return;
                    }
                }

                window.pack();
            }
        });
    }
}
