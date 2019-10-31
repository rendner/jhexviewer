package cms.rendner.hexviewer.view.ui.actions;

import cms.rendner.hexviewer.view.ui.actions.caret.*;
import cms.rendner.hexviewer.view.ui.actions.font.DecreaseFontAction;
import cms.rendner.hexviewer.view.ui.actions.font.FontAction;
import cms.rendner.hexviewer.view.ui.actions.font.IncreaseFontAction;
import cms.rendner.hexviewer.view.ui.actions.others.OthersAction;
import cms.rendner.hexviewer.view.ui.actions.others.RepackWindowAction;
import cms.rendner.hexviewer.view.ui.actions.others.SelectAllAction;
import cms.rendner.hexviewer.view.ui.actions.others.SwitchAreaFocusAction;
import cms.rendner.hexviewer.view.ui.container.common.ScrollDirection;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 *
 * @author rendner
 */
public class ActionMapFactory
{
    @NotNull
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

    protected static void addAction(@NotNull final ActionMap actionMap, @NotNull final IActionTypeId actionTypeId)
    {
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
            action.putValue(Action.NAME, actionTypeId);
            actionMap.put(action.getValue(Action.NAME), action);
        }
    }

    @NotNull
    private static AbstractHexViewerAction createOthersAction(@NotNull final OthersAction actionType)
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
            case SELECT_ALL:
            {
                return new SelectAllAction();
            }
            case REPACK_WINDOW:
            {
                return new RepackWindowAction();
            }
        }
    }

    @NotNull
    private static AbstractHexViewerAction createFontAction(@NotNull final FontAction actionType)
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

    @NotNull
    private static AbstractHexViewerAction createCaretAction(@NotNull final CaretAction actionType)
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
