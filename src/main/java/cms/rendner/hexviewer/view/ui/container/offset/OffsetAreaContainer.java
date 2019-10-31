package cms.rendner.hexviewer.view.ui.container.offset;

import cms.rendner.hexviewer.view.components.areas.offset.OffsetArea;
import cms.rendner.hexviewer.view.ui.container.common.BaseAreaContainer;
import cms.rendner.hexviewer.view.ui.container.common.ScrollDirection;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This container wraps the offset-area of the {@link cms.rendner.hexviewer.view.JHexViewer} and adds
 * scroll support for this area.
 *
 * @author rendner
 */
public final class OffsetAreaContainer extends BaseAreaContainer
{
    /**
     * The offset-area component of the {@link cms.rendner.hexviewer.view.JHexViewer}.
     */
    @NotNull
    private final OffsetArea offsetArea;

    /**
     * Creates a new instance.
     *
     * @param offsetArea the offset-area component of the {@link cms.rendner.hexviewer.view.JHexViewer}.
     */
    public OffsetAreaContainer(@NotNull final OffsetArea offsetArea)
    {
        super();
        this.offsetArea = offsetArea;
        setLayout(new BorderLayout());
        add(offsetArea, BorderLayout.LINE_START);
    }

    @Override
    protected int computeHorizontalUnitIncrement(@NotNull final Rectangle visibleRect,
                                                 @MagicConstant(flags = {ScrollDirection.LEFT, ScrollDirection.RIGHT}) final int direction)
    {
        // disable horizontal scrolling
        return 0;
    }

    @Override
    public int rowHeight()
    {
        return offsetArea.getRowHeight();
    }
}
