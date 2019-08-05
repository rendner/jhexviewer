package cms.rendner.hexviewer.core.uidelegate.actions;

import cms.rendner.hexviewer.core.JHexViewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * // todo: add comment
 * @author rendner
 */
public abstract class AbstractHexViewerAction extends AbstractAction implements IHexViewerAction
{
    @Nullable
    private IActionTypeId typeId;

    @NotNull
    public IActionTypeId getTypeId()
    {
        return Objects.requireNonNull(typeId);
    }

    public void setTypeId(@NotNull final IActionTypeId typeId)
    {
        // todo: get rid of this strange workaround
        if (this.typeId != null)
        {
            throw new IllegalStateException("Type can't be overwritten.");
        }

        this.typeId = typeId;
    }

    /**
     * Determines the component to use for the action.
     * This if fetched from the source of the ActionEvent
     * if it's not null.
     *
     * @param event the ActionEvent
     * @return the component
     */
    @Nullable
    protected final JHexViewer getHexViewer(@NotNull final ActionEvent event)
    {
        // todo: return optional
        final Object source = event.getSource();
        if (source instanceof JHexViewer)
        {
            return (JHexViewer) source;
        }
        return null;
    }
}
