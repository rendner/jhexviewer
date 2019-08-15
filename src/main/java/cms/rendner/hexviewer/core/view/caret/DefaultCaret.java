package cms.rendner.hexviewer.core.view.caret;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.areas.ByteRowsView;
import cms.rendner.hexviewer.core.view.color.ICaretColorProvider;
import cms.rendner.hexviewer.core.view.geom.IndexPosition;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A default implementation of Caret. The caret is rendered as a vertical line in the color specified by the
 * ICaretColorProvider.
 *
 * @author rendner
 * @see cms.rendner.hexviewer.core.view.color.ICaretColorProvider
 */
public class DefaultCaret extends AbstractCaret
{
    /**
     * Keeps track of the view where the dragging start (to create a selection by mouse).
     */
    protected ByteRowsView activeRowsView;

    @Override
    public void uninstall(@NotNull final JHexViewer hexViewer)
    {
        hexViewer.getHexRowsView().removeMouseMotionListener(internalHandler);
        hexViewer.getAsciiRowsView().removeMouseMotionListener(internalHandler);

        super.uninstall(hexViewer);
    }

    @Override
    public void paint(@NotNull final Graphics g, @NotNull final ByteRowsView rowsView)
    {
        if (caretIsVisible && isSelectionEmpty())
        {
            final Rectangle caretRect = rowsView.getCaretRect(dot.getIndex());
            final AreaId areaToPaint = rowsView.getId();
            final int caretInset = 2;
            g.setColor(colorProvider.getCaretColor(areaToPaint, hexViewer.getFocusedArea().equals(areaToPaint)));
            g.fillRect(caretRect.x, caretRect.y + caretInset, caretRect.width, caretRect.height - caretInset * 2);
        }
    }

    @NotNull
    @Override
    protected Rectangle calculateVisibleRectForCaret()
    {
        final Rectangle visibleRect;
        final AreaId id = hexViewer.getFocusedArea();
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);


        int caretIndex = dot.getIndex();

        if (!dragModeIsActive && isSelectionEmpty())
        {
            final Rectangle caretRect = rowsView.getCaretRect(caretIndex);
            final Rectangle byteRect = rowsView.getByteRect(caretIndex);
            visibleRect = caretRect.union(byteRect);
        }
        else
        {
            final int caretIndexInRow = hexViewer.byteIndexToIndexInRow(caretIndex);

            if (caretIndexInRow == 0)
            {
                // special case: focused rowsView isn't fully visible inside the visible rectangle
                //
                // A caret can't be placed after the last byte of a row, the caret is automatically adjusted in front of the first byte of the next row.
                // When the user clicks at the end of a row (after the last byte) and starts a mouseDrag and the width of the window is smaller
                // than the width of the active rowsView, the visible part in the window shouldn't jump to the first byte of the next row. Otherwise
                // the user would be confused about the jumping content.
                //
                if (dot.getBias().equals(IndexPosition.Bias.Backward))
                {
                    caretIndex = getSanitizedIndex(caretIndex - 1);
                    visibleRect = rowsView.getByteRect(caretIndex);
                }
                else
                {
                    visibleRect = rowsView.getByteRect(caretIndex);
                }
            }
            else
            {
                // try to show the bytes which surround (before and after) the caret
                final Rectangle byteAfterCaret = rowsView.getByteRect(caretIndex);
                final Rectangle byteBeforeCaret = rowsView.getByteRect(getSanitizedIndex(caretIndex - 1));
                visibleRect = byteBeforeCaret.union(byteAfterCaret);
            }
        }

        final int spaceLeftAndRight = 2;
        visibleRect.x -= spaceLeftAndRight;
        visibleRect.width += spaceLeftAndRight * 2;

        // add view position
        visibleRect.x += rowsView.getX();
        visibleRect.y += rowsView.getY();

        return visibleRect;
    }

    /**
     * Clamps the coordinates of a point object to always stay inside a specified area.
     *
     * @param id         the id of the area to stay.
     * @param x the x-location to adjust.
     * @param y the y-location to adjust.
     * @return the adjusted coordinates.
     */
    @NotNull
    protected Point clampLocationToRowsBounds(@NotNull final AreaId id, final int x, final int y)
    {
        final ByteRowsView rowsView = hexViewer.getByteRowsView(id);
        final Point result = new Point(x, y);

        if (x < 0)
        {
            result.x = 0;
        }
        else if (x >= rowsView.getWidth())
        {
            result.x = rowsView.getWidth() - 1;
        }

        if (y < 0)
        {
            result.y = 0;
        }
        else if (y >= rowsView.getHeight())
        {
            result.y = rowsView.getHeight() - 1;
        }

        return result;
    }

    /**
     * Requests the focus for the JHexViewer.
     */
    protected void requestComponentFocus()
    {
        if (!hexViewer.hasFocus() && hexViewer.isRequestFocusEnabled())
        {
            hexViewer.requestFocus();
        }
    }

    @NotNull
    @Override
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


    @Override
    protected void handleMouseReleased(@NotNull final MouseEvent event, @NotNull final ByteRowsView targetView)
    {
        if (!event.isConsumed() && SwingUtilities.isLeftMouseButton(event))
        {
            dragModeIsActive = false;

            if (activeRowsView != null)
            {
                activeRowsView.removeMouseMotionListener(internalHandler);
            }

            if (isSelectionEmpty())
            {
                // if the selection was modified during mouse-drag the end of the selection is visible
                // but if the selection is empty the caret can be outside of the visible rect of the component
                adjustCaretVisibility();
            }
        }
    }

    @Override
    protected void handleMouseDragged(@NotNull final MouseEvent event, @NotNull final ByteRowsView targetView)
    {
        if (!event.isConsumed() && SwingUtilities.isLeftMouseButton(event))
        {
            if (dragModeIsActive)
            {
                if (activeRowsView == targetView)
                {
                    final Point clampedLocation = clampLocationToRowsBounds(activeRowsView.getId(), event.getX(), event.getY());
                    final ByteRowsView.ByteHitInfo byteHit = activeRowsView.locationToByteHit(clampedLocation.x, clampedLocation.y);

                    if (byteHit != null)
                    {
                        moveDot(byteHit.getInsertionPosition());
                    }
                }
            }
        }
    }

    @Override
    protected void handleMousePressed(@NotNull final MouseEvent event, @NotNull final ByteRowsView targetView)
    {
        if (!event.isConsumed() && SwingUtilities.isLeftMouseButton(event))
        {
            requestComponentFocus();

            activeRowsView = targetView;

            final ByteRowsView.ByteHitInfo byteHit = activeRowsView.locationToByteHit(event.getX(), event.getY());

            if (byteHit != null)
            {
                dragModeIsActive = true;
                hexViewer.setFocusedArea(activeRowsView.getId());
                activeRowsView.addMouseMotionListener(internalHandler);

                setDot(byteHit.getInsertionPosition());
            }
        }
    }
}
