package cms.rendner.hexviewer.core.uidelegate;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.uidelegate.damager.DefaultDamager;
import cms.rendner.hexviewer.core.uidelegate.damager.IDamager;
import cms.rendner.hexviewer.core.uidelegate.datatransfer.FileTransferHandler;
import cms.rendner.hexviewer.core.uidelegate.row.template.factory.DefaultRowTemplateFactory;
import cms.rendner.hexviewer.core.uidelegate.row.template.factory.IRowTemplateFactory;
import cms.rendner.hexviewer.core.uidelegate.rows.DefaultPaintDelegate;
import cms.rendner.hexviewer.core.uidelegate.rows.IPaintDelegate;
import cms.rendner.hexviewer.core.uidelegate.scrollable.delegate.DefaultScrollableDelegate;
import cms.rendner.hexviewer.core.view.caret.DefaultCaret;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.highlight.DefaultHighlighter;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import cms.rendner.hexviewer.swing.scrollable.IScrollableDelegate;
import cms.rendner.hexviewer.utils.FontUtils;
import org.intellij.lang.annotations.MagicConstant;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Basis of a JHexViewer component look-and-feel (LAF).
 * Most state is held in the associated JHexViewer as bound properties, and the UI installs default values for the various properties.
 * This default will install something for all of the properties. Typically, a LAF implementation will do more however.
 * At a minimum, a LAF would generally install key bindings.
 *
 * @author rendner
 */
public class BasicHexViewerUI extends HexViewerUI
{
    /**
     * Reference to the {@link JHexViewer} component.
     */
    protected JHexViewer hexViewer;

    /**
     * Returns an instance of the UI delegate for the specified component.
     *
     * @param c the component.
     * @return the ui delegate to use.
     */
    public static ComponentUI createUI(final JComponent c)
    {
        return new BasicHexViewerUI();
    }

    @Override
    public void installUI(final JComponent c)
    {
        super.installUI(c);

        hexViewer = (JHexViewer) c;

        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    @Override
    public void uninstallUI(final JComponent c)
    {
        uninstallListeners();
        uninstallDefaults();
        uninstallKeyboardActions();

        hexViewer = null;

        super.uninstallUI(c);
    }

    /**
     * Initializes component properties, such as font, foreground,
     * background, caret color, selection color, selected text color,
     * disabled text color, and border color.  The font, foreground, and
     * background properties are only set if their current value is either null
     * or a UIResource, other properties are set if the current
     * value is null.
     *
     * @see #uninstallDefaults
     * @see #installUI
     */
    protected void installDefaults()
    {
        final String fontName = FontUtils.getMonospacedFontName();
        hexViewer.setFont(new Font(fontName, Font.PLAIN, 14));

        final IDamager damager = hexViewer.getDamager().orElse(null);
        if (damager == null || damager instanceof UIResource)
        {
            hexViewer.setDamager(createDamager());
        }

        final IRowTemplateFactory rowTemplateFactory = hexViewer.getRowTemplateFactory().orElse(null);
        if (rowTemplateFactory == null || rowTemplateFactory instanceof UIResource)
        {
            hexViewer.setRowTemplateFactory(createRowTemplateFactory());
        }

        final TransferHandler transferHandler = hexViewer.getTransferHandler();
        if (transferHandler == null || transferHandler instanceof UIResource)
        {
            hexViewer.setTransferHandler(createTransferHandler());
        }

        final ICaret caret = hexViewer.getCaret().orElse(null);
        if (caret == null || caret instanceof UIResource)
        {
            hexViewer.setCaret(createCaret());
        }

        final IHighlighter highlighter = hexViewer.getHighlighter();
        if (highlighter == null || highlighter instanceof UIResource)
        {
            hexViewer.setHighlighter(createHighlighter());
        }

        final IScrollableDelegate scrollableDelegate = hexViewer.getScrollableDelegate();
        if (scrollableDelegate == null || scrollableDelegate instanceof UIResource)
        {
            hexViewer.setScrollableDelegate(createScrollableDelegate());
        }

        final IPaintDelegate paintRowsDelegate = hexViewer.getPaintDelegate();
        if (paintRowsDelegate == null || paintRowsDelegate instanceof UIResource)
        {
            hexViewer.setPaintDelegate(createPaintRowsDelegate());
        }
    }

    /**
     * Sets the component properties that have not been explicitly overridden
     * to {@code null}.  A property is considered overridden if its current
     * value is not a {@code UIResource}.
     *
     * @see #installDefaults
     * @see #uninstallUI
     */
    protected void uninstallDefaults()
    {
        if (hexViewer.getTransferHandler() instanceof UIResource)
        {
            hexViewer.setTransferHandler(null);
        }

        if (hexViewer.getRowTemplateFactory().orElse(null) instanceof UIResource)
        {
            hexViewer.setRowTemplateFactory(null);
        }

        if (hexViewer.getCaret().orElse(null) instanceof UIResource)
        {
            hexViewer.setCaret(null);
        }

        if (hexViewer.getDamager().orElse(null) instanceof UIResource)
        {
            hexViewer.setDamager(null);
        }

        if (hexViewer.getHighlighter() instanceof UIResource)
        {
            hexViewer.setHighlighter(null);
        }

        if (hexViewer.getScrollableDelegate() instanceof UIResource)
        {
            hexViewer.setScrollableDelegate(null);
        }

        if (hexViewer.getPaintDelegate() instanceof UIResource)
        {
            hexViewer.setPaintDelegate(null);
        }
    }

    /**
     * Registers the keyboard bindings on the <code>JList</code> that the
     * <code>BasicListUI</code> is associated with. This method is called at
     * installUI() time.
     *
     * @see #installUI
     */
    protected void installKeyboardActions()
    {
        final InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        SwingUtilities.replaceUIInputMap(hexViewer, JComponent.WHEN_FOCUSED, inputMap);

        final ActionMap actionMap = getActionMap();
        SwingUtilities.replaceUIActionMap(hexViewer, actionMap);
    }

    /**
     * Uninstalls keyboard actions installed from
     * <code>installKeyboardActions</code>.
     * This method is called at uninstallUI() time - subclasses should
     * ensure that all of the keyboard actions registered at installUI
     * time are removed here.
     *
     * @see #installUI
     */
    protected void uninstallKeyboardActions()
    {
        SwingUtilities.replaceUIActionMap(hexViewer, null);
        SwingUtilities.replaceUIInputMap(hexViewer, JComponent.WHEN_FOCUSED, null);
    }

    /**
     * Creates an map of actions.
     * <p/>
     * ActionMap provides mappings from Objects (called keys or Action names) to Actions. An ActionMap is usually used
     * with an InputMap to locate a particular action when a key is pressed.
     *
     * @return the map of actions.
     */
    protected ActionMap getActionMap()
    {
        return null;
    }

    /**
     * Returns the InputMap that is used during condition.
     * <p/>
     * InputMap provides a binding between an input event (currently only KeyStrokes are used) and an Object.
     * InputMaps are usually used with an ActionMap, to determine an Action to perform when a key is pressed.
     *
     * @param condition one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     * @return the InputMap for the specified condition
     */
    protected InputMap getInputMap(@MagicConstant(flags = {JComponent.WHEN_IN_FOCUSED_WINDOW,JComponent.WHEN_FOCUSED,JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT})
                                   final int condition)
    {
        return null;
    }

    /**
     * Installs listeners for the UI.
     */
    protected void installListeners()
    {
    }

    /**
     * Uninstalls listeners for the UI.
     */
    protected void uninstallListeners()
    {
    }

    /**
     * Creates the object to use for adding highlights.  By default
     * an instance of BasicHighlighter is created.  This method
     * can be redefined to provide something else that implements
     * the Highlighter interface or a subclass of DefaultHighlighter.
     *
     * @return the highlighter
     */
    protected IHighlighter createHighlighter()
    {
        return new BasicHighlighter();
    }

    /**
     * Creates the object to use as damager. By default an
     * instance of BasicDamager is created. This method
     * can be redefined to provide something else.
     *
     * @return the damager object
     */
    protected IDamager createDamager()
    {
        return new BasicDamager();
    }

    /**
     * Creates the object to use for a caret. By default an
     * instance of BasicCaret is created. This method
     * can be redefined to provide something else.
     *
     * @return the caret object
     */
    protected ICaret createCaret()
    {
        return new BasicCaret();
    }

    /**
     * Creates the object to use as scroll delegate. By default an
     * instance of BasicScrollableDelegate is created. This method
     * can be redefined to provide something else.
     *
     * @return the scrollable delegate
     */
    protected IScrollableDelegate createScrollableDelegate()
    {
        return new BasicScrollableDelegate();
    }

    /**
     * Creates the object to use as transfer handler. By default an
     * instance of BasicTransferHandler is created. This method
     * can be redefined to provide something else.
     *
     * @return the scrollable delegate
     */
    protected TransferHandler createTransferHandler()
    {
        return new BasicTransferHandler();
    }

    /**
     * Creates the object to use as row template factory. By default an
     * instance of BasicRowTemplateFactory is created. This method
     * can be redefined to provide something else.
     *
     * @return the row template factory
     */
    protected IRowTemplateFactory createRowTemplateFactory()
    {
        return new BasicRowTemplateFactory();
    }

    /**
     * Creates the object to use as to paint the row templates. By default an
     * instance of BasicPaintDelegate is created. This method
     * can be redefined to provide something else.
     *
     * @return the paint delegate
     */
    protected IPaintDelegate createPaintRowsDelegate()
    {
        return new BasicPaintDelegate();
    }

    public static class BasicCaret extends DefaultCaret implements UIResource
    {
    }

    public static class BasicHighlighter extends DefaultHighlighter implements UIResource
    {
    }

    public static class BasicScrollableDelegate extends DefaultScrollableDelegate implements UIResource
    {
    }

    public static class BasicTransferHandler extends FileTransferHandler implements UIResource
    {
    }

    public static class BasicDamager extends DefaultDamager implements UIResource
    {
    }

    public static class BasicRowTemplateFactory extends DefaultRowTemplateFactory implements UIResource
    {
    }

    public static class BasicPaintDelegate extends DefaultPaintDelegate implements UIResource
    {
    }
}
