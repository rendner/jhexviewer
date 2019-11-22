package cms.rendner.hexviewer.view.components.caret;

import cms.rendner.hexviewer.common.geom.HDimension;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import cms.rendner.hexviewer.view.components.highlighter.DefaultHighlighter;
import cms.rendner.hexviewer.view.components.highlighter.IHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of a caret, which implements the missing methods of the {@link BaseCaret} class to paint the
 * caret and selection.
 *
 * @author rendner
 */
public class DefaultCaret extends BaseCaret
{
    /**
     * Stores the selection painters for the byte areas served by this caret.
     */
    private final Map<ByteArea, IHighlighter.IHighlightPainter> selectionPainterMap = new HashMap<>();

    @Override
    public void uninstall(final @NotNull JHexViewer hexViewer)
    {
        selectionPainterMap.clear();
        super.uninstall(hexViewer);
    }

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final ByteArea area)
    {
        if (hasSelection())
        {
            paintSelection(g, area);
        }
        else if (isVisible())
        {
            paintCaret(g, area);
        }
    }

    private void paintSelection(@NotNull final Graphics2D g, @NotNull final ByteArea area)
    {
        area.getRowTemplate().ifPresent(rowTemplate -> {
            final Rectangle elementBounds = rowTemplate.elementBounds(0, rowTemplate.elementCount() - 1);
            final HDimension elementsBounds = new HDimension(elementBounds.x, elementBounds.width);
            getPainter(area).paint(g, area, elementsBounds, getSelectionStart(), getSelectionEnd());
        });
    }

    private void paintCaret(@NotNull final Graphics2D g, @NotNull final ByteArea area)
    {
        final Rectangle caretRect = area.getCaretRect(getDot());
        final int caretInset = 2;
        g.setColor(getCaretColor(area));
        g.fillRect(caretRect.x, caretRect.y + caretInset, caretRect.width, caretRect.height - caretInset * 2);
    }

    @NotNull
    private Color getCaretColor(@NotNull final ByteArea area)
    {
        return area.getColorProvider()
                .map(IByteColorProvider::getCaret)
                .orElse(Color.white);
    }

    @NotNull
    private IHighlighter.IHighlightPainter getPainter(@NotNull final ByteArea area)
    {
        if (!selectionPainterMap.containsKey(area))
        {
            selectionPainterMap.put(area, new DefaultHighlighter.DefaultHighlightPainter()
            {
                @Nullable
                @Override
                protected Color getColor(@NotNull final ByteArea area)
                {
                    return area.getColorProvider()
                            .map(IByteColorProvider::getSelection)
                            .orElse(null);
                }
            });
        }

        return selectionPainterMap.get(area);
    }

    @Override
    protected void damageCaret(final long oldIndex, final long newIndex)
    {
        hexViewer.getDamager().ifPresent(damager -> damager.damageCaret(oldIndex, newIndex));
    }

    @Override
    protected void damageSelection(final long oldStartIndex, final long oldEndIndex, final long newStartIndex, final long newEndIndex)
    {
        hexViewer.getDamager().ifPresent(damager -> {
            damager.damageChangedHighlight(
                    Math.min(oldStartIndex, oldEndIndex),
                    Math.max(oldStartIndex, oldEndIndex),
                    Math.min(newStartIndex, newEndIndex),
                    Math.max(newStartIndex, newEndIndex)
            );
        });
    }
}
