package cms.rendner.hexviewer.core.uidelegate.actions;

import cms.rendner.hexviewer.swing.scrollable.ScrollDirection;
import cms.rendner.hexviewer.core.uidelegate.actions.caret.*;
import cms.rendner.hexviewer.core.uidelegate.actions.font.DecreaseFontAction;
import cms.rendner.hexviewer.core.uidelegate.actions.font.FontAction;
import cms.rendner.hexviewer.core.uidelegate.actions.font.IncreaseFontAction;
import cms.rendner.hexviewer.core.uidelegate.actions.others.OthersAction;
import cms.rendner.hexviewer.core.uidelegate.actions.others.SwitchAreaFocusAction;
import cms.rendner.hexviewer.utils.CheckUtils;

import javax.swing.*;

/**
 * // todo: add comment
 * @author rendner
 */
public class ActionMapFactory
{
    public static ActionMap createActionMap()
    {
        final ActionMap result = new ActionMap();

        for (final CaretAction action : CaretAction.values())
        {
            addAction(result, action);
        }

        for (final FontAction action : FontAction.values())
        {
            addAction(result, action);
        }

        for (final OthersAction action : OthersAction.values())
        {
            addAction(result, action);
        }

        return result;
    }

    protected static void addAction(final ActionMap actionMap, final IActionTypeId actionTypeId)
    {
        CheckUtils.checkNotNull(actionTypeId);

        AbstractHexViewerAction action = null;

        if (actionTypeId instanceof CaretAction)
        {
            action = createCaretAction((CaretAction) actionTypeId);
        }
        else if (actionTypeId instanceof FontAction)
        {
            action = createFontAction((FontAction) actionTypeId);
        }
        else if (actionTypeId instanceof OthersAction)
        {
            action = createOthersAction((OthersAction) actionTypeId);
        }

        if (action != null)
        {
            action.setTypeId(actionTypeId);
            action.putValue(Action.NAME, actionTypeId);

            actionMap.put(action.getValue(Action.NAME), action);
        }
    }

    private static AbstractHexViewerAction createOthersAction(final OthersAction actionType)
    {
        switch (actionType)
        {
            default:
            {
                return null;
            }
            case SWITCH_FOCUSED_AREA:
            {
                return new SwitchAreaFocusAction();
            }
        }
    }

    private static AbstractHexViewerAction createFontAction(final FontAction actionType)
    {
        switch (actionType)
        {
            default:
            {
                return null;
            }
            case INCREASE_SIZE:
            {
                return new IncreaseFontAction(1, 26);
            }
            case DECREASE_SIZE:
            {
                return new DecreaseFontAction(1, 14);
            }
        }
    }

    private static AbstractHexViewerAction createCaretAction(final CaretAction actionType)
    {
        switch (actionType)
        {
            default:
            {
                return null;
            }
            case MOVE_CARET_LEFT:
            {
                return new CaretLeftAction(false);
            }
            case EXPAND_SELECTION_LEFT:
            {
                return new CaretLeftAction(true);
            }
            case MOVE_CARET_RIGHT:
            {
                return new CaretRightAction(false);
            }
            case EXPAND_SELECTION_RIGHT:
            {
                return new CaretRightAction(true);
            }
            case MOVE_CARET_UP:
            {
                return new CaretUpAction(false);
            }
            case EXPAND_SELECTION_UP:
            {
                return new CaretUpAction(true);
            }
            case MOVE_CARET_DOWN:
            {
                return new CaretDownAction(false);
            }
            case EXPAND_SELECTION_DOWN:
            {
                return new CaretDownAction(true);
            }
            case MOVE_CARET_PAGE_UP:
            {
                return new VerticalPageAction(ScrollDirection.UP, false);
            }
            case EXPAND_SELECTION_PAGE_UP:
            {
                return new VerticalPageAction(ScrollDirection.UP, true);
            }
            case MOVE_CARET_PAGE_DOWN:
            {
                return new VerticalPageAction(ScrollDirection.DOWN, false);
            }
            case EXPAND_SELECTION_PAGE_DOWN:
            {
                return new VerticalPageAction(ScrollDirection.DOWN, true);
            }
        }
    }
}
