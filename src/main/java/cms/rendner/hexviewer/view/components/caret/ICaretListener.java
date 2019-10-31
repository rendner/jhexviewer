package cms.rendner.hexviewer.view.components.caret;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Listener for changes in the caret position.
 *
 * @author rendner
 */
public interface ICaretListener extends EventListener
{
    /**
     * Called after the caret position has changed.
     *
     * @param event the event containing information about the change.
     */
    void caretPositionChanged(@NotNull CaretEvent event);
}
