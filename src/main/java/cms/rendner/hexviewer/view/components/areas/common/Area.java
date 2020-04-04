package cms.rendner.hexviewer.view.components.areas.common;

import cms.rendner.hexviewer.common.rowtemplate.IRowTemplate;
import cms.rendner.hexviewer.common.utils.CheckUtils;
import cms.rendner.hexviewer.view.components.areas.common.model.colors.IAreaColorProvider;
import cms.rendner.hexviewer.view.components.areas.common.painter.IAreaPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract component which displays content row-wise.
 * <p/>
 * An area is rendered by an {@link IAreaPainter} which has to be present to render the content.
 *
 * @param <T> the row-template describing the layout of the rows displayed by the area.
 * @param <P> the color provider used by several other classes to allow customizing of the used colors.
 * @author rendner
 */
public abstract class Area<
        T extends IRowTemplate,
        P extends IAreaColorProvider
        > extends AreaComponent
{
    /**
     * Constant used to determine when the <code>painter</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_PAINTER = "painter";

    /**
     * Constant used to determine when the <code>colorProvider</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_COLOR_PROVIDER = "colorProvider";

    /**
     * Constant used to determine when the <code>rowTemplate</code> property has changed.
     * Note that either value (old and new) can also be null.
     */
    @NotNull
    public static final String PROPERTY_ROW_TEMPLATE = "rowTemplate";

    /**
     * Constant used to determine when the <code>rowCount</code> property has changed.
     */
    @NotNull
    public static final String PROPERTY_ROW_COUNT = "rowCount";

    /**
     * Default row height which is used when no rowTemplate is available.
     */
    private final static int DEFAULT_ROW_HEIGHT = 25;

    /**
     * Default row width which is used when no rowTemplate is available.
     */
    private final static int DEFAULT_ROW_WIDTH = 100;

    /**
     * describes the layout of the rows of the area.
     */
    @Nullable
    private T rowTemplate;

    /**
     * Used by the painter and several other components to allow customizing of the used colors.
     */
    @Nullable
    private P colorProvider;

    /**
     * Is used to paint the whole area component.
     */
    @Nullable
    private IAreaPainter painter;

    /**
     * The number of rows, displayed by this component.
     */
    private int rowCount;

    /**
     * Creates a new instance with the provided values.
     *
     * @param areaId the id of the area.
     */
    protected Area(@NotNull final AreaId areaId)
    {
        super(areaId);
    }

    /**
     * Returns the height of a single row.
     * <p/>
     * The height depends on the currently applied <code>rowTemplate</code>. If no <code>rowTemplate</code> is set
     * a default height of {@link Area#DEFAULT_ROW_HEIGHT} is returned.
     *
     * @return the height of a single row.
     */
    @Override
    public final int getRowHeight()
    {
        return rowTemplate == null ? DEFAULT_ROW_HEIGHT : rowTemplate.height();
    }

    /**
     * Returns the width of a single row.
     * <p/>
     * The width depends on the currently applied <code>rowTemplate</code>. If no <code>rowTemplate</code> is set
     * a default width of {@link Area#DEFAULT_ROW_WIDTH} is returned.
     *
     * @return the width of a single row.
     */
    @Override
    public final int getRowWidth()
    {
        return rowTemplate == null ? DEFAULT_ROW_WIDTH : rowTemplate.width();
    }

    @Override
    public int getRowCount()
    {
        return rowCount;
    }

    /**
     * Sets the number of rows of displayable content, &gt;= 1.
     * <p/>
     * Setting a new row count results in a revalidate and repaint of the component.
     * <p/>
     * A PropertyChange event {@link Area#PROPERTY_ROW_COUNT} is fired when a new row count is set.
     *
     * @param rowCount number of displayable rows.
     */
    public void setRowCount(final int rowCount)
    {
        CheckUtils.checkMinValue(rowCount, 1);
        if (this.rowCount != rowCount)
        {
            final int oldValue = this.rowCount;
            this.rowCount = rowCount;
            firePropertyChange(PROPERTY_ROW_COUNT, oldValue, this.rowCount);
            invalidate();
            revalidate();
            repaint();
        }
    }

    /**
     * Sets the new color provider of the area.
     * A color provider allows to exchange the colors used during the paint process.
     * <p/>
     * Setting a new provider results in a repaint of the component.
     * <p/>
     * A PropertyChange event {@link Area#PROPERTY_COLOR_PROVIDER} is fired when a new color provider is set.
     *
     * @param colorProvider the new color provider, passing <code>null</code> forces the installed ui-delegate to use
     *                      default colors for rendering the content of the area.
     */
    public void setColorProvider(@Nullable final P colorProvider)
    {
        if (this.colorProvider != colorProvider)
        {
            final P oldValue = this.colorProvider;
            this.colorProvider = colorProvider;
            firePropertyChange(PROPERTY_COLOR_PROVIDER, oldValue, this.colorProvider);
            repaint();
        }
    }

    /**
     * Returns the color provider of the area.
     *
     * @return the color provider used to color the rendered content of the area.
     */
    @Nullable
    public P getColorProvider()
    {
        return colorProvider;
    }

    @Override
    @Nullable
    public IAreaPainter getPainter()
    {
        return painter;
    }

    /**
     * Sets the new painter which is responsible for painting the content of the area component.
     * <p/>
     * Setting a new painter results in a repaint of the component.
     * <p/>
     * A PropertyChange event {@link Area#PROPERTY_PAINTER} is fired when a new painter is set.
     *
     * @param painter the new painter, if <code>null</code> no content can be drawn.
     */
    public void setPainter(@Nullable final IAreaPainter painter)
    {
        if (this.painter != painter)
        {
            final IAreaPainter oldValue = this.painter;
            this.painter = painter;
            firePropertyChange(PROPERTY_PAINTER, oldValue, this.painter);
            repaint();
        }
    }

    /**
     * Sets the new row-template.
     * The template describes the layout of the rows to paint.
     *
     * @param rowTemplate the new template, setting <code>null</code> will result in an empty area.
     */
    public void setRowTemplate(@Nullable final T rowTemplate)
    {
        if (this.rowTemplate != rowTemplate)
        {
            final T oldValue = this.rowTemplate;
            this.rowTemplate = rowTemplate;
            firePropertyChange(PROPERTY_ROW_TEMPLATE, oldValue, this.rowTemplate);
            revalidate();
            repaint();
        }
    }

    /**
     * Returns the row-template that describes the layout of the rows of the area.
     *
     * @return the row-template for the area.
     */
    @Nullable
    public T getRowTemplate()
    {
        return rowTemplate;
    }
}
