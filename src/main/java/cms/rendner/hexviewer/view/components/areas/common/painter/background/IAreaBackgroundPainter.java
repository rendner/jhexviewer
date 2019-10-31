package cms.rendner.hexviewer.view.components.areas.common.painter.background;

/**
 * Marker interface which has to be implemented by all area background painters.
 * A background painter is only responsible for painting the background of an area.
 * <p/>
 * Currently there are the following background painters available which should fulfill the most use cases:
 * <ul>
 *     <li>{@link IFullBackgroundPainter} - which paints the background in one step</li>
 *     <li>{@link IRowBasedBackgroundPainter} - which paints the background rowwise</li>
 * </ul>
 *
 * @author rendner
 */
public interface IAreaBackgroundPainter
{
}
