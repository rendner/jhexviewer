package cms.rendner.hexviewer.view.ui.painter;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter;
import cms.rendner.hexviewer.view.ui.areas.IPainterDelegate;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Forwards the {@link JHexViewer} to the {@link cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter areaPainter}
 * of an area-component to have access to {@link JHexViewer} inside the area painter.
 *
 * @author rendner
 */
public final class PainterDelegate implements IPainterDelegate
{
    @NotNull
    private final JHexViewer hexViewer;

    /**
     * Creates a new instance.
     *
     * @param hexViewer the {@link JHexViewer} which should be forwarded to an
     *                  {@link cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter areaPainter}
     */
    public PainterDelegate(@NotNull JHexViewer hexViewer)
    {
        this.hexViewer = hexViewer;
    }

    public void paint(@NotNull final Graphics2D g, @NotNull final AreaComponent component)
    {
        final IAreaPainter p = component.getPainter();
        if (p != null)
        {
            p.paint(g, hexViewer, component);
        }
    }
}
