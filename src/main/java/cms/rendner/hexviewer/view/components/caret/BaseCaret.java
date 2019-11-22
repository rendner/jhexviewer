package cms.rendner.hexviewer.view.components.caret;

import cms.rendner.hexviewer.common.rowtemplate.bytes.HitInfo;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.damager.IDamager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An abstract implementation of {@link ICaret}.
 * <p/>
 * A customized caret appearance can be achieved by implementing the paint method. If the paint method is changed,
 * the damage method should also be reimplemented to cause a repaint for the area needed to render the caret.
 *
 * @author rendner
 */
public abstract class BaseCaret implements ICaret
{
    /**
     * The blink rate.
     */
    private static final int BLINK_RATE = 520;

    /**
     * Contains the registered caret listeners.
     */
    @NotNull
    private final List<ICaretListener> caretListeners = new ArrayList<>();

    /**
     * The position of the dot.
     */
    private long dot = 0L;

    /**
     * The position of the mark.
     */
    private long mark = 0L;

    /**
     * The last mouse position during drag.
     * This position is updated whenever a drag event occurs, to adjust the visible area during the drag.
     */
    private Point lastDragMousePos;

    /**
     * Indicates the caret is visible.
     */
    private boolean caretIsVisible = true;

    /**
     * List of areas served by this caret.
     */
    private List<ByteArea> areas;

    /**
     * Indicates if caret is in drag mode.
     */
    @Nullable
    private ByteArea dragStartArea;

    /**
     * Used to let the caret blink.
     */
    private Timer blinker;

    /**
     * Internal listener used to observe the {@link ByteArea} component.
     */
    @Nullable
    private InternalHandler internalHandler;

    /**
     * The hexViewer component to which this caret was installed.
     */
    protected JHexViewer hexViewer;

    @Override
    public void install(@NotNull final JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;

        internalHandler = new InternalHandler();

        hexViewer.addPropertyChangeListener(internalHandler);

        areas = Arrays.asList(hexViewer.getHexArea(), hexViewer.getAsciiArea());
        areas.forEach(area -> area.addMouseListener(internalHandler));

        adjustBlinker();
        adjustCaretVisibility();
    }

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        hexViewer.removePropertyChangeListener(internalHandler);

        areas.forEach(area -> {
            area.removeMouseMotionListener(internalHandler);
            area.addMouseListener(internalHandler);
        });

        internalHandler = null;

        areas = null;
        this.hexViewer = null;
        removeBlinker();
    }

    @Override
    public long getSelectionStart()
    {
        return Math.min(dot, mark);
    }

    @Override
    public long getSelectionEnd()
    {
        // decrement the selection end by one, because the mark/dot is always in front of a byte
        // otherwise the selection would include the byte
        return Math.max(dot, mark) - 1;
    }

    @Override
    public void moveCaretRelatively(final long offsetShift, final boolean withSelection, final boolean scrollToCaret)
    {
        moveCaret(dot + offsetShift, withSelection, scrollToCaret);
    }

    @Override
    public void moveCaret(final long offset, final boolean withSelection, final boolean scrollToCaret)
    {
        final long sanitizedDot = getSanitizedIndex(offset);

        if (sanitizedDot != dot || withSelection != hasSelection())
        {
            final long oldDot = dot;
            final long oldMark = mark;

            if (withSelection)
            {
                dot = sanitizedDot;
            }
            else
            {
                mark = dot = sanitizedDot;
            }

            notifyListener(oldDot, oldMark);

            // Always damage caret and selection because a custom caret could also display the caret when
            // a selection was displayed. Otherwise the custom caret has to re-implement this method to only
            // damage the caret.
            damageCaret(oldDot, dot);
            damageSelection(oldMark, oldDot, mark, dot);

            adjustBlinker();
        }

        if (scrollToCaret)
        {
            adjustCaretVisibility();
        }
    }

    @Override
    public long getDot()
    {
        return dot;
    }

    @Override
    public long getMark()
    {
        return mark;
    }

    @Override
    public void addCaretListener(@NotNull final ICaretListener listener)
    {
        if (!caretListeners.contains(listener))
        {
            caretListeners.add(listener);
        }
    }

    @Override
    public void removeCaretListener(@NotNull final ICaretListener listener)
    {
        caretListeners.remove(listener);
    }

    /**
     * Notifies all registered listener about the changed caret position.
     *
     * @param oldDot  the old position of the dot.
     * @param oldMark the old position of the mark.
     */
    private void notifyListener(final long oldDot, final long oldMark)
    {
        if (!caretListeners.isEmpty())
        {
            final CaretEvent event = new CaretEvent(this, oldDot, oldMark, dot, mark);
            caretListeners.forEach(i -> i.caretPositionChanged(event));
        }
    }

    /**
     * Sanitizes an index, to ensure that the index is in the range [0, ICaretModel.lastPossibleCaretIndex()].
     *
     * @param index the index to sanitize. If negative or beyond the last possible caret index, the index is adjusted to
     *              the beginning or to the end, respectively.
     * @return a sanitized index, &gt=0.
     */
    private long getSanitizedIndex(final long index)
    {
        return Math.min(hexViewer.getLastPossibleCaretIndex(), Math.max(index, 0));
    }

    @Override
    public boolean hasSelection()
    {
        return dot != mark;
    }

    private Rectangle computeScrollRect(final ByteArea byteArea)
    {
        final Rectangle visibleRect = byteArea.getVisibleRect();

        final Rectangle result = byteArea.getRowRect(0);
        result.y = visibleRect.y;
        result.width = Math.min(result.width, visibleRect.width);

        return result;
    }

    protected boolean isVisible()
    {
        return caretIsVisible;
    }

    /**
     * Updates the state of the blinker.
     */
    private void adjustBlinker()
    {
        if (BLINK_RATE > 0 && !hasSelection())
        {
            if (blinker == null)
            {
                blinker = new Timer(BLINK_RATE, internalHandler);
            }

            blinker.setDelay(BLINK_RATE);

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
    private void removeBlinker()
    {
        if (blinker != null)
        {
            blinker.stop();
            blinker.removeActionListener(internalHandler);
            blinker = null;
        }
    }

    /**
     * Returns the bounds of the current caret (also for invisible carets).
     * The bounds is used to scroll automatically to the current caret position.
     *
     * @return the bounds of the current caret inside the area.
     */
    @NotNull
    protected Rectangle calculateVisibleRectForCaret(@NotNull final ByteArea area)
    {
        long indexOfLatestSelectedByte = dot;

        if (indexOfLatestSelectedByte > 0)
        {
            if (dragStartArea != null)
            {
                if (lastDragMousePos != null)
                {
                    final int indexInRow = hexViewer.byteIndexToIndexInRow(indexOfLatestSelectedByte);

                    if (indexInRow == 0)
                    {
                        final Rectangle byteRect = area.getByteRect(indexOfLatestSelectedByte);

                        if (lastDragMousePos.getX() > byteRect.x + byteRect.width)
                        {
                            indexOfLatestSelectedByte--;
                        }
                    }
                }
            }
            else if (hasSelection())
            {
                final boolean selectionFromLeftToRight = indexOfLatestSelectedByte > mark;
                if (selectionFromLeftToRight)
                {
                    indexOfLatestSelectedByte--;
                }
            }
        }

        final int caretIndexInRow = hexViewer.byteIndexToIndexInRow(indexOfLatestSelectedByte);
        final boolean isNotLastCaretIndex = hexViewer.getLastPossibleCaretIndex() != indexOfLatestSelectedByte;
        final boolean isNotAtFirsIndexInRow = caretIndexInRow != 0;
        final boolean isNotAtLastIndexInRow = (caretIndexInRow + 1) != hexViewer.getBytesPerRow();

        Rectangle visibleRect = null;

        if (isNotLastCaretIndex && isNotAtFirsIndexInRow && isNotAtLastIndexInRow)
        {
            // try to show the bytes which surround the caret
            final Rectangle byteBeforeCaret = area.getByteRect(getSanitizedIndex(indexOfLatestSelectedByte - 1));
            visibleRect = byteBeforeCaret.union(area.getByteRect(indexOfLatestSelectedByte));
        }

        if (visibleRect == null)
        {
            final Rectangle byteRect = area.getByteRect(indexOfLatestSelectedByte);
            final Rectangle caretRect = area.getCaretRect(indexOfLatestSelectedByte);

            visibleRect = new Rectangle(
                    caretRect.x -= byteRect.width,
                    Math.min(caretRect.y, byteRect.y),
                    caretRect.width + 3 * byteRect.width,
                    Math.max(caretRect.height, byteRect.height));
        }

        final int spaceLeftAndRight = 2;
        visibleRect.x -= spaceLeftAndRight;
        visibleRect.width += spaceLeftAndRight * 2;

        return visibleRect;
    }

    /**
     * Clamps the coordinates of a point object to always stay inside a specified area.
     *
     * @param area area to clamp to.
     * @param x    the x-location to adjust.
     * @param y    the y-location to adjust.
     * @return the adjusted coordinates.
     */
    @NotNull
    private Point clampLocationToArea(@NotNull final ByteArea area, final int x, final int y)
    {
        final Point result = new Point(x, y);

        if (x < 0)
        {
            result.x = 0;
        }
        else if (x >= area.getWidth())
        {
            result.x = area.getWidth() - 1;
        }

        if (y < 0)
        {
            result.y = 0;
        }
        else if (y >= area.getHeight())
        {
            result.y = area.getHeight() - 1;
        }

        return result;
    }

    /**
     * Scrolls the associated view (if necessary) to make the caret visible. By default the scrollRectToVisible method
     * is called on the associated component.
     */
    private void adjustCaretVisibility()
    {
        final ByteArea area = dragStartArea == null ? hexViewer.getCaretFocusedArea() : dragStartArea;
        area.scrollRectToVisible(calculateVisibleRectForCaret(area));
    }

    /**
     * Damages the old and new position of the caret which results in a repaint for these areas.
     * The IDamager bound to the associated {@link JHexViewer} is used to damage the areas.
     *
     * @param oldIndex the old position of the caret.
     * @param newIndex the new position of the caret.
     * @see IDamager
     */
    abstract protected void damageCaret(final long oldIndex, final long newIndex);

    /**
     * Damages the selection of the caret which results in a repaint for this range of selected bytes.
     * The IDamager bound to the associated {@link JHexViewer} is used to damage the areas.
     *
     * @param oldStartIndex the old start position of the selection.
     * @param oldEndIndex   the old end position of the selection.
     * @param newStartIndex the new end position of the selection.
     * @param newEndIndex   the new end position of the selection.
     */
    abstract protected void damageSelection(final long oldStartIndex, final long oldEndIndex, final long newStartIndex, final long newEndIndex);

    /**
     * Called by the blinker to update the visibility of the blinking caret.
     */
    private void toggleCaret()
    {
        caretIsVisible = !caretIsVisible;
        damageCaret(dot, dot);
    }

    /**
     * Invoked when a mouse button has been released on the area.
     *
     * @param event the event
     */
    private void handleMouseReleased(@NotNull final MouseEvent event)
    {
        if (!event.isConsumed() && SwingUtilities.isLeftMouseButton(event))
        {
            if (dragStartArea != null)
            {
                dragStartArea.removeMouseMotionListener(internalHandler);
            }

            lastDragMousePos = null;
            dragStartArea = null;

            if (!hasSelection())
            {
                // if the selection was modified during mouse-drag the end of the selection is visible
                // but if the selection is empty the caret can be outside of the visible rect of the component
                adjustCaretVisibility();
            }
        }
    }

    /**
     * Invoked when a mouse button on the area is pressed and then dragged. <code>MOUSE_DRAGGED</code> events
     * will continue to be delivered to the caret where the drag originated until the mouse button is released
     * (regardless of whether the mouse location is within the bounds of the area).
     * <p/>
     * Updates an already started selection.
     *
     * @param event the event
     */
    private void handleMouseDragged(@NotNull final MouseEvent event)
    {
        if (!event.isConsumed() && SwingUtilities.isLeftMouseButton(event))
        {
            if (dragStartArea != null)
            {
                lastDragMousePos = event.getPoint();

                final Point clampedLocation = clampLocationToArea(dragStartArea, event.getX(), event.getY());

                final long caretOffset = dragStartArea
                        .hitTest(clampedLocation.x, clampedLocation.y)
                        .map(HitInfo::insertionIndex)
                        .orElse(hexViewer.getLastPossibleCaretIndex());

                moveCaret(caretOffset, true, true);
            }
        }
    }

    /**
     * Invoked when the mouse button has been pressed.
     * Starts a selection if the press occurred in an area.
     *
     * @param event the event
     */
    private void handleMousePressed(@NotNull final MouseEvent event)
    {
        if (!event.isConsumed() && SwingUtilities.isLeftMouseButton(event))
        {
            lastDragMousePos = event.getPoint();

            dragStartArea = (ByteArea) event.getSource();
            dragStartArea.addMouseMotionListener(internalHandler);

            hexViewer.setCaretFocusedArea(dragStartArea);

            final long caretOffset = dragStartArea
                    .hitTest(event.getX(), event.getY())
                    .map(HitInfo::insertionIndex)
                    .orElse(hexViewer.getLastPossibleCaretIndex());

            moveCaret(caretOffset, false, true);
        }
    }

    private void handleHexViewerPropertyChange(@NotNull final PropertyChangeEvent event)
    {
        if (JHexViewer.PROPERTY_CARET_FOCUSED_AREA.equals(event.getPropertyName()))
        {
            if (dragStartArea == null)
            {
                final ByteArea focusedArea = hexViewer.getCaretFocusedArea();
                focusedArea.scrollRectToVisible(computeScrollRect(focusedArea));
            }
        }
    }

    /**
     * Internal observer to hide the public listener interfaces.
     */
    private class InternalHandler extends MouseInputAdapter implements ActionListener, PropertyChangeListener
    {
        @Override
        public void actionPerformed(@NotNull final ActionEvent event)
        {
            toggleCaret();
        }

        @Override
        public void mousePressed(@NotNull final MouseEvent event)
        {
            handleMousePressed(event);
        }

        @Override
        public void mouseDragged(@NotNull final MouseEvent event)
        {
            if (event.getSource() == dragStartArea)
            {
                handleMouseDragged(event);
            }
        }

        @Override
        public void mouseReleased(@NotNull final MouseEvent event)
        {
            if (event.getSource() == dragStartArea)
            {
                handleMouseReleased(event);
            }
        }

        @Override
        public void propertyChange(@NotNull final PropertyChangeEvent event)
        {
            if (event.getSource() == hexViewer)
            {
                handleHexViewerPropertyChange(event);
            }
        }
    }
}
