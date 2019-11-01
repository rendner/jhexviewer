package cms.rendner.hexviewer.view.ui.painter.bytes;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.bytes.ByteArea;
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
public class ByteAreaPainter extends BasicAreaPainter<ByteArea>
{
    /**
     * The {@link JHexViewer} to which the area to be painted belongs.
     */
    @NotNull
    private final JHexViewer hexViewer;

    /**
     * Creates a new instance which paints the byte-area.
     *
     * @param hexViewer the {@link JHexViewer} to which the area belongs. Required to query additional properties of the {@link JHexViewer}.
     * @param area      the area to be painted by this instance.
     *                  This area should be the same which calls {@link #paint(Graphics2D)}
     *                  on this painter.
     */
    public ByteAreaPainter(@NotNull final JHexViewer hexViewer, @NotNull final ByteArea area)
    {
        super(area);
        this.hexViewer = hexViewer;
        setForegroundPainter(new ByteRowForegroundPainter(hexViewer, area));
    }

    @Override
    public void paint(@NotNull final Graphics2D g)
    {
        paintBackground(g);
        paintMiddleground(g);
        paintForeground(g);
    }

    /**
     * Paints the middleground of the area.
     * <p/>
     * The caret, selection and the highlights are painted between the background and the foreground.
     *
     * @param g the Graphics2D object of the area to paint into.
     */
    protected void paintMiddleground(@NotNull final Graphics2D g)
    {
        hexViewer.getHighlighter().ifPresent(highlighter -> highlighter.paint(g, area));
        hexViewer.getCaret().ifPresent(caret -> caret.paint(g, area));
    }
}
