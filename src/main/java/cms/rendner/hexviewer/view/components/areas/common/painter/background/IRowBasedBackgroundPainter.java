package cms.rendner.hexviewer.view.components.areas.common.painter.background;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This kind of background  painter is responsible for painting the background of an area rowwise.
 *
 * @author rendner
 */
public interface IRowBasedBackgroundPainter extends IAreaBackgroundPainter
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
     * Will be called after {@link IRowBasedBackgroundPainter#prePaint()} to check if the painter has the required
     * resource setup to fulfill the paint requests. If <code>false</code> is returned {@link IRowBasedBackgroundPainter#postPaint()}
     * will be called immediately, otherwise {@link IRowBasedBackgroundPainter#paint(Graphics2D, int, boolean)} is called multiple times
     * followed by a call of {@link IRowBasedBackgroundPainter#postPaint()}.
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
     * @param g         the Graphics2D object in which to paint. The bounds of the Graphics2D object matches with the bounds
     *                  of the row to paint. This object can be completely changed and does not need to be restored to
     *                  its original state.
     * @param rowIndex  the index of the row to paint.
     * @param isLastRow <code>true</code> if the row is the last row of the background.
     */
    void paint(@NotNull Graphics2D g, int rowIndex, boolean isLastRow);

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
