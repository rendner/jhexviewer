package cms.rendner.hexviewer.view.ui.actions;

import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

/**
 * This class provides default implementations for the {@link IHexViewerAction}.
 * The developer need only subclass this abstract class and define the actionPerformed method.
 *
 * @author rendner
 */
public abstract class AbstractHexViewerAction extends AbstractAction implements IHexViewerAction
{
    /**
     * Determines the hexViewer component to use for the action.
     * This if fetched from the source of the ActionEvent if it's not null.
     *
     * @param event the ActionEvent
     * @return the hexViewer component
     */
    @NotNull
    protected final Optional<JHexViewer> getHexViewer(@NotNull final ActionEvent event)
    {
        final Object source = event.getSource();
        if (source instanceof JHexViewer)
        {
            return Optional.of((JHexViewer) source);
        }
        return Optional.empty();
    }
}
