package cms.rendner.hexviewer.view.ui.painter.bytes;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.BasicAreaPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.background.IAreaBackgroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.foreground.IAreaForegroundPainter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Paints the byte-area to which the painter belongs.
 * <p/>
 * The paint request is also forwarded to the {@link cms.rendner.hexviewer.view.components.caret.ICaret ICaret}
 * and {@link cms.rendner.hexviewer.view.components.highlighter.IHighlighter IHighlighter} to paint themselves. These
 * objects are painted between the background and foreground of the area.
 * <p/>
 * To allow customizing of the paint process this class uses an {@link IAreaForegroundPainter} and
 * {@link IAreaBackgroundPainter} which can be exchanged during runtime.
 *
 * @author rendner
 */
public class ByteAreaPainter extends BasicAreaPainter
{
    /**
     * Creates a new instance which paints a byte-area.
     */
    public ByteAreaPainter()
    {
        setForegroundPainter(new ByteRowForegroundPainter());
    }

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        paintBackground(g, hexViewer, component);
        paintMiddleground(g, hexViewer, component);
        paintForeground(g, hexViewer, component);
    }

    /**
     * Paints the middleground of the area.
     * <p/>
     * The caret, selection and the highlights are painted between the background and the foreground.
     *
     * @param g the Graphics2D object of the area to paint into.
     */
    protected void paintMiddleground(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        final ByteArea area = (ByteArea) component;
        hexViewer.getHighlighter().ifPresent(highlighter -> highlighter.paint(g, area));
        hexViewer.getCaret().ifPresent(caret -> caret.paint(g, area));
    }
}
