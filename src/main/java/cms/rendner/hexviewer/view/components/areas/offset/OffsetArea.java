package cms.rendner.hexviewer.view.components.areas.offset;

import cms.rendner.hexviewer.common.data.formatter.offset.IOffsetFormatter;
import cms.rendner.hexviewer.common.data.formatter.offset.OffsetFormatter;
import cms.rendner.hexviewer.common.rowtemplate.offset.IOffsetRowTemplate;
import cms.rendner.hexviewer.view.components.areas.common.Area;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
import cms.rendner.hexviewer.view.components.areas.offset.model.colors.IOffsetColorProvider;

/**
 * Component which displays the offset addresses of the current visible rows.
 *
 * @author rendner
 */
public final class OffsetArea extends Area<IOffsetRowTemplate, IOffsetColorProvider, IOffsetFormatter>
{
    public OffsetArea()
    {
        super(AreaId.OFFSET, new OffsetFormatter(true, "h"));
    }
}
