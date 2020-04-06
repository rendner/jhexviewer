package cms.rendner.hexviewer.view.components.areas.common.painter;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * The composite layer painter paints a stack of layers. Thus, the order of the layers is important. Opaque parts on a
 * layer higher up the stack will obscure parts contained in layers lower in the stack.
 *
 * @author rendner
 */
public class CompositeLayerPainter implements IAreaLayerPainter
{
    private final IAreaLayerPainter[] painters;

    /**
     * Creates a new composed painter.
     *
     * @param painters list of painters to use. Painters are used in the order as specified (first to last).
     */
    public CompositeLayerPainter(@NotNull IAreaLayerPainter... painters)
    {
        this.painters = painters;
    }

    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        for (final IAreaLayerPainter painter : painters)
        {
            painter.paint(g, hexViewer, component);
        }
    }
}
