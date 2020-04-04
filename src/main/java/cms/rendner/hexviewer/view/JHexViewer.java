package cms.rendner.hexviewer.view;

import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.common.utils.IndexUtils;
import cms.rendner.hexviewer.model.data.IDataModel;
import cms.rendner.hexviewer.model.data.IDisposableModel;
import cms.rendner.hexviewer.model.rowtemplate.configuration.HexRowTemplateConfiguration;
import cms.rendner.hexviewer.model.rowtemplate.configuration.OffsetRowTemplateConfiguration;
import cms.rendner.hexviewer.model.rowtemplate.configuration.TextRowTemplateConfiguration;
import cms.rendner.hexviewer.view.components.areas.bytes.TextArea;
import cms.rendner.hexviewer.view.components.areas.bytes.*;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.components.caret.ICaret;
import cms.rendner.hexviewer.view.components.damager.IDamager;
import cms.rendner.hexviewer.view.components.highlighter.IHighlighter;
import cms.rendner.hexviewer.view.ui.DefaultHexViewerUI;
import cms.rendner.hexviewer.view.ui.HexViewerUI;
import cms.rendner.hexviewer.view.ui.container.IContextMenuFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * A basic hex viewer component which supports easy modification of the ui and internals.
 * <p/>
 * This component provides the following pluggable objects to provide a comfortable way to interact with the component:
 * <p/>
 * <b>Caret:</b>
 * <p/>
 * The caret can be used to navigate through the data model displayed by this component and to select a specific
 * range of bytes. Notification of changes to the caret position and the selection are sent to implementations of the
 * {@link cms.rendner.hexviewer.view.components.caret.ICaretListener ICaretListener} interface that have been registered
 * with the caret component. A listener may should also listen for {@link java.beans.PropertyChangeEvent} sent by the {@link JHexViewer}
 * to get notified when another caret is being installed. The UI will install a default caret unless a customized caret has been set.
 * The default caret tries to make itself visible which may lead to scrolling inside the component.
 * <p/>
 * <b>Highlighter:</b>
 * <p/>
 * The highlighter can be used to define several ranges of bytes which should be highlighted in a
 * different color. Each highlight can define it's own color. The UI will install a default highlighter unless a customized
 * highlighter has been set.
 * <p/>
 * <b>Damager:</b>
 * <p/>
 * Provides an easy way to mark parts of the byte-areas as dirty to trigger an repaint of these parts. This would usually
 * the case if content has changed and should be repainted to present the actual state. The damager is used internal to
 * request repaints after highlights, selection, position of the caret or the focuses area has changed. Therefore this
 * property shouldn't be <code>null</code> because this will result in an unexpected behaviour.The UI will install a default
 * damager unless a customized damager has been set.
 *
 * @author rendner
 */
public class JHexViewer extends JComponent
{
    /**
     * Constant used to determine when the<code>caretFocusedArea</code> property has changed.
     */
    @NotNull
    public static final String PROPERTY_CARET_FOCUSED_AREA = "caretFocusedArea";

    /**
     * Constant used to determine when the <code>dataModel</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_DATA_MODEL = "dataModel";

    /**
     * Constant used to determine when the <code>caret</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_CARET = "caret";

    /**
     * Constant used to determine when the <code>damager</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_DAMAGER = "damager";

    /**
     * Constant used to determine when the <code>highlighter</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_HIGHLIGHTER = "highlighter";

    /**
     * Constant used to determine when the<code>rowContentFont</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_ROW_CONTENT_FONT = "rowContentFont";

    /**
     * Constant used to determine when the<code>offsetRowTemplateConfiguration</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_OFFSET_ROW_TEMPLATE_CONFIGURATION = "offsetRowTemplateConfiguration";

    /**
     * Constant used to determine when the<code>hexRowTemplateConfiguration</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_HEX_ROW_TEMPLATE_CONFIGURATION = "hexRowTemplateConfiguration";

    /**
     * Constant used to determine when the<code>textRowTemplateConfiguration</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_TEXT_ROW_TEMPLATE_CONFIGURATION = "textRowTemplateConfiguration";

    /**
     * Constant used to determine when the <code>preferredVisibleRowCount</code> property has changed.
     */
    @NotNull
    public static final String PROPERTY_PREFERRED_VISIBLE_ROW_COUNT = "preferredVisibleRowCount";

    /**
     * Constant used to determine when the <code>bytesPerRow</code> property has changed.
     */
    @NotNull
    public static final String PROPERTY_BYTES_PER_ROW = "bytesPerRow";

    /**
     * The caret to navigate through the data model displayed by this component.
     */
    @Nullable
    protected ICaret caret;

    /**
     * Provides an easy way for marking regions of the byte-areas for repaint.
     */
    @Nullable
    protected IDamager damager;

    /**
     * The object responsible for managing highlights.
     */
    @Nullable
    private IHighlighter highlighter;

    /**
     * The component which displays the offset addresses of the current visible rows.
     */
    @NotNull
    private final OffsetArea offsetArea;
    /**
     * Displays the data model as hex values.
     */
    @NotNull
    private final HexArea hexArea;
    /**
     * Displays the data as text.
     */
    @NotNull
    private final TextArea textArea;
    /**
     * The byte area which currently has the caret focus.
     */
    @NotNull
    private ByteArea caretFocusedArea;
    /**
     * To configure the layout of the rows displayed in the offset-area.
     */
    @Nullable
    private OffsetRowTemplateConfiguration offsetRowTemplateConfiguration;
    /**
     * To configure the layout of the rows displayed in the hex-area.
     */
    @Nullable
    private HexRowTemplateConfiguration hexRowTemplateConfiguration;
    /**
     * To configure the layout of the rows displayed in the text-area.
     */
    @Nullable
    private TextRowTemplateConfiguration textRowTemplateConfiguration;
    /**
     * The font used to render the data model in the areas.
     */
    @Nullable
    private Font rowContentFont;
    /**
     * The number of preferred visible rows displayed in the area.
     */
    private int preferredVisibleRowCount = 8;

    /**
     * The number of bytes displayed per row, &gt;= 1.
     */
    private int bytesPerRow = 16;
    /**
     * Factory which is used to create a context menu.
     */
    @Nullable
    private IContextMenuFactory contextMenuFactory;
    /**
     * The data provider, the content of this provider is rendered by the hex viewer.
     */
    @Nullable
    private IDataModel dataModel;
    /**
     * Indicates if the offset view should display the position of the caret.
     */
    private boolean showOffsetCaretIndicator;

    /**
     * Creates a new instance.
     */
    public JHexViewer()
    {
        super();

        offsetArea = new OffsetArea();
        hexArea = new HexArea();
        textArea = new TextArea();

        caretFocusedArea = hexArea;

        updateUI();
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

    public HexViewerUI getUI()
    {
        return (HexViewerUI) ui;
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
        return "HexViewerUI";
    }

    /**
     * @return the offset-area which displays the offset addresses of the current visible rows.
     */
    @NotNull
    public OffsetArea getOffsetArea()
    {
        return offsetArea;
    }

    /**
     * @return the hex-area which displays the data model as hex values.
     */
    @NotNull
    public HexArea getHexArea()
    {
        return hexArea;
    }

    /**
     * @return the text-area which displays the data model as text.
     */
    @NotNull
    public TextArea getTextArea()
    {
        return textArea;
    }

    /**
     * Returns the number of rows which should be displayed.
     *
     * @return number of trows &gt;= 1.
     */
    public int getPreferredVisibleRowCount()
    {
        return preferredVisibleRowCount;
    }

    /**
     * Sets the number of preferred rows which should be displayed.
     * <p/>
     * Setting a new value results in a revalidate and repaint of the component.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_PREFERRED_VISIBLE_ROW_COUNT} is fired when a new value is set.
     *
     * @param newValue number of rows, &gt;= 1.
     */
    public void setPreferredVisibleRowCount(final int newValue)
    {
        CheckUtils.checkMinValue(newValue, 1);

        if (newValue != preferredVisibleRowCount)
        {
            final int oldValue = preferredVisibleRowCount;
            preferredVisibleRowCount = newValue;
            firePropertyChange(PROPERTY_PREFERRED_VISIBLE_ROW_COUNT, preferredVisibleRowCount, oldValue);
            revalidate();
            repaint();
        }
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
     * Sets if the current position of the caret should be displayed in the offset view.
     * <p/>
     * It is up to {@link cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter IAreaPainter} of the
     * offset-area to honor this property, some may choose to ignore it.
     * <p/>
     * Setting a new value results in a repaint of the offset-area.
     *
     * @param newValue the new value.
     */
    public void setShowOffsetCaretIndicator(final boolean newValue)
    {
        if (showOffsetCaretIndicator != newValue)
        {
            showOffsetCaretIndicator = newValue;
            offsetArea.repaint();
        }
    }

    /**
     * @return the font used to render the data model inside the areas.
     */
    @NotNull
    public Optional<Font> getRowContentFont()
    {
        return Optional.ofNullable(rowContentFont);
    }

    /**
     * Sets the new font used to render the data model inside the areas.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_ROW_CONTENT_FONT} is fired when a new font is set.
     *
     * @param rowContentFont the new font.
     */
    public void setRowContentFont(@Nullable final Font rowContentFont)
    {
        if (this.rowContentFont != rowContentFont)
        {
            final Font oldValue = this.rowContentFont;
            this.rowContentFont = rowContentFont;

            offsetArea.setFont(rowContentFont);
            hexArea.setFont(rowContentFont);
            textArea.setFont(rowContentFont);

            firePropertyChange(PROPERTY_ROW_CONTENT_FONT, oldValue, this.rowContentFont);
        }
    }

    /**
     * Sets the factory to be used to create context menus.
     * Whenever a mouse-down-right inside of one of the following areas: {@link AreaId#HEX} or {@link AreaId#TEXT}
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
     * @return teh context menu factory which is used to populate a context menu when the user clicks inside a byte-area.
     */
    public Optional<IContextMenuFactory> getContextMenuFactory()
    {
        return Optional.ofNullable(contextMenuFactory);
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
     * The data provider to be used as data source. The data will be displayed as content of the component.
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
        if (dataModel != newModel)
        {
            final IDataModel oldModel = dataModel;
            dataModel = newModel;

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
        }
    }

    /**
     * Sets and installs the caret to this component. A previous set caret will automatically uninstalled before
     * installing the new one.
     * <p/>
     * By default this will be set by the UI that gets installed. This can be changed to a custom caret if desired.
     * <p/>
     * Setting a new caret results in a repaint of the component.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_CARET} is fired when a new caret is set.
     *
     * @param newCaret the new caret to install.
     */
    public void setCaret(@Nullable final ICaret newCaret)
    {
        if (caret != newCaret)
        {
            if (caret != null)
            {
                caret.uninstall(this);
            }

            final ICaret oldValue = caret;
            caret = newCaret;

            if (caret != null)
            {
                caret.install(this);
            }

            firePropertyChange(PROPERTY_CARET, oldValue, caret);
            repaint();
        }
    }

    /**
     * Returns the caret that allows text-oriented navigation over the bytes.
     *
     * @return the installed caret.
     */
    @NotNull
    public Optional<ICaret> getCaret()
    {
        return Optional.ofNullable(caret);
    }

    /**
     * Sets and installs the damager to this component. A previous set damager will automatically uninstalled before
     * installing the new one.
     * <p/>
     * By default this will be set by the UI that gets installed. This can be changed to a custom damager if desired.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_DAMAGER} is fired when a new damager is installed.
     *
     * @param newValue the new damager.
     */
    public void setDamager(@Nullable final IDamager newValue)
    {
        if (damager != newValue)
        {
            if (damager != null)
            {
                damager.uninstall(this);
            }

            final IDamager oldValue = damager;
            damager = newValue;

            if (damager != null)
            {
                damager.install(this);
            }

            firePropertyChange(PROPERTY_DAMAGER, oldValue, damager);
        }
    }

    /**
     * Returns the current installed damager, which can be used to request repaints inside the component.
     *
     * @return the current installed damager.
     */
    @NotNull
    public Optional<IDamager> getDamager()
    {
        return Optional.ofNullable(damager);
    }

    /**
     * Sets and installs the highlighter to this component. A previous set highlighter will automatically uninstalled before
     * installing the new one.
     * <p/>
     * Setting a new highlighter results in a repaint of the component.
     * <p/>
     * By default this will be set by the UI that gets installed. This can be changed to a custom highlighter if desired.
     * The highlighter can be set to <code>null</code> to disable highlights.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_HIGHLIGHTER} is fired when a new highlighter is
     * installed.
     *
     * @param newHighlighter the new highlighter, can be <code>null</code> to disable highlighting.
     *                       Setting the highlighter to <code>null</code> will also disable the selection highlight.
     */
    public void setHighlighter(@Nullable final IHighlighter newHighlighter)
    {
        if (highlighter != newHighlighter)
        {
            if (highlighter != null)
            {
                highlighter.uninstall(this);
            }

            final IHighlighter oldValue = highlighter;
            highlighter = newHighlighter;

            if (highlighter != null)
            {
                highlighter.install(this);
            }

            firePropertyChange(PROPERTY_HIGHLIGHTER, oldValue, highlighter);
            repaint();
        }
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
     * Sets the number of bytes displayed in each byte-area.
     * <p/>
     * Setting a new value results in a repaint of the component. It is expected that the component may be revalidated,
     * but this depends on the installed UI.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_BYTES_PER_ROW} is fired when a new value is set.
     *
     * @param bytesPerRow the new number of bytes displayed per row.
     */
    public void setBytesPerRow(final int bytesPerRow)
    {
        if (this.bytesPerRow != bytesPerRow)
        {
            CheckUtils.checkMinValue(bytesPerRow, 1);
            final int oldValue = this.bytesPerRow;
            this.bytesPerRow = bytesPerRow;
            firePropertyChange(PROPERTY_BYTES_PER_ROW, oldValue, this.bytesPerRow);
            repaint();
        }
    }

    /**
     * @return the configuration for the row template of the text-area.
     */
    @NotNull
    public Optional<TextRowTemplateConfiguration> getTextRowTemplateConfiguration()
    {
        return Optional.ofNullable(textRowTemplateConfiguration);
    }

    /**
     * Sets the new configuration for the row template used by the text-area.
     * <p/>
     * By default this will be set by the UI that gets installed. This can be changed to a custom configuration if desired.
     * <p/>
     * Setting a new configuration may results in a revalidate and repaint of the component, but this depends on the installed UI.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_TEXT_ROW_TEMPLATE_CONFIGURATION} is fired when a new configuration is set.
     *
     * @param textRowTemplateConfiguration the new configuration for the row template of the text-area
     */
    public void setTextRowTemplateConfiguration(@Nullable final TextRowTemplateConfiguration textRowTemplateConfiguration)
    {
        if (this.textRowTemplateConfiguration != textRowTemplateConfiguration)
        {
            final TextRowTemplateConfiguration oldValue = this.textRowTemplateConfiguration;
            this.textRowTemplateConfiguration = textRowTemplateConfiguration;
            firePropertyChange(PROPERTY_TEXT_ROW_TEMPLATE_CONFIGURATION, oldValue, this.textRowTemplateConfiguration);
        }
    }

    /**
     * @return the configuration for the row template of the hex-area.
     */
    @NotNull
    public Optional<HexRowTemplateConfiguration> getHexRowTemplateConfiguration()
    {
        return Optional.ofNullable(hexRowTemplateConfiguration);
    }

    /**
     * Sets the new configuration for the row template used by the hex-area.
     * <p/>
     * By default this will be set by the UI that gets installed. This can be changed to a custom configuration if desired.
     * <p/>
     * Setting a new configuration may results in a revalidate and repaint of the component, but this depends on the installed UI.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_HEX_ROW_TEMPLATE_CONFIGURATION} is fired when a new configuration is set.
     *
     * @param hexRowTemplateConfiguration the new configuration for the row template of the hex-area
     */
    public void setHexRowTemplateConfiguration(@Nullable final HexRowTemplateConfiguration hexRowTemplateConfiguration)
    {
        if (this.hexRowTemplateConfiguration != hexRowTemplateConfiguration)
        {
            final HexRowTemplateConfiguration oldValue = this.hexRowTemplateConfiguration;
            this.hexRowTemplateConfiguration = hexRowTemplateConfiguration;
            firePropertyChange(PROPERTY_HEX_ROW_TEMPLATE_CONFIGURATION, oldValue, this.hexRowTemplateConfiguration);
        }
    }

    /**
     * @return the configuration for the row template of the offset-area.
     */
    @NotNull
    public Optional<OffsetRowTemplateConfiguration> getOffsetRowTemplateConfiguration()
    {
        return Optional.ofNullable(offsetRowTemplateConfiguration);
    }

    /**
     * Sets the new configuration for the row template used by the offset-area.
     * <p/>
     * By default this will be set by the UI that gets installed. This can be changed to a custom configuration if desired.
     * <p/>
     * Setting a new configuration may results in a revalidate and repaint of the component, but this depends on the installed UI.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_OFFSET_ROW_TEMPLATE_CONFIGURATION} is fired when a new configuration is set.
     *
     * @param offsetRowTemplateConfiguration the new configuration for the row template of the offset-area
     */
    public void setOffsetRowTemplateConfiguration(@Nullable final OffsetRowTemplateConfiguration offsetRowTemplateConfiguration)
    {
        if (this.offsetRowTemplateConfiguration != offsetRowTemplateConfiguration)
        {
            final OffsetRowTemplateConfiguration oldValue = this.offsetRowTemplateConfiguration;
            this.offsetRowTemplateConfiguration = offsetRowTemplateConfiguration;
            firePropertyChange(PROPERTY_OFFSET_ROW_TEMPLATE_CONFIGURATION, oldValue, this.offsetRowTemplateConfiguration);
        }
    }

    /**
     * Sets the caret-focus to a byte-area.
     * <p/>
     * The byte-area which has the caret focus is used to when the caret tries to make itself visible (which may lead to scrolling inside the component).
     * <p/>
     * Setting a new value results in repaint of all byte-areas, the previous one which had the caret-focus and the other one which
     * received the caret-focus.
     * <p/>
     * A PropertyChange event {@link JHexViewer#PROPERTY_CARET_FOCUSED_AREA} is fired when a new byte-area is focused.
     *
     * @param area the area to focus.
     */
    public void setCaretFocusedArea(@NotNull final ByteArea area)
    {
        if (area != caretFocusedArea)
        {
            final ByteArea oldValue = caretFocusedArea;
            caretFocusedArea = area;
            firePropertyChange(PROPERTY_CARET_FOCUSED_AREA, oldValue, caretFocusedArea);
            oldValue.repaint();
            caretFocusedArea.repaint();
        }
    }

    /**
     * @return the byte-area which has the caret-focus.
     */
    @NotNull
    public ByteArea getCaretFocusedArea()
    {
        return caretFocusedArea;
    }

    /**
     * Toggles the caret focus between the two byte-areas.
     */
    public void toggleCaretFocusedArea()
    {
        setCaretFocusedArea(caretFocusedArea == hexArea ? textArea : hexArea);
    }

    /**
     * @return <code>true</code> if one or more bytes are selected.
     */
    public boolean hasSelection()
    {
        return caret != null && caret.hasSelection();
    }

    /**
     * Returns the height of one row.
     *
     * @return the current height of a single row in this component, &gt;= 1.
     */
    public int rowHeight()
    {
        return hexArea.getRowHeight();
    }

    /**
     * Returns the number of bytes displayed per row.
     *
     * @return number of bytes, &gt;= 1.
     */
    public int getBytesPerRow()
    {
        return bytesPerRow;
    }

    /**
     * Returns the last possible byte index.
     * If no <code>dataModel</code> is set <code>0</code> will be returned.
     *
     * @return the index of the last byte.
     */
    public long getLastPossibleByteIndex()
    {
        return dataModel == null ? 0 : Math.max(0, dataModel.size() - 1);
    }

    /**
     * Returns the last possible index of the caret.
     * The default value will be <code>0</code>.
     *
     * @return the index of the last possible caret position, &gt;= 0.
     */
    public long getLastPossibleCaretIndex()
    {
        return dataModel == null ? 0 : dataModel.size();
    }

    /**
     * Returns the index (zero based) of the row to which a byteIndex belongs.
     * <p/>
     * This method doesn't check if the calculated index is within the bounds of the rows displayed by the areas.
     *
     * @param byteIndex the byte index to convert.
     * @return the index of the row, or <code>-1</code> if <code>rowIndex</code> is negative.
     */
    public int byteIndexToRowIndex(final long byteIndex)
    {
        return IndexUtils.byteIndexToRowIndex(byteIndex, getBytesPerRow());
    }

    /**
     * Returns the byte index (zero based) for a row index.
     * <p/>
     * This method doesn't check if the calculated index is within the bounds of the installed data model.
     *
     * @param rowIndex the row index to convert.
     * @return the index of the first byte of the row, or <code>-1</code> if <code>rowIndex</code> is negative.
     */
    public long rowIndexToByteIndex(final int rowIndex)
    {
        return IndexUtils.rowIndexToByteIndex(rowIndex, getBytesPerRow());
    }

    /**
     * Returns the index (zero based) in the row to which a byte belongs.
     *
     * @param byteIndex the byte index to convert.
     * @return the index inside a row, or <code>-1</code> if <code>rowIndex</code> is negative.
     */
    public int byteIndexToIndexInRow(final long byteIndex)
    {
        return IndexUtils.byteIndexToIndexInRow(byteIndex, getBytesPerRow());
    }
}