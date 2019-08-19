package cms.rendner.hexviewer.core;

import cms.rendner.hexviewer.core.formatter.IValueFormatter;
import cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter;
import cms.rendner.hexviewer.core.formatter.offset.OffsetFormatter;
import cms.rendner.hexviewer.core.model.data.DefaultDataModel;
import cms.rendner.hexviewer.core.model.data.IDataModel;
import cms.rendner.hexviewer.core.model.data.IDisposableModel;
import cms.rendner.hexviewer.core.model.row.template.IByteRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IOffsetRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.model.row.template.configuration.DefaultRowTemplateConfiguration;
import cms.rendner.hexviewer.core.model.row.template.configuration.IRowTemplateConfiguration;
import cms.rendner.hexviewer.core.uidelegate.DefaultHexViewerUI;
import cms.rendner.hexviewer.core.uidelegate.damager.IDamager;
import cms.rendner.hexviewer.core.uidelegate.row.template.factory.IRowTemplateFactory;
import cms.rendner.hexviewer.core.uidelegate.rows.IPaintDelegate;
import cms.rendner.hexviewer.core.view.IContextMenuFactory;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.areas.OffsetRowsView;
import cms.rendner.hexviewer.core.view.areas.RowBasedView;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.color.IRowColorProvider;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import cms.rendner.hexviewer.support.data.formatter.AsciiByteFormatter;
import cms.rendner.hexviewer.support.data.formatter.HexByteFormatter;
import cms.rendner.hexviewer.swing.scrollable.IScrollableDelegate;
import cms.rendner.hexviewer.swing.scrollable.ScrollableContainer;
import cms.rendner.hexviewer.swing.scrollable.ScrollableRowsContainer;
import cms.rendner.hexviewer.swing.separator.JSeparatedView;
import cms.rendner.hexviewer.swing.separator.JSeparatedViewport;
import cms.rendner.hexviewer.swing.separator.Separator;
import cms.rendner.hexviewer.swing.separator.VSeparatorPlaceholder;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.FallbackValue;
import cms.rendner.hexviewer.utils.IndexUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * A very basic hex viewer component which supports easy modification of the ui and internals.
 *
 * @author rendner
 */
public class JHexViewer extends JComponent
{
    /**
     * Constant used to determine when the <code>dataModel</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_DATA_MODEL = "dataModel";
    /**
     * Constant used to determine when the <code>caretModel</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_CARET = "caretModel";
    /**
     * Constant used to determine when the <code>areaFocus</code> property has changed.
     */
    @NotNull
    public static final String PROPERTY_FOCUSED_AREA = "areaFocus";
    /**
     * Constant used to determine when the <code>highlighter</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    public static final String PROPERTY_HIGHLIGHTER = "highlighter";
    /**
     * Constant used to determine when the <code>damager</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_DAMAGER = "damager";
    /**
     * Constant used to determine when the row template configuration property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_ROW_TEMPLATE_CONFIGURATION = "rowTemplateConfiguration";

    /**
     * @see #getUIClassID
     */
    @NotNull
    private static final String uiClassID = "HexViewerUI";

    /**
     * Stores the value formatter to format the text content displayed by the areas.
     * Even if no one was set directly by the user, a default formatter is available which was set by the
     * hexViewer during the initialisation phase.
     */
    @NotNull
    protected final Map<AreaId, FallbackValue<IValueFormatter>> valueFormatterMap = new EnumMap<>(AreaId.class);

    /**
     * Internal callback handler to hide the public callback methods.
     */
    protected InternalHandler internalHandler;

    /**
     * The configuration from which the <code>IRowTemplates</code> are build.
     */
    @Nullable
    protected IRowTemplateConfiguration rowTemplateConfiguration;

    /**
     * The factory which builds <code>IRowTemplates</code>.
     */
    @Nullable
    protected IRowTemplateFactory rowTemplateFactory;

    /**
     * Scrollable container which contains the byte-areas ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     */
    private ScrollableRowsContainer byteRowsViewContainer;

    /**
     * The component which displays the content of the offset area.
     */
    private OffsetRowsView offsetRowsView;

    /**
     * The component which displays the content of the hex area.
     */
    private ByteRowsView hexRowsView;

    /**
     * The component which displays the content of the ascii area.
     */
    private ByteRowsView asciiRowsView;

    /**
     * Factory which is used to create a context menu.
     */
    @Nullable
    private IContextMenuFactory contextMenuFactory;

    /**
     * The caret used to display the insert position and navigate throughout the <code>dataModel</code>.
     */
    @Nullable
    private ICaret caret;

    /**
     * The damager used to request repaints of specific parts inside the hex viewer without knowing to much details.
     */
    @Nullable
    private IDamager damager;

    /**
     * The data provider, the content of this provider is rendered by the hex viewer.
     */
    @Nullable
    private IDataModel dataModel;

    /**
     * The object responsible for managing highlights.
     */
    @Nullable
    private IHighlighter highlighter;

    /**
     * Indicates if the offset view ({@link AreaId#OFFSET}) should display the position of the caret.
     */
    private boolean showOffsetCaretIndicator;

    /**
     * The currently focused area, can only by a byte-area ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     */
    @NotNull
    private AreaId focusedArea = AreaId.HEX;

    /**
     * The scroll pane which arranges the internal sub views.
     */
    private JScrollPane scrollPane;

    /**
     * Allows to access the internal api of the row based views.
     */
    @NotNull
    private final Object internalApiAccessToken = new Object();

    /**
     * Creates a new instance.
     */
    public JHexViewer()
    {
        super();
        init();
        updateUI();
    }

    /**
     * Returns if the position of the caret should be displayed in the offset view or not.
     *
     * @return <code>true</code> if the position of the caret is also displayed by the offset view.
     */
    public boolean isShowOffsetCaretIndicator()
    {
        return showOffsetCaretIndicator;
    }

    /**
     * Sets if the position of the caret should be displayed in the offset view ({@link AreaId#OFFSET}).
     * <p/>
     * It is up to the installed {@link IPaintDelegate} to honor this property, some may
     * choose to ignore it.
     *
     * @param newValue the new value.
     * @see IPaintDelegate
     * @see #setPaintDelegate(IPaintDelegate)
     * @see #getPaintDelegate()
     */
    public void setShowOffsetCaretIndicator(final boolean newValue)
    {
        if (showOffsetCaretIndicator != newValue)
        {
            showOffsetCaretIndicator = newValue;
            getDamager().ifPresent(damager -> damager.damageArea(AreaId.OFFSET));
        }
    }

    /**
     * Returns the delegate which paints all three areas ({@link AreaId#OFFSET}, {@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @return the current paint delegate.
     */
    @NotNull
    public Optional<IPaintDelegate> getPaintDelegate()
    {
        return offsetRowsView.getPaintDelegate();
    }

    /**
     * Sets the delegate which is responsible to paint all three areas ({@link AreaId#OFFSET}, {@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @param newDelegate the new delegate.
     */
    public void setPaintDelegate(@Nullable final IPaintDelegate newDelegate)
    {
        final IPaintDelegate oldDelegate = getPaintDelegate().orElse(null);

        if(oldDelegate != newDelegate)
        {
            if(oldDelegate != null)
            {
                oldDelegate.uninstall(this);
            }

            if (newDelegate != null)
            {
                newDelegate.install(this);
            }

            offsetRowsView.getInternalApi(internalApiAccessToken).setPaintDelegate(newDelegate);
            hexRowsView.getInternalApi(internalApiAccessToken).setPaintDelegate(newDelegate);
            asciiRowsView.getInternalApi(internalApiAccessToken).setPaintDelegate(newDelegate);
        }
    }

    /**
     * Sets the new formatter to format the offset displayed by the offset area.
     * <p/>
     * Changing the formatter results in a recreation of the internal row template used by the offset area
     * which depends on the used formatter. And a complete repainting of the offset area.
     *
     * @param newValue the new formatter, passing <code>null</code> will activate the default formatter.
     */
    public void setOffsetFormatter(@Nullable final IOffsetValueFormatter newValue)
    {
        final FallbackValue<IValueFormatter> formatter = valueFormatterMap.get(AreaId.OFFSET);
        final IValueFormatter oldValue = formatter.getPreferredValue();

        if (newValue != oldValue)
        {
            formatter.setPreferredValue(newValue);
            recreateOffsetRowTemplate();
            getDamager().ifPresent(damager -> damager.damageArea(AreaId.OFFSET));
        }
    }

    /**
     * Sets the new formatter to format the bytes displayed by the hex area.
     * <p/>
     * Changing the formatter results in a complete repainting of the hex area.
     *
     * @param newValue the new formatter, passing <code>null</code> will activate the default formatter.
     */
    public void setHexFormatter(@Nullable final IValueFormatter newValue)
    {
        final FallbackValue<IValueFormatter> formatter = valueFormatterMap.get(AreaId.HEX);
        final IValueFormatter oldValue = formatter.getPreferredValue();

        if (newValue != oldValue)
        {
            formatter.setPreferredValue(newValue);
            getDamager().ifPresent(damager -> damager.damageArea(AreaId.HEX));
        }
    }

    /**
     * Sets the new formatter to format the bytes displayed by the hex ascii.
     * <p/>
     * Changing the formatter results in a complete repainting of the ascii area.
     *
     * @param newValue the new formatter, passing <code>null</code> will activate the default formatter.
     */
    public void setAsciiFormatter(@Nullable final IValueFormatter newValue)
    {
        final FallbackValue<IValueFormatter> formatter = valueFormatterMap.get(AreaId.ASCII);
        final IValueFormatter oldValue = formatter.getPreferredValue();

        if (newValue != oldValue)
        {
            formatter.setPreferredValue(newValue);
            getDamager().ifPresent(damager -> damager.damageArea(AreaId.ASCII));
        }
    }

    /**
     * @return the current used formatter to format the displayed values of the offset area.
     * Can't be <code>null</code>, even if no one was set in this case a default formatter will be returned.
     */
    @NotNull
    public IOffsetValueFormatter getOffsetValueFormatter()
    {
        return (IOffsetValueFormatter) valueFormatterMap.get(AreaId.OFFSET).getValue();
    }

    /**
     * @return the current used formatter to format the displayed bytes of the hex area.
     * Can't be <code>null</code>, even if no one was set in this case a default formatter will be returned.
     */
    @NotNull
    public IValueFormatter getHexValueFormatter()
    {
        return valueFormatterMap.get(AreaId.HEX).getValue();
    }

    /**
     * @return the current used formatter to format the displayed bytes of the ascii area.
     * Can't be <code>null</code>, even if no one was set in this case a default formatter will be returned.
     */
    @NotNull
    public IValueFormatter getAsciiValueFormatter()
    {
        return valueFormatterMap.get(AreaId.ASCII).getValue();
    }

    /**
     * The container which contains the byte-areas ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     * This component can be used to determine the scroll increment to scroll the container.
     * <p/>
     * To scroll to a specific row or byte use the {@link JComponent#scrollRectToVisible(Rectangle)} of the
     * <code>hexRowsView</code> or <code>asciiRowsView</code>.
     *
     * @return the container of the byte-areas.
     * @see #getHexRowsView()
     * @see #getAsciiRowsView()
     * @see JComponent#scrollRectToVisible(Rectangle)
     */
    @NotNull
    public ScrollableContainer getScrollableByteRowsContainer()
    {
        return byteRowsViewContainer;
    }

    /**
     * Returns the number of rows which should be displayed.
     *
     * @return number of rows &gt;= 0.
     */
    public int getPreferredVisibleRowCount()
    {
        return byteRowsViewContainer.getPreferredVisibleRowCount();
    }

    /**
     * Sets the number of preferred rows which should be displayed.
     *
     * @param newValue number of rows. Value should be &gt;= 0.
     */
    public void setPreferredVisibleRowCount(final int newValue)
    {
        if (newValue != getPreferredVisibleRowCount())
        {
            byteRowsViewContainer.setPreferredVisibleRowCount(newValue);
        }
    }

    /**
     * Returns the currently focused area, can only be a byte-area ({@link AreaId#HEX} or {@link AreaId#ASCII}).
     *
     * @return the focused area.
     */
    @NotNull
    public AreaId getFocusedArea()
    {
        return focusedArea;
    }

    /**
     * Sets the focused area, can only by a byte-area ({@link AreaId#HEX} and {@link AreaId#ASCII}).
     * <p/>
     * It is up to the installed {@link IPaintDelegate} to honor this property, some may
     * choose to ignore it.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_FOCUSED_AREA} is fired when focused area has changed.
     *
     * @param id <code>AreaId.HEX</code> or <code>AreaId.ASCII</code>
     * @see IPaintDelegate
     * @see #setPaintDelegate(IPaintDelegate)
     * @see #getPaintDelegate()
     */
    public void setFocusedArea(@NotNull final AreaId id)
    {
        if (!isFocusableArea(id))
        {
            throw new IllegalArgumentException("The area '" + id + "' isn't a focusable area.");
        }

        if (!id.equals(focusedArea))
        {
            final AreaId oldFocusedAreaId = focusedArea;
            focusedArea = id;
            updateByteAreaFocus();
            firePropertyChange(PROPERTY_FOCUSED_AREA, oldFocusedAreaId, focusedArea);
        }
    }

    @Override
    public void setBackground(@Nullable final Color newBackground)
    {
        super.setBackground(newBackground);
        scrollPane.setBackground(newBackground);
    }

    /**
     * Resets the UI property to a value from the current look-and-feel.
     * If no UI was installed to the {@link UIManager}, the {@link DefaultHexViewerUI}
     * will be used.
     *
     * @see #setUI
     * @see UIManager#getLookAndFeel
     * @see UIManager#getUI
     */
    @Override
    public void updateUI()
    {
        if (UIManager.get(getUIClassID()) == null)
        {
            // if no ui is installed, install the default one
            UIManager.put(getUIClassID(), DefaultHexViewerUI.class.getName());
        }

        setUI(UIManager.getUI(this));
    }

    /**
     * Returns the suffix used to construct the name of the L&amp;F class used to
     * render this component.
     *
     * @return the string "HexViewerUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @NotNull
    @Override
    public String getUIClassID()
    {
        return uiClassID;
    }

    /**
     * Sets the separator to be used to divide the {@link AreaId#OFFSET} and {@link AreaId#HEX} areas.
     *
     * @param separator the new separator, this value can be <code>null</code> if no separator should be displayed.
     */
    public void setOffsetSeparator(@Nullable final Separator separator)
    {
        final Component view = scrollPane.getRowHeader().getView();
        replaceSeparator(view, separator);
    }

    /**
     * Sets the separator to be used to divide the {@link AreaId#HEX} and {@link AreaId#ASCII} areas.
     *
     * @param separator the new separator, this value can be <code>null</code> if no separator should be displayed.
     */
    public void setByteViewsSeparator(@Nullable final Separator separator)
    {
        final Component view = scrollPane.getViewport().getView();
        replaceSeparator(view, separator);
    }

    /**
     * Returns the component which displays the content of the {@link AreaId#OFFSET}.
     *
     * @return the component which displays the offset part.
     */
    @NotNull
    public OffsetRowsView getOffsetRowsView()
    {
        return offsetRowsView;
    }

    /**
     * Returns the component which displays the content of the {@link AreaId#HEX}.
     *
     * @return the component which displays the hex part.
     */
    @NotNull
    public ByteRowsView getHexRowsView()
    {
        return hexRowsView;
    }

    /**
     * Returns the component which displays the content of the {@link AreaId#ASCII}.
     *
     * @return the component which displays the ascii part.
     */
    @NotNull
    public ByteRowsView getAsciiRowsView()
    {
        return asciiRowsView;
    }

    /**
     * Returns the component which displays the requested area.
     *
     * @param id the id of the requested byte component.
     * @return the component which displays the content of the requested area.
     * @see #isByteArea(AreaId)
     */
    @NotNull
    public ByteRowsView getByteRowsView(@NotNull final AreaId id)
    {
        if (!isByteArea(id))
        {
            throw new IllegalArgumentException("The area '" + id + "' isn't a byte area.");
        }
        return AreaId.HEX.equals(id) ? hexRowsView : asciiRowsView;
    }

    /**
     * Returns the component which displays the requested area.
     *
     * @param id the id of the requested component.
     * @return the component which displays the content of the requested area.
     */
    @NotNull
    public RowBasedView getRowsView(@NotNull final AreaId id)
    {
        if (AreaId.OFFSET.equals(id))
        {
            return offsetRowsView;
        }

        return getByteRowsView(id);
    }

    /**
     * Returns the scroll delegate to be used to scroll the internal <code>byteRowsViewContainer</code>.
     *
     * @return the delegate or <code>null</code> if no delegate was set.
     * @see #getScrollableByteRowsContainer()
     */
    @NotNull
    public Optional<IScrollableDelegate> getScrollableDelegate()
    {
        return byteRowsViewContainer.getScrollableDelegate();
    }

    /**
     * Sets the delegate to be used to scroll the content of the internal <code>byteRowsViewContainer</code>.
     * Setting a new delegate doesn't reset or change the current scroll position.
     * <p/>
     * The delegate should provide at least a vertical row based scrolling.
     *
     * @param newDelegate the new delegate, can be <code>null</code>.
     */
    public void setScrollableDelegate(@Nullable final IScrollableDelegate newDelegate)
    {
        final IScrollableDelegate oldDelegate = byteRowsViewContainer.getScrollableDelegate().orElse(null);

        if (oldDelegate != newDelegate)
        {
            if (oldDelegate != null)
            {
                oldDelegate.uninstall(this);
            }

            byteRowsViewContainer.setScrollableDelegate(newDelegate);

            if (newDelegate != null)
            {
                newDelegate.install(this);
            }
        }
    }

    /**
     * Sets the background color of the container which displays the content of the offset area.
     * If the new value is <code>null</code>, the component will be fully transparent.
     *
     * @param newBackground the new color.
     */
    public void setOffsetViewBackground(@Nullable final Color newBackground)
    {
        setComponentBackground(scrollPane.getRowHeader(), newBackground);
    }

    /**
     * Sets the background color of the container which displays the byte area.
     * If the new value is <code>null</code>, the component will be fully transparent.
     *
     * @param newBackground the new color.
     */
    public void setByteViewBackground(@Nullable final Color newBackground)
    {
        setComponentBackground(scrollPane.getViewport(), newBackground);
    }

    /**
     * Returns the factory to be used to create row templates for the areas.
     *
     * @return the current installed factory.
     */
    @NotNull
    public Optional<IRowTemplateFactory> getRowTemplateFactory()
    {
        return Optional.ofNullable(rowTemplateFactory);
    }

    /**
     * Sets the factory to be used to create row templates for the areas.
     * <p/>
     * This property is usually set by a look and feel, but can be overwritten with an own implementation.
     * Setting a new factory recreates the internal row templates and result in a repaint of the areas to reflect the new
     * row layout.
     *
     * @param newValue the new factory.
     */
    public void setRowTemplateFactory(@Nullable final IRowTemplateFactory newValue)
    {
        if (rowTemplateFactory != newValue)
        {
            rowTemplateFactory = newValue;
            recreateRowTemplates();
            recalculateRowCount();
        }
    }

    /**
     * Returns the current installed damager, which is used to request repaints inside the component.
     *
     * @return the current installed damager.
     */
    @NotNull
    public Optional<IDamager> getDamager()
    {
        return Optional.ofNullable(damager);
    }

    /**
     * Sets the new damager. This damager can be used from everywhere to force repaints inside the component.
     * <p/>
     * The damager is used internal to request repaints after highlights, selection, position of the caret or the focus
     * has changed. Therefore this property shouldn't be <code>null</code> because this will result in an unexpected behaviour.
     * <p/>
     * This property is usually set by a look and feel, but can be overwritten with an own implementation.
     * Setting a new damager results in a complete repaint of the component.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_DAMAGER} is fired when a new damager is installed.
     *
     * @param newValue the new damager.
     */
    public void setDamager(@Nullable final IDamager newValue)
    {
        final IDamager oldValue = damager;

        if (oldValue != newValue)
        {
            if (oldValue != null)
            {
                oldValue.uninstall(this);
            }

            if (newValue != null)
            {
                newValue.install(this);
            }

            damager = newValue;
            firePropertyChange(PROPERTY_DAMAGER, oldValue, damager);
            repaint();
        }
    }

    /**
     * Forwards a color provider to the current installed paint delegate. If no paint delegate is installed this method
     * does nothing. The paint delegate is usually installed by the UI delegate which provides the look and feel of the
     * hexViewer.
     * <p/>
     * The provider is used during the repaint of a row to determine which color should be used to render the foreground
     * or background. The provider can be set to <code>null</code> to use the default colors.
     *
     * @param id          the id of the area for which the provider should be used.
     * @param newProvider the new provider, can be <code>null</code>.
     */
    public void setRowColorProvider(@NotNull final AreaId id, @Nullable final IRowColorProvider newProvider)
    {
        getPaintDelegate().ifPresent(delegate -> delegate.setRowColorProvider(id, newProvider));
    }

    /**
     * Returns the current installed highlighter.
     *
     * @return the installed highlighter or <code>null</code> if no delegate was set.
     */
    @NotNull
    public Optional<IHighlighter> getHighlighter()
    {
        return Optional.ofNullable(highlighter);
    }

    /**
     * Sets the highlighter to be used to render selection and highlights.
     * By default this will be set by the UI that gets installed. This can be changed to
     * a custom highlighter if desired. The highlighter can be set to
     * <code>null</code> to disable highlights and the selection.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_HIGHLIGHTER} is fired when a new highlighter is installed.
     *
     * @param newHighlighter the new highlighter, can be <code>null</code>
     */
    public void setHighlighter(@Nullable final IHighlighter newHighlighter)
    {
        final IHighlighter oldHighlighter = highlighter;
        if (oldHighlighter != null)
        {
            oldHighlighter.uninstall(this);
        }

        if (newHighlighter != null)
        {
            newHighlighter.install(this);
        }

        highlighter = newHighlighter;
        firePropertyChange(PROPERTY_HIGHLIGHTER, oldHighlighter, newHighlighter);
        getDamager().ifPresent(damager -> {
            damager.damageArea(AreaId.HEX);
            damager.damageArea(AreaId.ASCII);
        });
    }

    /**
     * Checks if an area is focusable. Only the {@link AreaId#HEX} and {@link AreaId#ASCII} is focusable.
     *
     * @param id the id of the area which should be checked.
     * @return <code>true</code> if focusable otherwise <code>false</code>
     */
    public boolean isFocusableArea(@NotNull final AreaId id)
    {
        return isByteArea(id);
    }

    /**
     * Checks if an area is a byte area. Only the {@link AreaId#HEX} and {@link AreaId#ASCII} is byte areas.
     *
     * @param id the id of the area which should be checked.
     * @return <code>true</code> if byte area otherwise <code>false</code>
     */
    public boolean isByteArea(@NotNull final AreaId id)
    {
        CheckUtils.checkNotNull(id);
        return AreaId.HEX.equals(id) || AreaId.ASCII.equals(id);
    }

    /**
     * Sets the factory to be used to create context menus.
     * Whenever a mouse-down-right inside of one of the three areas ({@link AreaId#OFFSET}, {@link AreaId#HEX} or {@link AreaId#ASCII}
     * is detected this factory will be used to create a context menu. If the factory returns <code>null</code> no context
     * menu will be displayed.
     * <p/>
     * This property can be modified to support own context menus.
     *
     * @param factory the factory to be used, can be <code>null</code>.
     */
    public void setContextMenuFactory(@Nullable final IContextMenuFactory factory)
    {
        this.contextMenuFactory = factory;
    }

    /**
     * The currently used data provider.
     *
     * @return the data provider.
     */
    @NotNull
    public Optional<IDataModel> getDataModel()
    {
        return Optional.ofNullable(dataModel);
    }

    /**
     * The data provider to be used as data source. the data will be displayed as content of the component.
     * Setting this property results into a reset of the caret position to <code>0</code>, all highlights will be removed
     * and the current scroll position will be set to <code>0</code>.
     * <p/>
     * If the previous installed provider implements the {@link IDisposableModel} interface and {@link IDisposableModel#isAutoDispose()}
     * returns <code>true</code> for this provider the {@link IDisposableModel#dispose()} will be automatically called. This behaviour
     * can be suppressed if {@link IDisposableModel#isAutoDispose()} returns <code>false</code>.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_DATA_MODEL} is fired when a new model is installed.
     *
     * @param newModel the data to be displayed.
     */
    public void setDataModel(@Nullable final IDataModel newModel)
    {
        if (newModel != dataModel)
        {
            final IDataModel oldModel = dataModel;

            dataModel = newModel;

            if (caret != null)
            {
                if (caret.getDot().getIndex() != 0)
                {
                    caret.setDot(0);
                }
                else
                {
                    byteRowsViewContainer.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
                }
            }

            getHighlighter().ifPresent(IHighlighter::removeAllHighlights);

            recalculateRowCount();
            recreateOffsetRowTemplateIfNeeded();
            firePropertyChange(PROPERTY_DATA_MODEL, oldModel, dataModel);

            // dispose the old model after propertyChange was fired
            if (oldModel instanceof IDisposableModel)
            {
                final IDisposableModel disposable = (IDisposableModel) oldModel;
                if (disposable.isAutoDispose() && !disposable.disposed())
                {
                    disposable.dispose();
                }
            }

            invalidate();
            getDamager().ifPresent(IDamager::damageAllAreas);
        }
    }

    /**
     * Returns the caret that allows text-oriented navigation over
     * the view.
     *
     * @return the installed caret.
     */
    @NotNull
    public Optional<ICaret> getCaret()
    {
        return Optional.ofNullable(caret);
    }

    /**
     * Sets the caret to be used.  By default this will be set
     * by the UI that gets installed. This can be changed to a custom caret if desired.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_CARET} is fired when a new caret is installed.
     *
     * @param newCaret the caret used to select/navigate
     */
    public void setCaret(@Nullable final ICaret newCaret)
    {
        if (newCaret != caret)
        {
            final ICaret oldCaret = caret;

            if (oldCaret != null)
            {
                oldCaret.uninstall(this);
            }

            caret = newCaret;

            if (caret != null)
            {
                caret.install(this);
            }

            firePropertyChange(PROPERTY_CARET, oldCaret, newCaret);

            // there are two cases which have to be handled:
            //
            // case 1 - we had a selection
            //      - then we would have to damage the whole selected area
            //
            // case 2 - we don't had a selection
            //      - then we only have to damage the position of the caret
            //
            // to simplify this we always damage the whole area, which is more than required,
            // but this method is normally called rarely
            getDamager().ifPresent(damager -> {
                damager.damageArea(AreaId.HEX);
                damager.damageArea(AreaId.ASCII);
            });
        }
    }

    /**
     * Returns the row-template-configuration which allows to manipulate the configuration to update the internal row templates used by the areas.
     *
     * @return the configuration.
     */
    @NotNull
    public Optional<IRowTemplateConfiguration> getRowTemplateConfiguration()
    {
        return Optional.ofNullable(rowTemplateConfiguration);
    }

    /**
     * Sets the new row-template-configuration.
     * The configuration will be used to create the internal row templates for the areas ({@link AreaId#OFFSET}, {@link AreaId#HEX} and {@link AreaId#ASCII}).
     *
     * @param newConfiguration the new configuration, this value can't be null.
     */
    public void setRowTemplateConfiguration(@NotNull final IRowTemplateConfiguration newConfiguration)
    {
        if (rowTemplateConfiguration != newConfiguration)
        {
            final IRowTemplateConfiguration oldValue = rowTemplateConfiguration;
            rowTemplateConfiguration = newConfiguration;
            handleRowTemplateConfigurationChange();
            firePropertyChange(PROPERTY_ROW_TEMPLATE_CONFIGURATION, oldValue, newConfiguration);
        }
    }

    /**
     * Returns the number of bytes displayed in one row.
     * <p/>
     * This value represents the real displayed number of bytes and should be preferred over
     * {@link IRowTemplateConfiguration#bytesPerRow()} which is only a configuration which can be ignored by the
     * installed {@link IRowTemplateFactory}.
     * <p/>
     * This value changes whenever new row templates produced by the installed {@link IRowTemplateFactory} are set.
     *
     * @return the number of displayed bytes.
     * @see ByteRowsView#bytesPerRow()
     */
    public int bytesPerRow()
    {
        return hexRowsView.bytesPerRow();
    }

    /**
     * Calculates the index of the byte in the line where the byte is located.
     *
     * @param byteIndex the index of the byte.
     * @return the index in the row, or <code>-1</code> if the conversion fails.
     * @see IndexUtils#byteIndexToIndexInRow(int, int)
     */
    public int byteIndexToIndexInRow(final int byteIndex)
    {
        return IndexUtils.byteIndexToIndexInRow(byteIndex, bytesPerRow());
    }

    /**
     * Calculates the index of the row where the byte is located.
     *
     * @param byteIndex the index of the byte.
     * @return the index of the row, or <code>-1</code> if the conversion fails.
     * @see IndexUtils#byteIndexToRowIndex(int, int)
     */
    public int byteIndexToRowIndex(final int byteIndex)
    {
        return IndexUtils.byteIndexToRowIndex(byteIndex, bytesPerRow());
    }

    /**
     * Calculates the byte index of the first byte of a row.
     *
     * @param rowIndex the index of the row.
     * @return the index of the byte, or <code>-1</code> if the conversion fails.
     * @see IndexUtils#rowIndexToByteIndex(int, int)
     */
    public int rowIndexToByteIndex(final int rowIndex)
    {
        return IndexUtils.rowIndexToByteIndex(rowIndex, bytesPerRow());
    }

    /**
     * Returns the last possible byte index.
     * If no <code>dataModel</code> is set <code>0</code> will be returned.
     *
     * @return the index of the last byte.
     */
    public int lastPossibleByteIndex()
    {
        return getDataModel().map(data -> Math.max(0, data.size() - 1)).orElse(0);
    }

    /**
     * Returns the last possible index for a caret.
     * If no data configuration is set or if the <code>dataModel</code> is empty the result will be <code>0</code>
     * otherwise equals the size of the <code>dataModel</code>. Because a caret can be positioned after the last
     * available byte to insert new bytes in edit mode or to start a selection starting after the last byte
     * (from right to left).
     *
     * @return the index of the last possible caret position.
     */
    public int lastPossibleCaretIndex()
    {
        return getDataModel().map(IDataModel::size).orElse(0);
    }

    /**
     * Returns the real height of a row.
     * <p/>
     * This value changes whenever new row templates produced by the installed {@link IRowTemplateFactory} are set.
     *
     * @return the height of a row.
     * @see ByteRowsView#rowHeight()
     */
    public int rowHeight()
    {
        return hexRowsView.rowHeight();
    }

    /**
     * Initializes the component and all his sub components.
     */
    protected void init()
    {
        setLayout(new BorderLayout());
        setFocusTraversalKeysEnabled(false);
        setupValueFormatterMap(valueFormatterMap);

        internalHandler = createInternalHandler();

        createSubComponents();
        createDefaultModels();

        registerInternalHandler(internalHandler);

        updateByteAreaFocus();
        revalidate();
        repaint();
    }

    /**
     * Creates and compose the internal sub components of the hex viewer.
     */
    protected void createSubComponents()
    {
        // the views which renders the three areas
        offsetRowsView = new OffsetRowsView(internalApiAccessToken);
        hexRowsView = new ByteRowsView(AreaId.HEX, internalApiAccessToken);
        asciiRowsView = new ByteRowsView(AreaId.ASCII, internalApiAccessToken);

        // scroll pane which contains all sub containers
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        // the scrollable container which contains the hex and ascii area
        byteRowsViewContainer = new ScrollableRowsContainer();
        byteRowsViewContainer.add(hexRowsView);
        byteRowsViewContainer.add(new VSeparatorPlaceholder());
        byteRowsViewContainer.add(asciiRowsView);
        scrollPane.setViewport(new JSeparatedViewport());
        scrollPane.setViewportView(byteRowsViewContainer);

        // the container of the offset view
        final JSeparatedView rowHeaderView = new JSeparatedView();
        rowHeaderView.add(offsetRowsView);
        rowHeaderView.add(new VSeparatorPlaceholder());
        scrollPane.setRowHeader(new JSeparatedViewport());
        scrollPane.setRowHeaderView(rowHeaderView);
    }

    /**
     * Updates the focus property of the byte areas depending on the current focused area.
     */
    protected void updateByteAreaFocus()
    {
        if (AreaId.HEX.equals(focusedArea))
        {
            hexRowsView.getInternalApi(internalApiAccessToken).setFocus(true);
            asciiRowsView.getInternalApi(internalApiAccessToken).setFocus(false);
        }
        else if (AreaId.ASCII.equals(focusedArea))
        {
            hexRowsView.getInternalApi(internalApiAccessToken).setFocus(false);
            asciiRowsView.getInternalApi(internalApiAccessToken).setFocus(true);
        }
    }

    /**
     * Creates the internal handler used to register for events sent by internal sub components or observable objects.
     * <p/>
     * Overwrite this method if a custom internal handler should be used.
     *
     * @return the internal handler for the <code>{@link JHexViewer}</code>.
     */
    @NotNull
    protected InternalHandler createInternalHandler()
    {
        return new InternalHandler();
    }

    /**
     * Registers the <code>internalHandler</code> as listener to internal sub components or observable objects.
     * <p/>
     * By default the <code>internalHandler</code> is registered as <code>{@link java.awt.event.MouseListener}</code>
     * with the <code>hexRowsView</code> and <code>asciiRowsView</code> to handle mouse clicks.
     *
     * @param handler the internal handler to register as listener.
     */
    protected void registerInternalHandler(@NotNull final InternalHandler handler)
    {
        CheckUtils.checkNotNull(handler);

        hexRowsView.addMouseListener(handler);
        asciiRowsView.addMouseListener(handler);
    }

    /**
     * Creates the default models of the component.
     * By default an empty {@link DefaultDataModel} and a {@link DefaultRowTemplateConfiguration} is installed.
     * This can be changed to customs models if desired.
     */
    protected void createDefaultModels()
    {
        setRowTemplateConfiguration(DefaultRowTemplateConfiguration.newBuilder().build());
        setDataModel(new DefaultDataModel());
    }

    /**
     * This method should add for each area a FallbackValue which provides a valid fallback value provider.
     * This fallback provider will be used if no additional provider was set by the user.
     *
     * @param map the map to fill with the fallback instances.
     */
    protected void setupValueFormatterMap(@NotNull final Map<AreaId, FallbackValue<IValueFormatter>> map)
    {
        map.put(AreaId.OFFSET, new FallbackValue<>(new OffsetFormatter(4)));
        map.put(AreaId.HEX, new FallbackValue<>(new HexByteFormatter()));
        map.put(AreaId.ASCII, new FallbackValue<>(new AsciiByteFormatter()));
    }

    /**
     * (Re)calculates the number of rows in the layout.
     * This method should be called whenever the <code>dataModel</code> was changed or the internal {@link IRowTemplate rowTemplates}
     * have been changed.
     */
    protected void recalculateRowCount()
    {
        // "+1" because rowIndex is zero based
        final int rowCount = 1 + Math.max(0, byteIndexToRowIndex(lastPossibleCaretIndex()));
        offsetRowsView.getInternalApi(internalApiAccessToken).setRowCount(rowCount);
        hexRowsView.getInternalApi(internalApiAccessToken).setRowCount(rowCount);
        asciiRowsView.getInternalApi(internalApiAccessToken).setRowCount(rowCount);
    }

    /**
     * Is called whenever a right-mouse-down happens inside the hex or ascii area.
     * If an {@link IContextMenuFactory} is installed, this factory will be used to create a context menu which is
     * displayed to the user. If not, nothing will happen.
     *
     * @param rowsView           the view in which the mouse click happened.
     * @param locationInRowsView the location of the mouse click inside the <code>rowsView</code>.
     */
    protected void showContextMenu(@NotNull final ByteRowsView rowsView, @NotNull final Point locationInRowsView)
    {
        if (contextMenuFactory != null)
        {
            final int maxBiteIndex = lastPossibleByteIndex();
            final int byteIndex = rowsView
                    .hitTest(locationInRowsView.x, locationInRowsView.y)
                    .map(byteHitInfo -> Math.min(byteHitInfo.index(), maxBiteIndex))
                    .orElse(maxBiteIndex);
            final JPopupMenu menu = contextMenuFactory.create(this, rowsView.getId(), byteIndex);

            if (menu != null)
            {
                menu.show(rowsView, locationInRowsView.x, locationInRowsView.y);
            }
        }
    }

    /**
     * Is called from the <code>internalHandler</code> whenever a mouse event occurred.
     *
     * @param event the mouse event.
     */
    protected void handleMouseEvent(@NotNull final MouseEvent event)
    {
        // Note: Popup menus are triggered differently on different systems.
        // Therefore, isPopupTrigger should be checked in both mousePressed and mouseReleased for
        // proper cross-platform functionality.
        if (event.isPopupTrigger())
        {
            if (event.getSource() instanceof ByteRowsView)
            {
                showContextMenu((ByteRowsView) event.getSource(), event.getPoint());
            }
        }
    }

    /**
     * Is executed whenever the row-template-configuration was changed to recalculate and update the internal state.
     */
    protected void handleRowTemplateConfigurationChange()
    {
        recreateRowTemplates();
        recalculateRowCount();
    }

    /**
     * (Re)creates the row templates of the areas, depending on the row-template-configuration and the installed template-factory.
     * The new created row-templates are directly applied to the areas.
     */
    protected void recreateRowTemplates()
    {
        recreateOffsetRowTemplate();
        recreateByteRowTemplates();
    }

    /**
     * (Re)creates the row-template of the hex-area and ascii-area.
     * <p/>
     * The byte row templates can't be created if the <code>rowTemplateConfiguration</code> or <code>rowTemplateFactory</code>
     * is <code>null</code>, in that case the current installed templates will be removed.
     */
    protected void recreateByteRowTemplates()
    {
        IByteRowTemplate hexRowTemplate = null;
        IByteRowTemplate asciiRowTemplate = null;

        if (rowTemplateConfiguration != null && rowTemplateFactory != null)
        {
            final FontMetrics fm = getFontMetrics(rowTemplateConfiguration.font());
            hexRowTemplate = rowTemplateFactory.createHexTemplate(this, fm, rowTemplateConfiguration);
            asciiRowTemplate = rowTemplateFactory.createAsciiTemplate(this, fm, rowTemplateConfiguration);
        }

        hexRowsView.getInternalApi(internalApiAccessToken).setRowTemplate(hexRowTemplate);
        asciiRowsView.getInternalApi(internalApiAccessToken).setRowTemplate(asciiRowTemplate);
    }

    /**
     * (Re)creates the row-template of the offset-area.
     * <p/>
     * The offset row template can't be created if the <code>rowTemplateConfiguration</code> or <code>rowTemplateFactory</code>
     * is <code>null</code>, in that case the current installed template will be removed.
     */
    protected void recreateOffsetRowTemplate()
    {
        IOffsetRowTemplate offsetRowTemplate = null;

        if (rowTemplateConfiguration != null && rowTemplateFactory != null)
        {
            offsetRowTemplate = rowTemplateFactory.createOffsetTemplate(
                    this,
                    getFontMetrics(rowTemplateConfiguration.font()),
                    rowTemplateConfiguration);
        }

        offsetRowsView.getInternalApi(internalApiAccessToken).setRowTemplate(offsetRowTemplate);
    }

    /**
     * Recreates the row-template of the offset-area if required.
     */
    protected final void recreateOffsetRowTemplateIfNeeded()
    {
        getRowTemplateFactory().ifPresent(factory -> {
            if (factory.shouldOffsetRowTemplateRecreated(this))
            {
                recreateOffsetRowTemplate();
            }
        });
    }

    /**
     * Replaces the first separator in a container.
     * This method does nothing if the container isn't a {@link JSeparatedView}.
     *
     * @param container the container which contains the separator.
     * @param separator the new separator, can be <code>null</code>
     */
    protected final void replaceSeparator(@NotNull final Component container, @Nullable final Separator separator)
    {
        if (container instanceof JSeparatedView)
        {
            final JSeparatedView separatedView = (JSeparatedView) container;
            if (separatedView.getSeparatorPlaceholderCount() > 0)
            {
                separatedView.getSeparatorPlaceholder(0).setSeparator(separator);
            }
        }
    }

    /**
     * Sets the new background color for a component.
     * This method checks if the component is not <code>null</code> before setting the color.
     *
     * @param component     the component to modify.
     * @param newBackground the new background color, can be <code>null</code>.
     */
    protected final void setComponentBackground(@Nullable final JComponent component, @Nullable final Color newBackground)
    {
        if (component != null)
        {
            component.setBackground(newBackground);
        }
    }

    /**
     * Hides the public methods of the implemented listener and delegates these to protected methods.
     */
    protected class InternalHandler extends MouseAdapter
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
    }
}