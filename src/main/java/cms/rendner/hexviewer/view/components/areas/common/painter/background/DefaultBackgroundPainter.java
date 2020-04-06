package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.AreaComponent;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaLayerPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Paints the background of an area in one single step.
 * <p/>
 * This class uses the background color provided by the {@link IAreaColorProvider#getBackground()} of the area model to paint
 * the background.
 */
public class DefaultBackgroundPainter implements IAreaLayerPainter
{
    @Override
    public void paint(@NotNull final Graphics2D g, @NotNull final JHexViewer hexViewer, @NotNull final AreaComponent component)
    {
        final IAreaColorProvider colorProvider = getColorProvider(component);
        if (colorProvider != null)
        {
            g.setColor(colorProvider.getBackground());
            g.fillRect(0, 0, component.getWidth(), component.getHeight());
        }
    }

    @Nullable
    private IAreaColorProvider getColorProvider(@NotNull final AreaComponent component)
    {
        return ((Area<?,?>) component).getColorProvider();
    }
}
