package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Paints the background of an area in one single step.
 * <p/>
 * This class uses the background color provided by the {@link IAreaColorProvider#getBackground()} of the area to paint
 * the background.
 *
 * @param <A> the type of the area which is painted by this class.
 */
public class DefaultBackgroundPainter<A extends Area<?, ?>> implements IAreaBackgroundPainter
{
    /**
     * The area to be painted.
     */
    protected final A area;

    /**
     * Creates a new instance responsible for painting the background of the area.
     * <p>
     * Creating an instance of this class doesn't automatically registers this instance for painting the area background.
     *
     * @param area the area to be painted by this instance.
     */
    public DefaultBackgroundPainter(@NotNull final A area)
    {
        this.area = area;
    }

    @Override
    public void paint(@NotNull final Graphics2D g)
    {
        area.getColorProvider().ifPresent(provider -> {
            g.setColor(provider.getBackground());
            g.fillRect(0, 0, area.getWidth(), area.getHeight());
        });
    }
}
