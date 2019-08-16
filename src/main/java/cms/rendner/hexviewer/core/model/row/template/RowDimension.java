package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.utils.CheckUtils;

/**
 * The dimension of a row.
 *
 * @author rendner
 */
public class RowDimension implements IRowTemplate.IDimension
{
    /**
     * The width.
     */
    private final int width;
    /**
     * The height.
     */
    private final int height;

    /**
     * Creates a new instance.
     *
     * @param width  the width, &gt;= 1.
     * @param height the height, &gt;= 1.
     */
    public RowDimension(final int width, final int height)
    {
        super();

        CheckUtils.checkMinValue(width, 1);
        CheckUtils.checkMinValue(height, 1);

        this.width = width;
        this.height = height;
    }


    @Override
    public int width()
    {
        return width;
    }

    @Override
    public int height()
    {
        return height;
    }
}
