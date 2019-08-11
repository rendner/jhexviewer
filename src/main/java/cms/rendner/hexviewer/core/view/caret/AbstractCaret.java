package cms.rendner.hexviewer.core.view.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.color.ICaretColorProvider;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;
import cms.rendner.hexviewer.core.view.highlight.DefaultHighlighter;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import cms.rendner.hexviewer.utils.CheckUtils;
import cms.rendner.hexviewer.utils.observer.Observable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * An abstract implementation of ICaret. It can blink at the rate specified by the BlinkRate property.
 * <p/>
 * The Highlighter bound to the associated JHexViewer is used to render the selection. Selection appearance can be
 * customized by supplying a selectionPainter.
 * <p/>
 * A customized caret appearance can be achieved by implementing the paint method. If the paint method is changed,
 * the damage method should also be reimplemented to cause a repaint for the area needed to render the caret.
 *
 * @author rendner
 */
public abstract class AbstractCaret extends Observable<Void> implements ICaret
{
    /**
     * The position of the dot.
     */
    @NotNull
    protected IndexPosition dotPosition = new IndexPosition();

    /**
     * The position of the mark.
     */
    @NotNull
    protected IndexPosition markPosition = new IndexPosition();

    /**
     * Used to paint the selection.
     */
    protected IHighlighter.IHighlightPainter selectionPainter;

    /**
     * Provides colors during for rendering a customized caret.
     */
    protected ICaretColorProvider colorProvider;

    /**
     * Indicates if caret is in drag mode.
     */
    protected boolean dragModeIsActive;

    /**
     * Indicates the caret is visible.
     * Note: This flag won't be toggled during blinking. Not visible means that the caret isn't displayed in the ui.
     */
    protected boolean caretIsVisible = true;

    /**
     * The hex viewer component to which the caret was installed.
     */
    protected JHexViewer hexViewer;

    /**
     * Used to let the caret blink if visible.
     */
    protected Timer blinker;

    /**
     * The blink rate.
     */
    protected int blinkRate = 520;

    /**
     * The highlight which describes the current selection.
     */
    protected IHighlighter.IHighlight selectionHighlight;

    /**
     * Internal listener used to observe the JHexViewer component.
     */
    @Nullable
    protected InternalHandler internalHandler;

    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;

        setColorProvider(createDefaultColorProvider());
        setSelectionPainter(new DefaultHighlighter.DefaultHighlightPainter()
        {
            @Override
            protected Color getColor(@NotNull final JHexViewer hexViewer, @NotNull final AreaId id)
            {
                return colorProvider.getSelectionColor(id, hexViewer.getFocusedArea().equals(id));
            }
        });

        internalHandler = new InternalHandler();
        hexViewer.addPropertyChangeListener(internalHandler);

        // add mouse listener for each byte view separately to monitor dragging (to update selection)
        hexViewer.getHexRowsView().addMouseListener(internalHandler);
        hexViewer.getAsciiRowsView().addMouseListener(internalHandler);

        adjustBlinker();
        adjustCaretVisibility();
        adjustSelectionHighlight();
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        hexViewer.removePropertyChangeListener(internalHandler);

        hexViewer.getHexRowsView().removeMouseListener(internalHandler);
        hexViewer.getAsciiRowsView().removeMouseListener(internalHandler);

        internalHandler = null;
        selectionPainter = null;
        this.hexViewer = null;

        removeBlinker();
    }

    @Override
    public void setColorProvider(@NotNull final ICaretColorProvider newColorProvider)
    {
        if (!newColorProvider.equals(colorProvider))
        {
            colorProvider = newColorProvider;

            if (isSelectionEmpty())
            {
                final int dotIndex = dotPosition.getIndex();
                damageCaret(dotIndex, dotIndex);
            }
            else
            {
                damageSelection(markPosition, dotPosition);
            }
        }
    }

    @NotNull
    @Override
    public ICaretColorProvider getColorProvider()
    {
        return colorProvider;
    }

    @NotNull
    @Override
    public IHighlighter.IHighlightPainter getSelectionPainter()
    {
        return selectionPainter;
    }

    @Override
    public void setSelectionPainter(@NotNull final IHighlighter.IHighlightPainter selectionPainter)
    {
        this.selectionPainter = selectionPainter;
    }

    @Override
    public int getBlinkRate()
    {
        return blinkRate;
    }

    @Override
    public void setBlinkRate(final int value)
    {
        CheckUtils.checkMinValue(value, 0);

        if (this.blinkRate != value)
        {
            this.blinkRate = value;
            adjustBlinker();
        }
    }

    @NotNull
    @Override
    public IndexPosition getDot()
    {
        return dotPosition;
    }

    @Override
    public void setDot(final int index)
    {
        setDot(index, IndexPosition.Bias.Forward);
    }

    @Override
    public void setDot(@NotNull final IndexPosition position)
    {
        setDot(position.getIndex(), position.getBias());
    }

    @Override
    public void setDot(final int index, @NotNull final IndexPosition.Bias bias)
    {
        final IndexPosition sanitizedDot = createSanitizedPosition(index, bias);
        changeDotAndMark(sanitizedDot, sanitizedDot);
    }

    @Override
    public void moveDot(final int index)
    {
        moveDot(index, IndexPosition.Bias.Forward);
    }

    @Override
    public void moveDot(@NotNull final IndexPosition position)
    {
        moveDot(position.getIndex(), position.getBias());
    }

    @Override
    public void moveDot(final int index, @NotNull final IndexPosition.Bias bias)
    {
        final IndexPosition sanitizedDot = createSanitizedPosition(index, bias);
        changeDotAndMark(sanitizedDot, markPosition);
    }

    @NotNull
    @Override
    public IndexPosition getMark()
    {
        return markPosition;
    }

    @Override
    public boolean isSelectionEmpty()
    {
        return dotPosition.getIndex() == markPosition.getIndex();
    }

    @Override
    public boolean hasSelection()
    {
        return !isSelectionEmpty();
    }

    @Override
    public int getSelectionStart()
    {
        return Math.min(dotPosition.getIndex(), markPosition.getIndex());
    }

    @Override
    public int getSelectionEnd()
    {
        // decrement the selection end by one, because the mark/dot is always in front of a byte
        // otherwise the selection would include the byte
        return Math.max(dotPosition.getIndex(), markPosition.getIndex()) - 1;
    }

    @Override
    public void clearSelection()
    {
        if (hasSelection())
        {
            setDot(dotPosition.getIndex());
        }
    }

    @Override
    public void setSelection(final int startIndex, final int endIndex)
    {
        final int markIndex = startIndex;
        final int dotIndex = endIndex;

        // the dot/mark is always in front of a byte
        // if the byte in front of the dot/mark should be included
        // the dot/mark should be incremented by one
        if (dotIndex >= markIndex)
        {
            changeDotAndMark(
                    createSanitizedPosition(dotIndex + 1),
                    createSanitizedPosition(markIndex));
        }
        else
        {
            changeDotAndMark(
                    createSanitizedPosition(dotIndex),
                    createSanitizedPosition(markIndex + 1));
        }
    }

    /**
     * Updates the state of the blinker.
     */
    protected void adjustBlinker()
    {
        final boolean caretShouldBeVisible = isSelectionEmpty();

        if (blinkRate > 0 && caretShouldBeVisible)
        {
            if (blinker == null)
            {
                blinker = new Timer(blinkRate, internalHandler);
            }

            blinker.setDelay(blinkRate);

            if (!blinker.isRunning())
            {
                blinker.start();
            }
        }
        else
        {
            removeBlinker();
        }
    }

    /**
     * Removes the blinker and unregisters attached listeners from the blinker.
     */
    protected void removeBlinker()
    {
        if (blinker != null)
        {
            blinker.stop();
            blinker.removeActionListener(internalHandler);
            blinker = null;
        }
    }

    /**
     * Updates the position of the dot and mark if they have changed.
     * This method also invalidates the areas which have to be repainted after the update and notifies the observer.
     *
     * @param dot  the new sanitized dot position.
     * @param mark the new sanitized mark position.
     */
    protected void changeDotAndMark(@NotNull final IndexPosition dot, @NotNull final IndexPosition mark)
    {
        if (!dot.equals(dotPosition) || !mark.equals(markPosition))
        {
            final int oldDotIndex = dotPosition.getIndex();

            dotPosition = dot;
            markPosition = mark;

            damageCaret(oldDotIndex, dotPosition.getIndex());

            adjustSelectionHighlight();
            adjustCaretVisibility();
            adjustBlinker();
            setChangedAndNotifyObservers();
        }
    }

    /**
     * Creates a sanitized IndexPosition with a forward bias.
     * <p/>
     * Note: The index and the bias will be adjusted to always point to a valid position. Therefore the forward bias may
     * become a backward bias.
     *
     * @param index the index of the position to create.
     * @return the sanitized position.
     */
    @NotNull
    protected IndexPosition createSanitizedPosition(final int index)
    {
        return createSanitizedPosition(index, IndexPosition.Bias.Forward);
    }

    /**
     * Creates a sanitized IndexPosition.
     * <p/>
     * The index and the bias will be adjusted to always point to a valid position. Therefore the bias may change.
     *
     * @param index the index of the position to create.
     * @param bias  an interest toward one of the two sides of the position in boundary conditions when the index is
     *              ambiguous. The bias will be adjusted if the new index doesn't allow the specified value.
     * @return the sanitized position.
     */
    @NotNull
    protected IndexPosition createSanitizedPosition(final int index, @NotNull final IndexPosition.Bias bias)
    {
        final int sanitizedIndex = getSanitizedIndex(index);
        final IndexPosition.Bias sanitizedBias = getSanitizedBias(sanitizedIndex, bias);
        return new IndexPosition(sanitizedIndex, sanitizedBias);
    }

    /**
     * Sanitizes an index, to ensure that the index is in the range [0, hexViewer.lastPossibleCaretIndex()].
     *
     * @param index the index to sanitize. If negative or beyond the last possible caret index, the index is adjusted to
     *              the beginning or to the end, respectively.
     * @return a sanitized index, &gt=0.
     * @see JHexViewer#lastPossibleCaretIndex()
     */
    protected int getSanitizedIndex(final int index)
    {
        return Math.min(hexViewer.lastPossibleCaretIndex(), Math.max(index, 0));
    }

    /**
     * Sanitizes a bias, to ensure that the bias is valid for a specific index.
     *
     * @param index the index, has to be sanitized.
     * @param bias  the bias to be sanitized.
     * @return the sanitized bias if specified is invalid, otherwise the specified bias.
     */
    @NotNull
    protected IndexPosition.Bias getSanitizedBias(final int index, @NotNull final IndexPosition.Bias bias)
    {
        return index == 0 ? IndexPosition.Bias.Backward : bias;
    }

    /**
     * @return returns the bounds of the current caret (also for invisible carets).
     * The bounds is used to scroll automatically to the current caret position.
     */
    @NotNull
    protected abstract Rectangle calculateVisibleRectForCaret();

    /**
     * Updates the highlighter which describes the selection and adds the updated instance to the
     * JHexViewer's highlighter.
     */
    protected void adjustSelectionHighlight()
    {
        hexViewer.getHighlighter().ifPresent(highlighter ->
        {
            if (isSelectionEmpty())
            {
                if (selectionHighlight != null)
                {
                    highlighter.removeHighlight(selectionHighlight);
                    selectionHighlight = null;
                }
            }
            else
            {
                if (selectionHighlight == null)
                {
                    selectionHighlight = highlighter.setSelectionHighlight(getSelectionStart(), getSelectionEnd(), getSelectionPainter());
                }
                else
                {
                    highlighter.changeHighlight(selectionHighlight, getSelectionStart(), getSelectionEnd());
                }
            }
        });
    }

    /**
     * Scrolls the associated view (if necessary) to make
     * the caret visible. By default
     * the scrollRectToVisible method is called on the
     * associated component.
     */
    protected void adjustCaretVisibility()
    {
        hexViewer.getScrollableByteRowsContainer().scrollRectToVisible(calculateVisibleRectForCaret());
    }

    /**
     * Creates a default color provider which is automatically assigned to the caret when the caret is installed to a
     * JHexViewer.
     *
     * @return a default color provider, never <code>null</code>.
     */
    protected ICaretColorProvider createDefaultColorProvider()
    {
        return new ICaretColorProvider()
        {
            @NotNull
            private final Color focusedCaretColor = new Color(0, 148, 200);
            @NotNull
            private final Color caretColor = new Color(0, 148, 200, 100);
            @NotNull
            private final Color focusedSelectionColor = new Color(0, 148, 200, 125);
            @NotNull
            private final Color selectionColor = new Color(0, 148, 200, 55);

            @NotNull
            @Override
            public Color getCaretColor(@NotNull final AreaId areaId, final boolean focused)
            {
                return focused ? focusedCaretColor : caretColor;
            }

            @NotNull
            @Override
            public Color getSelectionColor(@NotNull final AreaId areaId, final boolean focused)
            {
                return focused ? focusedSelectionColor : selectionColor;
            }
        };
    }

    /**
     * Damages the old and new position of the caret which results in a repaint for these areas.
     * The IDamager bound to the associated JHexViewer is used to damage the areas.
     *
     * @param oldIndex the old position of the caret.
     * @param newIndex the new position of the caret.
     * @see cms.rendner.hexviewer.core.uidelegate.damager.IDamager
     */
    protected void damageCaret(final int oldIndex, final int newIndex)
    {
        hexViewer.getDamager().ifPresent(damager -> damager.damageCaret(oldIndex, newIndex));
    }

    /**
     * Damages the selection of the caret which results in a repaint for this range of selected bytes.
     * The IDamager bound to the associated JHexViewer is used to damage the areas.
     *
     * @param start the start position of the selection.
     * @param end   the end position of the selection.
     */
    protected void damageSelection(final IndexPosition start, final IndexPosition end)
    {
        hexViewer.getDamager().ifPresent(damager -> {
            damager.damageBytes(AreaId.HEX, start.getIndex(), end.getIndex());
            damager.damageBytes(AreaId.ASCII, start.getIndex(), end.getIndex());
        });
    }

    /**
     * Callback triggered by the blinker which updates the visibility of the blinking caret and requests a repaint
     * after modifying the visibility.
     *
     * @param event the event send by the a Timer.
     */
    protected void handleBlinkerAction(final ActionEvent event)
    {
        caretIsVisible = !caretIsVisible;
        final int dotIndex = dotPosition.getIndex();
        damageCaret(dotIndex, dotIndex);
    }

    /**
     * Handles all kind of property changes.
     *
     * @param event the event send by the owner of the changed property.
     */
    protected void handlePropertyChange(final PropertyChangeEvent event)
    {
        final String propertyName = event.getPropertyName();

        if (event.getSource().equals(hexViewer))
        {
            if (JHexViewer.PROPERTY_HIGHLIGHTER.equals(propertyName))
            {
                adjustSelectionHighlight();
            }
        }
    }

    /**
     * Invoked when a mouse button on a ByteRowsView is pressed and then dragged. <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the caret where the drag originated until the mouse button is released (regardless of whether the
     * mouse location is within the bounds of the ByteRowsView which initiated the drag).
     * <p>
     * Updates an already started selection.
     *
     * @param event      the event
     * @param targetView the view which sends the event.
     */
    protected abstract void handleMouseDragged(final MouseEvent event, final ByteRowsView targetView);

    /**
     * Invoked when a mouse button has been released on a ByteRowsView.
     *
     * @param event      the event
     * @param targetView the view which sends the event.
     */
    protected abstract void handleMouseReleased(final MouseEvent event, final ByteRowsView targetView);

    /**
     * Invoked when the mouse button has been pressed.
     * Starts a selection if the press occurred in a selectable area.
     *
     * @param event      the event
     * @param targetView the view which sends the event.
     */
    protected abstract void handleMousePressed(final MouseEvent event, final ByteRowsView targetView);

    /**
     * Internal observer to hide the public listener interfaces.
     */
    protected class InternalHandler extends MouseInputAdapter implements ActionListener, PropertyChangeListener
    {
        @Override
        public void actionPerformed(final ActionEvent event)
        {
            handleBlinkerAction(event);
        }

        @Override
        public void mousePressed(final MouseEvent event)
        {
            final Object source = event.getSource();
            if (source instanceof ByteRowsView)
            {
                handleMousePressed(event, (ByteRowsView) source);
            }
        }

        @Override
        public void mouseDragged(final MouseEvent event)
        {
            final Object source = event.getSource();
            if (source instanceof ByteRowsView)
            {
                handleMouseDragged(event, (ByteRowsView) source);
            }
        }

        @Override
        public void mouseReleased(final MouseEvent event)
        {
            final Object source = event.getSource();
            if (source instanceof ByteRowsView)
            {
                handleMouseReleased(event, (ByteRowsView) source);
            }
        }

        @Override
        public void propertyChange(final PropertyChangeEvent event)
        {
            handlePropertyChange(event);
        }
    }
}
