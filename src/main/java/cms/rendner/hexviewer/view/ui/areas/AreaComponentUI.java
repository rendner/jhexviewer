package cms.rendner.hexviewer.view.ui.areas;

import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * The ui delegate to define the look-and-feel (LAF) of an {@link AreaComponent}.
 *
 * @author rendner
 */
public class AreaComponentUI extends ComponentUI
{
    /**
     * Reference to the {@link AreaComponent} component.
     */
    @NotNull
    private final AreaComponent component;

    /**
     * Is used to paint the whole area component.
     */
    private IPainterDelegate painterDelegate;

    protected AreaComponentUI(@NotNull final AreaComponent component)
    {
        this.component = component;
    }

    /**
     * Returns an instance of the UI delegate for the specified component.
     *
     * @param c the component.
     * @return the ui delegate to use.
     */
    public static ComponentUI createUI(@NotNull final JComponent c)
    {
        return new AreaComponentUI((AreaComponent) c);
    }

    /**
     * Delegates the painting to an {@link IPainterDelegate} which provides access to the {@link cms.rendner.hexviewer.view.JHexViewer}
     * for the {@link cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter} installed to the internal
     * component.
     *
     * @param g       the Graphics context in which to paint
     * @param ignored this argument is ignored
     */
    @Override
    public void paint(final Graphics g, final JComponent ignored)
    {
        if (painterDelegate != null)
        {
            painterDelegate.paint((Graphics2D) g, component);
        }
    }

    /**
     * Sets the new painter which is responsible for painting the content of the component.
     * <p/>
     * Setting a new painter results in a repaint of the component.
     *
     * @param painterDelegate the new painterDelegate, if <code>null</code> no content can be drawn.
     */
    public void setPainterDelegate(final IPainterDelegate painterDelegate)
    {
        if (this.painterDelegate != painterDelegate)
        {
            this.painterDelegate = painterDelegate;
            component.repaint();
        }
    }
}
