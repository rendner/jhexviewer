package cms.rendner.hexviewer.core.uidelegate.actions.caret;

import cms.rendner.hexviewer.core.uidelegate.actions.IActionTypeId;

/**
 * // todo: add comment
 * @author rendner
 */
public enum CaretAction implements IActionTypeId
{
    MOVE_CARET_LEFT,
    EXPAND_SELECTION_LEFT,
    MOVE_CARET_RIGHT,
    EXPAND_SELECTION_RIGHT,
    MOVE_CARET_DOWN,
    EXPAND_SELECTION_DOWN,
    MOVE_CARET_UP,
    EXPAND_SELECTION_UP,
    MOVE_CARET_PAGE_UP,
    EXPAND_SELECTION_PAGE_UP,
    MOVE_CARET_PAGE_DOWN,
    EXPAND_SELECTION_PAGE_DOWN
}
