package cms.rendner.hexviewer.model.rowtemplate.configuration.values;

import java.util.Objects;

/**
 * This value depends on the used font and font size (typographically unit 'em').
 * The real value is calculated at runtime by using a font size.
 *
 * @author rendner
 */
public final class EMValue implements IValue
{
    /**
     * The part of the final value, which is multiplied with the font size.
     */
    private final double multiplierPart;
    /**
     * The fixed part of the final value.
     */
    private final double fixedPart;

    /**
     * Creates a new instance with the provided multiplierPart and a default fixedPart of <code>0</code>.
     *
     * @param multiplierPart the part of the final value, which is multiplied with the font size.
     */
    public EMValue(final double multiplierPart)
    {
        this(multiplierPart, 0.0d);
    }

    /**
     * Creates a new instance with the provided multiplier.
     *
     * @param multiplierPart the part of the final value, which is multiplied with the font size.
     * @param fixedExtra     the fixed part of the final value.
     */
    public EMValue(final double multiplierPart, final double fixedExtra)
    {
        super();

        this.multiplierPart = multiplierPart;
        this.fixedPart = fixedExtra;
    }

    /**
     * Calculates the final value depending on the font size of the <code>JHexViewer</code>.
     *
     * @param fontSize the size of the <code>JHexViewer</code> font.
     * @return the calculated value.
     */
    public double resolve(final double fontSize)
    {
        return (fontSize * multiplierPart) + fixedPart;
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
        return Double.compare(emValue.multiplierPart, multiplierPart) == 0 &&
                Double.compare(emValue.fixedPart, fixedPart) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(multiplierPart, fixedPart);
    }
}
