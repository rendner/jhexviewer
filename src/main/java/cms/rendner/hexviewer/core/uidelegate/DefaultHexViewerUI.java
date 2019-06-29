package cms.rendner.hexviewer.core.uidelegate;

import cms.rendner.hexviewer.core.uidelegate.actions.ActionMapFactory;
import cms.rendner.hexviewer.core.uidelegate.actions.caret.CaretAction;
import cms.rendner.hexviewer.core.uidelegate.actions.font.FontAction;
import cms.rendner.hexviewer.core.uidelegate.actions.others.OthersAction;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.event.KeyEvent;

/**
 * Defines an ActionMap and InputMap for the JHexViewer component.
 *
 * @author rendner
 * @see ActionMap
 * @see InputMap
 * @see BasicHexViewerUI
 */
public class DefaultHexViewerUI extends BasicHexViewerUI
{
    /**
     * Returns an instance of the UI delegate for the specified component.
     *
     * @param c the component.
     * @return the ui delegate to use.
     */
    public static ComponentUI createUI(final JComponent c)
    {
        return new DefaultHexViewerUI();
    }

    @Override
    public void installUI(final JComponent c)
    {
        super.installUI(c);
    }

    @Override
    public void uninstallUI(final JComponent c)
    {
        super.uninstallUI(c);
    }

    @Override
    protected ActionMap getActionMap()
    {
        return ActionMapFactory.createActionMap();
    }

    @Override
    protected InputMap getInputMap(final int condition)
    {
        final InputMap result = new InputMap();

        if (condition == JComponent.WHEN_FOCUSED)
        {
            result.put(KeyStroke.getKeyStroke("TAB"), OthersAction.SWITCH_FOCUSED_AREA);

            result.put(KeyStroke.getKeyStroke("LEFT"), CaretAction.MOVE_CARET_LEFT);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK), CaretAction.EXPAND_SELECTION_LEFT);

            result.put(KeyStroke.getKeyStroke("RIGHT"), CaretAction.MOVE_CARET_RIGHT);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK), CaretAction.EXPAND_SELECTION_RIGHT);

            result.put(KeyStroke.getKeyStroke("UP"), CaretAction.MOVE_CARET_UP);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK), CaretAction.EXPAND_SELECTION_UP);

            result.put(KeyStroke.getKeyStroke("DOWN"), CaretAction.MOVE_CARET_DOWN);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK), CaretAction.EXPAND_SELECTION_DOWN);

            result.put(KeyStroke.getKeyStroke("PAGE_UP"), CaretAction.MOVE_CARET_PAGE_UP);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.SHIFT_DOWN_MASK), CaretAction.EXPAND_SELECTION_PAGE_UP);

            result.put(KeyStroke.getKeyStroke("PAGE_DOWN"), CaretAction.MOVE_CARET_PAGE_DOWN);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.SHIFT_DOWN_MASK), CaretAction.EXPAND_SELECTION_PAGE_DOWN);

            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK), FontAction.INCREASE_SIZE);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, KeyEvent.CTRL_DOWN_MASK), FontAction.INCREASE_SIZE);

            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK), FontAction.DECREASE_SIZE);
            result.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, KeyEvent.CTRL_DOWN_MASK), FontAction.DECREASE_SIZE);
        }

        return result;
    }
}
