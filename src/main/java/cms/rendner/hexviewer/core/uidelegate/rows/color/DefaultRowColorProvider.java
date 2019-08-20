package cms.rendner.hexviewer.core.uidelegate.rows.color;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.core.view.color.AbstractRowColorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author rendner
 */
public final class DefaultRowColorProvider extends AbstractRowColorProvider
{
    private final Color gray = new Color(0xF4F4F4);

    @Nullable
    @Override
    public Color getRowBackground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex)
    {
        return (rowIndex & 1) == 0 ? gray : Color.white;
    }

    @Nullable
    @Override
    public Color getRowElementBackground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex, final int elementIndex)
    {
        return null;
    }

    @Nullable
    @Override
    public Color getRowElementForeground(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final int rowIndex, final int elementIndex)
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
