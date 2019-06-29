package cms.rendner.hexviewer.core.uidelegate.rows.color;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.color.AbstractRowColorProvider;

import java.awt.*;

/**
 * @author rendner
 */
public class DefaultRowColorProvider extends AbstractRowColorProvider
{
    private Color gray = new Color(0xF4F4F4);

    @Override
    public Color getRowBackground(final JHexViewer hexViewer, final AreaId areaId, final int rowIndex)
    {
        return (rowIndex & 1) == 0 ? gray : Color.white;
    }

    @Override
    public Color getRowElementBackground(final JHexViewer hexViewer, final AreaId areaId, final int rowIndex, final int elementIndex)
    {
        return null;
    }

    @Override
    public Color getRowElementForeground(final JHexViewer hexViewer, final AreaId areaId, final int rowIndex, final int elementIndex)
    {
        if (AreaId.OFFSET.equals(areaId))
        {
            if (hexViewer.isShowOffsetCaretIndicator() && isCaretRowIndex(hexViewer, rowIndex))
            {
                return Color.pink;
            }
        }
        return Color.black;
    }
}
