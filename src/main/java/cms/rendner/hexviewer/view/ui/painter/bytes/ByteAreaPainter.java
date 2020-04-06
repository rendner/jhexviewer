package cms.rendner.hexviewer.view.ui.painter.bytes;

import cms.rendner.hexviewer.view.components.areas.bytes.painter.middleground.DefaultMiddlegroundPainter;
import cms.rendner.hexviewer.view.components.areas.common.painter.BasicAreaPainter;

/**
 * Paints the byte-area to which the painter belongs.
 * <p/>
 * The paint request is also forwarded to the {@link cms.rendner.hexviewer.view.components.caret.ICaret ICaret}
 * and {@link cms.rendner.hexviewer.view.components.highlighter.IHighlighter IHighlighter} to paint themselves. These
 * objects are painted between the background and foreground of the area.
 * <p/>
 * To allow customizing of the paint process this class uses separated painters which can be exchanged during runtime.
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
        setMiddlegroundPainter(new DefaultMiddlegroundPainter());
    }
}
