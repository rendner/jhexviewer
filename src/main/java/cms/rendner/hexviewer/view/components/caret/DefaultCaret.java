package cms.rendner.hexviewer.view.components.caret;

import cms.rendner.hexviewer.common.geom.HDimension;
import cms.rendner.hexviewer.common.rowtemplate.bytes.IByteRowTemplate;
import cms.rendner.hexviewer.common.utils.ObjectUtils;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.bytes.model.colors.IByteColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
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
    private final Map<AreaId, IHighlighter.IHighlightPainter> selectionPainterMap = new HashMap<>();

    private final Color DEFAULT_CARET_COLOR = Color.WHITE;

    @Override
    public void install(final @NotNull JHexViewer hexViewer)
    {
        super.install(hexViewer);
        final IHighlighter.IHighlightPainter selectionPainter = new SelectionHighlighter();
        selectionPainterMap.put(AreaId.HEX, selectionPainter);
        selectionPainterMap.put(AreaId.TEXT, selectionPainter);
    }

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
        final IByteRowTemplate rowTemplate = area.getRowTemplate();
        if (rowTemplate != null)
        {
            final Rectangle elementBounds = rowTemplate.elementBounds(0, rowTemplate.elementCount() - 1);
            final HDimension elementsBounds = new HDimension(elementBounds.x, elementBounds.width);
            selectionPainterMap.get(area.getAreaId()).paint(g, hexViewer, area, elementsBounds, getSelectionStart(), getSelectionEnd());
        }
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
        final IByteColorProvider cp = area.getColorProvider();
        return cp == null ? DEFAULT_CARET_COLOR : ObjectUtils.ifNotNullOtherwise(cp.getCaret(), DEFAULT_CARET_COLOR);
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

    private static class SelectionHighlighter extends DefaultHighlighter.DefaultHighlightPainter
    {
        @Nullable
        @Override
        protected Color getColor(@NotNull final ByteArea area)
        {
            final IByteColorProvider cp = area.getColorProvider();
            return cp == null ? null : cp.getSelection();
        }
    }
}
