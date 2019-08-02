package cms.rendner.hexviewer.core.model.row.template.configuration.values;

import java.util.Objects;

/**
 * This value depends on the used font and font size (typographically unit 'em').
 * The real value is calculated at runtime by using the current installed font of the JHexViewer.
 *
 * @author rendner
 */
public final class EMValue implements IResolvableValue
{
    /**
     * Used to calculate the final value.
     */
    private final double multiplier;

    /**
     * Creates a instance with a default multiplier of <code>1</code>.
     */
    public EMValue()
    {
        this(1.0d);
    }

    /**
     * Creates a new instance with the provided multiplier.
     *
     * @param multiplier used to calculate the final value.
     */
    public EMValue(final double multiplier)
    {
        super();

        this.multiplier = multiplier;
    }

    /**
     * Calculates the final value depending on the font size of the <code>JHexViewer</code>.
     *
     * @param fontSize the size of the <code>JHexViewer</code> font.
     * @return the calculated value.
     */
    @Override
    public double resolve(final double fontSize)
    {
        return fontSize * multiplier;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof EMValue))
        {
            return false;
        }
        EMValue emValue = (EMValue) o;
        return Double.compare(emValue.multiplier, multiplier) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(multiplier);
    }
}
