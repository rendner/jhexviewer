package cms.rendner.hexviewer.core.uidelegate.actions;

import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.core.JHexViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * // todo: add comment
 * @author rendner
 */
public abstract class AbstractHexViewerAction extends AbstractAction implements IHexViewerAction
{
    private IActionTypeId typeId;

    public IActionTypeId getTypeId()
    {
        return typeId;
    }

    public void setTypeId(final IActionTypeId typeId)
    {
        if (this.typeId != null)
        {
            throw new IllegalStateException("Type can't be overwritten.");
        }

        CheckUtils.checkNotNull(typeId);

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
    protected final JHexViewer getHexViewer(final ActionEvent event)
    {
        if (event != null)
        {
            final Object source = event.getSource();
            if (source instanceof JHexViewer)
            {
                return (JHexViewer) source;
            }
        }
        return null;
    }
}
