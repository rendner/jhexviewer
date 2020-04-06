package cms.rendner.hexviewer.view.components.areas.bytes.painter.middleground;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaLayerPainter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The painting request is forwarded to the {@link cms.rendner.hexviewer.view.components.caret.ICaret ICaret}
 * and {@link cms.rendner.hexviewer.view.components.highlighter.IHighlighter IHighlighter} of the JHexViewer to paint
 * themselves.
 *
 * @author rendner
 */
public class DefaultMiddlegroundPainter implements IAreaLayerPainter
{
    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        final ByteArea area = (ByteArea) component;
        hexViewer.getHighlighter().ifPresent(highlighter -> highlighter.paint(g, area));
        hexViewer.getCaret().ifPresent(caret -> caret.paint(g, area));
    }
}
