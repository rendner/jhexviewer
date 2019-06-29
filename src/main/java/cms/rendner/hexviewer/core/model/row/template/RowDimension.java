package cms.rendner.hexviewer.core.model.row.template;

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
     * @param width  the width.
     * @param height the height.
     */
    public RowDimension(final int width, final int height)
    {
        super();
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
