package cms.rendner.hexviewer.core.uidelegate.rows.renderer;

import cms.rendner.hexviewer.core.model.row.template.IRowTemplate;
import cms.rendner.hexviewer.core.uidelegate.rows.renderer.context.IRendererContext;

import java.awt.*;

/**
 * Interfaces for renderer which paint foreground and background of a row.
 * Each row which should be displayed on the screen is forward to an implementation of this interface.
 * The renderer is responsible for painting all parts of the row e.g displayed bytes/text.
 * The renderer doesn't have to paint one of these following things:
 * <pre>
 *     <ul>
 *         <li>the caret</li>
 *         <li>the selection</li>
 *         <li>highlighted ranges of bytes</li>
 *     </ul>
 * </pre>
 *
 * @author rendner
 */
public interface IRowRenderer<T extends IRowTemplate>
{
    /**
     * Is called whenever the background of a row should be painted. In this method only the background should be painted.
     * Painting parts of the foreground inside this method would result in an unexpected visually result because the
     * selection and highlights are painted between the background and foreground.
     * <p/>
     * It is guaranteed that the paintForeground-method is always called after the paintBackground-method.
     *
     * @param g           the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     *                    This object can be completely changed and does not need to be restored to its original state.
     * @param rowTemplate the layout of the row to be painted.
     * @param context     the context to provide additional information.
     */
    void paintBackground(final Graphics g, final T rowTemplate, final IRendererContext context);

    /**
     * Is called whenever the foreground of a row should be painted. In this method only the foreground should be painted.
     * Painting parts of the background inside this method would result in an unexpected visually result because the
     * selection and highlights are painted between the background and foreground.
     * <p/>
     * It is guaranteed that the paintBackground-method is always called before the paintForeground-method.
     *
     * @param g           the Graphics object in which to paint. The bounds of the Graphics object matches with the bounds of the row to paint.
     *                    This object can be completely changed and does not need to be restored to its original state.
     * @param rowTemplate the layout of the row to be painted.
     * @param context     the context to provide additional information.
     */
    void paintForeground(final Graphics g, final T rowTemplate, final IRendererContext context);
}
