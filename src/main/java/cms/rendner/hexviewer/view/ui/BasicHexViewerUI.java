package cms.rendner.hexviewer.view.ui;

import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.common.utils.FontUtils;
import cms.rendner.hexviewer.common.utils.UIDelegateUtils;
import cms.rendner.hexviewer.model.rowtemplate.configuration.AsciiRowTemplateConfiguration;
import cms.rendner.hexviewer.model.rowtemplate.configuration.HexRowTemplateConfiguration;
import cms.rendner.hexviewer.model.rowtemplate.configuration.OffsetRowTemplateConfiguration;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.AsciiArea;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.HexArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.painter.BasicAreaPainter;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;
import cms.rendner.hexviewer.view.components.caret.DefaultCaret;
import cms.rendner.hexviewer.view.components.caret.ICaret;
import cms.rendner.hexviewer.view.components.caret.ICaretListener;
import cms.rendner.hexviewer.view.components.damager.DefaultDamager;
import cms.rendner.hexviewer.view.components.damager.IDamager;
import cms.rendner.hexviewer.view.components.highlighter.DefaultHighlighter;
import cms.rendner.hexviewer.view.components.highlighter.IHighlighter;
import cms.rendner.hexviewer.view.ui.container.bytes.ByteAreasContainer;
import cms.rendner.hexviewer.view.ui.container.offset.OffsetAreaContainer;
import cms.rendner.hexviewer.view.ui.datatransfer.FileTransferHandler;
import cms.rendner.hexviewer.view.ui.painter.bytes.ByteAreaPainter;
import cms.rendner.hexviewer.view.ui.painter.offset.OffsetAreaPainter;
import cms.rendner.hexviewer.view.ui.rowtemplate.factory.bytes.AsciiRowTemplateFactory;
import cms.rendner.hexviewer.view.ui.rowtemplate.factory.bytes.HexRowTemplateFactory;
import cms.rendner.hexviewer.view.ui.rowtemplate.factory.offset.OffsetRowTemplateFactory;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basis of a JHexViewer component look-and-feel (LAF).
 * Most state is held in the associated JHexViewer as bound properties, and the UI installs default values for the various properties.
 * This will install something for all of the properties. Typically, a LAF implementation will do more however.
 *
 * @author rendner
 */
public class BasicHexViewerUI extends HexViewerUI
{
    /**
     * Reference to the {@link JHexViewer} component.
     */
    @NotNull
    protected final JHexViewer hexViewer;

    /**
     * The scroll pane which arranges the internal sub views.
     */
    protected JScrollPane scrollPane;

    /**
     * The container component which contains the offset-area.
     */
    protected OffsetAreaContainer offsetAreaContainer;

    /**
     * The container component which contains the hex and ascii-area.
     */
    protected ByteAreasContainer byteAreasContainer;

    /**
     * Used to listen for JComponent property changes.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * Used for watching when the context menu should be opened.
     */
    private MouseAdapter mouseAdapter;

    /**
     * Used to listen for caret events to adjust internal stuff.
     */
    private ICaretListener caretListener;

    /**
     * The row template factory used to create row templates for the offset-area.
     */
    @NotNull
    private final OffsetRowTemplateFactory offsetRowTemplateFactory = new OffsetRowTemplateFactory();

    /**
     * The row template factory used to create row templates for the ascii-area.
     */
    @NotNull
    private final AsciiRowTemplateFactory asciiRowTemplateFactory = new AsciiRowTemplateFactory();

    /**
     * The row template factory used to create row templates for the hex-area.
     */
    @NotNull
    private final HexRowTemplateFactory hexRowTemplateFactory = new HexRowTemplateFactory();

    protected BasicHexViewerUI(@NotNull final JHexViewer hexViewer)
    {
        super();
        this.hexViewer = hexViewer;
    }

    /**
     * Returns an instance of the UI delegate for the specified component.
     *
     * @param c the component.
     * @return the ui delegate to use.
     */
    public static ComponentUI createUI(@NotNull final JComponent c)
    {
        return new BasicHexViewerUI((JHexViewer) c);
    }

    @Override
    public void installUI(@NotNull final JComponent c)
    {
        super.installUI(c);

        installDefaults();

        updateAreaRowCount();
        updateAreaRowTemplates();

        installComponents();
        installListeners();
        installKeyboardActions();
    }

    @Override
    public void uninstallUI(@NotNull final JComponent c)
    {
        uninstallListeners();
        uninstallDefaults();
        uninstallKeyboardActions();

        super.uninstallUI(c);
    }

    /**
     * Installs components used by this UI to align the areas of the {@link JHexViewer}.
     */
    protected void installComponents()
    {
        byteAreasContainer = new ByteAreasContainer(hexViewer.getHexArea(), hexViewer.getAsciiArea());
        offsetAreaContainer = new OffsetAreaContainer(hexViewer.getOffsetArea());

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(byteAreasContainer);
        scrollPane.setRowHeaderView(offsetAreaContainer);

        hexViewer.setLayout(new BorderLayout());
        hexViewer.add(scrollPane, BorderLayout.CENTER);
        hexViewer.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Initializes component properties, such as font, damager,
     * background, or highlighter.
     * These properties are only set if their current value is either {@code null} or an {@code UIResource}.
     * <p/>
     * Subclasses should ensure that all of the defaults initialized here are removed in {@link BasicHexViewerUI#uninstallDefaults()}.
     */
    protected void installDefaults()
    {
        final String fontName = FontUtils.getMonospacedFontName();
        hexViewer.setRowContentFont(new FontUIResource(fontName, Font.PLAIN, 14));

        if (UIDelegateUtils.canInstallValue(hexViewer.getTransferHandler()))
        {
            hexViewer.setTransferHandler(createTransferHandler());
        }

        if (UIDelegateUtils.canInstallValue(hexViewer.getDamager().orElse(null)))
        {
            hexViewer.setDamager(createDamager());
        }

        if (UIDelegateUtils.canInstallValue(hexViewer.getHighlighter().orElse(null)))
        {
            hexViewer.setHighlighter(createHighlighter());
        }

        if (UIDelegateUtils.canInstallValue(hexViewer.getCaret().orElse(null)))
        {
            hexViewer.setCaret(createCaret());
        }

        if (UIDelegateUtils.canInstallValue(hexViewer.getOffsetRowTemplateConfiguration().orElse(null)))
        {
            hexViewer.setOffsetRowTemplateConfiguration(createOffsetRowTemplateConfiguration());
        }

        if (UIDelegateUtils.canInstallValue(hexViewer.getHexRowTemplateConfiguration().orElse(null)))
        {
            hexViewer.setHexRowTemplateConfiguration(createHexRowTemplateConfiguration());
        }

        if (UIDelegateUtils.canInstallValue(hexViewer.getAsciiRowTemplateConfiguration().orElse(null)))
        {
            hexViewer.setAsciiRowTemplateConfiguration(createAsciiRowTemplateConfiguration());
        }

        installOffsetAreaDefaults();
        installByteAreaDefaults(hexViewer.getHexArea());
        installByteAreaDefaults(hexViewer.getAsciiArea());
    }

    /**
     * Sets the component properties that have not been explicitly overridden to {@code null}.
     * A property is considered overridden if its current value is not an {@code UIResource}.
     * <p/>
     * Subclasses should ensure that all of the defaults initialized in {@link BasicHexViewerUI#installDefaults()}
     * are removed here.
     */
    protected void uninstallDefaults()
    {
        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getTransferHandler()))
        {
            hexViewer.setTransferHandler(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getDamager().orElse(null)))
        {
            hexViewer.setDamager(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getHighlighter().orElse(null)))
        {
            hexViewer.setHighlighter(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getCaret().orElse(null)))
        {
            hexViewer.setCaret(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getOffsetRowTemplateConfiguration().orElse(null)))
        {
            hexViewer.setOffsetRowTemplateConfiguration(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getHexRowTemplateConfiguration().orElse(null)))
        {
            hexViewer.setHexRowTemplateConfiguration(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(hexViewer.getAsciiRowTemplateConfiguration().orElse(null)))
        {
            hexViewer.setAsciiRowTemplateConfiguration(null);
        }

        uninstallOffsetAreaDefaults();
        uninstallByteAreaDefaults(hexViewer.getHexArea());
        uninstallByteAreaDefaults(hexViewer.getAsciiArea());
    }

    /**
     * Initializes offset-area component properties, such as background, colorProvider, or painter.
     * <p/>
     * These properties are only set if their current value is either {@code null} or an {@code UIResource}.
     * Subclasses should ensure that all of the defaults initialized here are removed in {@link BasicHexViewerUI#uninstallOffsetAreaDefaults()}.
     */
    protected void installOffsetAreaDefaults()
    {
        final OffsetArea area = hexViewer.getOffsetArea();
        if (UIDelegateUtils.canInstallValue(area.getColorProvider().orElse(null)))
        {
            area.setColorProvider(createOffsetColorProvider());
        }

        if (UIDelegateUtils.canInstallValue(area.getPainter().orElse(null)))
        {
            area.setPainter(createOffsetAreaPainter());
        }
    }

    /**
     * Sets the offset-area defaults, initialized in {@link BasicHexViewerUI#installOffsetAreaDefaults()}, that have
     * not been explicitly overridden to {@code null}.
     * <p/>
     * A property is considered overridden if its current value is not an {@code UIResource}.
     * <p/>
     * Subclasses should ensure that all of the defaults initialized in {@link BasicHexViewerUI#installOffsetAreaDefaults()}
     * are removed here.
     */
    protected void uninstallOffsetAreaDefaults()
    {
        final OffsetArea area = hexViewer.getOffsetArea();
        if (UIDelegateUtils.shouldUninstallValue(area.getColorProvider().orElse(null)))
        {
            area.setColorProvider(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(area.getPainter().orElse(null)))
        {
            area.setPainter(null);
        }
    }

    /**
     * Initializes byte-area component properties, such as background, colorProvider, or painter.
     * <p/>
     * These properties are only set if their current value is either {@code null} or an {@code UIResource}.
     * Subclasses should ensure that all of the defaults initialized here are removed in {@link BasicHexViewerUI#uninstallByteAreaDefaults(ByteArea)}.
     */
    protected void installByteAreaDefaults(@NotNull final ByteArea area)
    {
        if (UIDelegateUtils.canInstallValue(area.getColorProvider().orElse(null)))
        {
            area.setColorProvider(createByteColorProvider(area));
        }

        if (UIDelegateUtils.canInstallValue(area.getPainter().orElse(null)))
        {
            area.setPainter(createByteAreaPainter(area));
        }
    }

    /**
     * Sets the offset-area defaults, initialized in {@link BasicHexViewerUI#installByteAreaDefaults(ByteArea)}, that have
     * not been explicitly overridden to {@code null}.
     * <p/>
     * A property is considered overridden if its current value is not an {@code UIResource}.
     * <p/>
     * Subclasses should ensure that all of the defaults initialized in {@link BasicHexViewerUI#installByteAreaDefaults(ByteArea)}
     * are removed here.
     */
    protected void uninstallByteAreaDefaults(@NotNull final ByteArea area)
    {
        if (UIDelegateUtils.shouldUninstallValue(area.getColorProvider().orElse(null)))
        {
            area.setColorProvider(null);
        }

        if (UIDelegateUtils.shouldUninstallValue(area.getPainter().orElse(null)))
        {
            area.setPainter(null);
        }
    }

    /**
     * Registers the keyboard bindings on the {@link JHexViewer} that this UI is associated with.
     * <p/>
     * Subclasses should ensure that all of the keyboard bindings registered here are removed in {@link BasicHexViewerUI#uninstallKeyboardActions()}.
     */
    protected void installKeyboardActions()
    {
        final InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        SwingUtilities.replaceUIInputMap(hexViewer, JComponent.WHEN_FOCUSED, inputMap);

        final ActionMap actionMap = getActionMap();
        SwingUtilities.replaceUIActionMap(hexViewer, actionMap);
    }

    /**
     * Uninstalls keyboard bindings registered in {@link BasicHexViewerUI#installKeyboardActions}.
     * <p/>
     * Subclasses should ensure that all of the keyboard bindings registered in {@link BasicHexViewerUI#installKeyboardActions}
     * are removed here.
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
    protected InputMap getInputMap(@MagicConstant(flags = {
            JComponent.WHEN_IN_FOCUSED_WINDOW,
            JComponent.WHEN_FOCUSED,
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT}) final int condition)
    {
        return null;
    }

    /**
     * Installs listeners for the UI.
     */
    protected void installListeners()
    {
        mouseAdapter = createMouseAdapter();
        caretListener = createCaretListener();
        propertyChangeListener = createPropertyChangeListener();

        hexViewer.addPropertyChangeListener(propertyChangeListener);
        hexViewer.getCaret().ifPresent(model -> model.addCaretListener(caretListener));

        final OffsetArea offsetArea = hexViewer.getOffsetArea();
        offsetArea.addPropertyChangeListener(propertyChangeListener);

        final HexArea hexArea = hexViewer.getHexArea();
        hexArea.addPropertyChangeListener(propertyChangeListener);
        hexArea.addMouseListener(mouseAdapter);

        final AsciiArea asciiArea = hexViewer.getAsciiArea();
        asciiArea.addPropertyChangeListener(propertyChangeListener);
        asciiArea.addMouseListener(mouseAdapter);
    }

    /**
     * Uninstalls listeners for the UI.
     */
    protected void uninstallListeners()
    {
        hexViewer.removePropertyChangeListener(propertyChangeListener);
        hexViewer.getCaret().ifPresent(model -> model.removeCaretListener(caretListener));

        final OffsetArea offsetArea = hexViewer.getOffsetArea();
        offsetArea.removePropertyChangeListener(propertyChangeListener);

        final HexArea hexArea = hexViewer.getHexArea();
        hexArea.removePropertyChangeListener(propertyChangeListener);
        hexArea.removeMouseListener(mouseAdapter);

        final AsciiArea asciiArea = hexViewer.getAsciiArea();
        asciiArea.removePropertyChangeListener(propertyChangeListener);
        asciiArea.removeMouseListener(mouseAdapter);

        propertyChangeListener = null;
        caretListener = null;
        mouseAdapter = null;
    }

    private void updateAreaRowCount()
    {
        final int rowCount = calculateAreaRowCount();
        hexViewer.getOffsetArea().setRowCount(rowCount);
        hexViewer.getHexArea().setRowCount(rowCount);
        hexViewer.getAsciiArea().setRowCount(rowCount);
    }

    private int calculateAreaRowCount()
    {
        return 1 + hexViewer.byteIndexToRowIndex(hexViewer.getLastPossibleCaretIndex());
    }

    private void updateAreaRowTemplates()
    {
        updateOffsetRowTemplate();
        hexViewer.getHexArea().setRowTemplate(hexRowTemplateFactory.createTemplate(hexViewer));
        hexViewer.getAsciiArea().setRowTemplate(asciiRowTemplateFactory.createTemplate(hexViewer));
    }

    private void updateOffsetRowTemplate()
    {
        final IOffsetRowTemplate rowTemplate = offsetRowTemplateFactory.createTemplate(hexViewer);
        hexViewer.getOffsetArea().setRowTemplate(rowTemplate);

        final int padSize;
        if (rowTemplate != null)
        {
            padSize = rowTemplate.padSize();
        }
        else
        {
            padSize = hexViewer.getOffsetRowTemplateConfiguration()
                    .map(OffsetRowTemplateConfiguration::minPadSize)
                    .orElse(4);
        }

        hexViewer.getOffsetArea().getValueFormatter().adjustPadSize(padSize);
    }

    /**
     * Creates the object to use as transfer handler.
     * This method can be redefined to provide something else.
     *
     * @return the transfer handler
     */
    @Nullable
    protected TransferHandler createTransferHandler()
    {
        return new TransferHandlerUIResource();
    }

    /**
     * Creates the caret for the {@link JHexViewer}.
     * This method can be redefined to provide something else.
     *
     * @return the caret.
     */
    @Nullable
    protected ICaret createCaret()
    {
        return new CaretUIResource();
    }

    /**
     * Creates the object to damage specific parts of the byte areas.
     * This method can be redefined to provide something else.
     *
     * @return the damager.
     */
    @Nullable
    protected IDamager createDamager()
    {
        return new AreaDamagerUIResource();
    }

    /**
     * Creates the object to use for adding highlights.
     * This method can be redefined to provide something else.
     *
     * @return the highlighter.
     */
    @Nullable
    protected IHighlighter createHighlighter()
    {
        return new HighlighterUIResource();
    }

    /**
     * Creates the color provider for the offset-area.
     * This method can be redefined to provide something else.
     *
     * @return the color provider.
     */
    @Nullable
    protected IOffsetColorProvider createOffsetColorProvider()
    {
        return new OffsetColorProviderUIResource();
    }

    /**
     * Creates the area painter for the offset-area.
     * This method can be redefined to provide something else.
     *
     * @return the area painter.
     */
    @Nullable
    protected BasicAreaPainter createOffsetAreaPainter()
    {
        return new OffsetAreaPainterUIResource(hexViewer);
    }

    /**
     * Creates the row-template configuration for the offset-area.
     * This method can be redefined to provide something else.
     *
     * @return the row-template configuration.
     */
    @Nullable
    protected OffsetRowTemplateConfiguration createOffsetRowTemplateConfiguration()
    {
        return OffsetRowTemplateConfiguration.newBuilder().build();
    }

    /**
     * Creates the color provider for a byte-area.
     * This method can be redefined to provide something else.
     *
     * @param area the area for which the color provider to create.
     * @return the color provider.
     */
    @Nullable
    protected IByteColorProvider createByteColorProvider(@NotNull final ByteArea area)
    {
        return new ByteColorProviderUIResource();
    }

    /**
     * Creates the area painter for a byte-area.
     * This method can be redefined to provide something else.
     *
     * @param area the area for which the area painter to create.
     * @return the area painter.
     */
    @Nullable
    protected BasicAreaPainter createByteAreaPainter(@NotNull final ByteArea area)
    {
        return new ByteAreaPainterUIResource(hexViewer, area);
    }

    /**
     * Creates the row-template configuration for the hex-area.
     * This method can be redefined to provide something else.
     *
     * @return the row-template configuration.
     */
    @Nullable
    protected HexRowTemplateConfiguration createHexRowTemplateConfiguration()
    {
        return HexRowTemplateConfiguration.newBuilder().build();
    }

    /**
     * Creates the row-template configuration for the ascii-area.
     * This method can be redefined to provide something else.
     *
     * @return the row-template configuration.
     */
    @Nullable
    protected AsciiRowTemplateConfiguration createAsciiRowTemplateConfiguration()
    {
        return AsciiRowTemplateConfiguration.newBuilder().build();
    }

    @NotNull
    private MouseAdapter createMouseAdapter()
    {
        return new MouseAdapter()
        {
            @Override
            public void mousePressed(final MouseEvent event)
            {
                handleMouseEvent(event);
            }

            @Override
            public void mouseReleased(final MouseEvent event)
            {
                handleMouseEvent(event);
            }
        };
    }

    @NotNull
    private ICaretListener createCaretListener()
    {
        return event -> {
            final long oldIndex = event.getOldDot();
            final long newIndex = event.getNewDot();
            if (oldIndex != newIndex)
            {
                if (hexViewer.isShowOffsetCaretIndicator())
                {
                    final int oldRowIndex = hexViewer.byteIndexToRowIndex(oldIndex);
                    final int newRowIndex = hexViewer.byteIndexToRowIndex(newIndex);

                    // the offset-row-view displays the offset of the caret in the row of the caret
                    final OffsetArea offsetArea = hexViewer.getOffsetArea();
                    offsetArea.damageRow(oldRowIndex);

                    if (oldRowIndex != newRowIndex)
                    {
                        offsetArea.damageRow(newRowIndex);
                    }
                }
            }
        };
    }

    @NotNull
    private PropertyChangeListener createPropertyChangeListener()
    {
        return event -> {
            final Object eventSource = event.getSource();

            if (eventSource == hexViewer)
            {
                handleHexViewerPropertyChanges(event);
            }
            else if (eventSource instanceof Area)
            {
                handleAreaPropertyChanges(event);
            }
        };
    }

    private void handleMouseEvent(@NotNull final MouseEvent event)
    {
        // Note: Popup menus are triggered differently on different systems.
        // Therefore, isPopupTrigger should be checked in both mousePressed and mouseReleased for
        // proper cross-platform functionality.
        if (event.isPopupTrigger())
        {
            if (event.getSource() instanceof ByteArea)
            {
                showContextMenu((ByteArea) event.getSource(), event.getPoint());
            }
        }
    }

    private void showContextMenu(@NotNull final ByteArea area, @NotNull final Point locationInRowsView)
    {
        hexViewer.getContextMenuFactory().ifPresent(factory -> {
                    final long maxByteIndex = hexViewer.getLastPossibleByteIndex();
                    final long byteIndex = area
                            .hitTest(locationInRowsView.x, locationInRowsView.y)
                            .map(byteHitInfo -> Math.min(byteHitInfo.index(), maxByteIndex))
                            .orElse(maxByteIndex);
                    final JPopupMenu menu = factory.create(hexViewer, area.getId(), byteIndex);

                    if (menu != null)
                    {
                        menu.show(area, locationInRowsView.x, locationInRowsView.y);
                    }
                }
        );
    }

    private void handleAreaPropertyChanges(@NotNull final PropertyChangeEvent event)
    {
        final String propertyName = event.getPropertyName();
        if (Area.PROPERTY_ROW_COUNT.equals(propertyName))
        {
            final int rowCount = calculateAreaRowCount();
            if (((Integer) event.getNewValue()) != rowCount)
            {
                throw new IllegalStateException("The 'rowCount' can't be set directly, this value is calculated from the data model of the 'JHexViewer'.");
            }
        }
        if ("font".equals(propertyName))
        {
            if (hexViewer.getRowContentFont().orElse(null) != event.getNewValue())
            {
                throw new IllegalStateException("The area 'font' can't be set directly. Use 'JHexViewer.setRowContentFont' to change the font of the areas.");
            }
        }
    }

    private void handleHexViewerPropertyChanges(@NotNull final PropertyChangeEvent event)
    {
        final String propertyName = event.getPropertyName();
        if (JHexViewer.PROPERTY_ROW_CONTENT_FONT.equals(propertyName))
        {
            updateAreaRowTemplates();
        }
        else if (JHexViewer.PROPERTY_DATA_MODEL.equals(propertyName))
        {
            hexViewer.getCaret().ifPresent(caret -> caret.moveCaret(0, false, true));
            hexViewer.getHighlighter().ifPresent(IHighlighter::removeAllHighlights);
            updateAreaRowCount();
            updateAreaRowTemplates();
        }
        else if (JHexViewer.PROPERTY_PREFERRED_VISIBLE_ROW_COUNT.equals(propertyName))
        {
            final int newValue = hexViewer.getPreferredVisibleRowCount();
            offsetAreaContainer.setPreferredVisibleRowCount(newValue);
            byteAreasContainer.setPreferredVisibleRowCount(newValue);
        }
        else if (JHexViewer.PROPERTY_OFFSET_ROW_TEMPLATE_CONFIGURATION.equals(propertyName))
        {
            updateOffsetRowTemplate();
        }
        else if ("background".equals(propertyName))
        {
            final Color newValue = hexViewer.getBackground();
            scrollPane.setBackground(newValue);
            scrollPane.getRowHeader().setBackground(newValue);
            scrollPane.getViewport().setBackground(newValue);
        }
    }

    // HEX_VIEWER
    private static class HighlighterUIResource extends DefaultHighlighter implements UIResource
    {
    }

    private static class TransferHandlerUIResource extends FileTransferHandler implements UIResource
    {
    }

    private static class CaretUIResource extends DefaultCaret implements UIResource
    {
    }

    private static class AreaDamagerUIResource extends DefaultDamager implements UIResource
    {
    }

    // AREAS
    private static class OffsetColorProviderUIResource implements IOffsetColorProvider, UIResource
    {
    }

    private static class ByteColorProviderUIResource implements IByteColorProvider, UIResource
    {
    }

    private static class ByteAreaPainterUIResource extends ByteAreaPainter implements UIResource
    {
        ByteAreaPainterUIResource(@NotNull final JHexViewer hexViewer, @NotNull final ByteArea area)
        {
            super(hexViewer, area);
        }
    }

    private static class OffsetAreaPainterUIResource extends OffsetAreaPainter implements UIResource
    {

        OffsetAreaPainterUIResource(@NotNull final JHexViewer hexViewer)
        {
            super(hexViewer, hexViewer.getOffsetArea());
        }
    }
}
