package cms.rendner.hexviewer.core.uidelegate.actions;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * // todo: add comment
 * @author rendner
 */
public interface IHexViewerAction extends Action
{
    @NotNull
    IActionTypeId getTypeId();
}
