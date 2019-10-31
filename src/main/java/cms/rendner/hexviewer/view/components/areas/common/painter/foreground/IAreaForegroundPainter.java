package cms.rendner.hexviewer.view.components.areas.common.painter.foreground;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * A foreground painter is responsible for painting the foreground of an area rowwise.
 *
 * @author rendner
 */
public interface IAreaForegroundPainter
{
    /**
     * Called to signal the painter that multiple rows will be repainted.
     * It is guaranteed that this method will always called before starting the paint process.
     * <p/>
     * This method can be used to initialize resource which are used during the repeated calls.
     */
    default void prePaint()
    {
    }

    /**
     * Will be called after {@link IAreaForegroundPainter#prePaint()} to check if the painter has the required
     * resource setup to fulfill the paint requests. If <code>false</code> is returned {@link IAreaForegroundPainter#postPaint()}
     * will be called immediately, otherwise {@link IAreaForegroundPainter#paint(Graphics2D, int)} is called multiple times
     * followed by a call of {@link IAreaForegroundPainter#postPaint()}.
     *
     * @return <code>true</code> when painter can handle the paint requests, <code>false</code> otherwise.
     */
    default boolean canPaint()
    {
        return true;
    }

    /**
     * Is called for each row which has to be repainted.
     *
     * @param g        the Graphics2D object in which to paint. The bounds of the Graphics2D object matches with the bounds
     *                 of the row to paint. This object can be completely changed and does not need to be restored to
     *                 its original state.
     * @param rowIndex the index of the row to paint.
     */
    void paint(@NotNull final Graphics2D g, final int rowIndex);

    /**
     * Called to signal the painter that all current paint requests are fulfilled.
     * It is guaranteed that this method will always called before ending the paint process.
     * <p/>
     * This method can be used to un-initialize resource which were used during the repeated paint calls.
     */
    default void postPaint()
    {
    }
}
