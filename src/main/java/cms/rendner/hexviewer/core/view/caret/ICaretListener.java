package cms.rendner.hexviewer.core.view.caret;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Listener for changes in the caret position of a text component.
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
