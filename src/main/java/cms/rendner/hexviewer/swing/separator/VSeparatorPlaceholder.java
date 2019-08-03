package cms.rendner.hexviewer.swing.separator;

import cms.rendner.hexviewer.swing.BorderlessJComponent;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * An implementation of a lightweight component that participates in layout but has no view.
 * This vertical placeholder allocates extra space for a wrapped separator.
 *
 * @author rendner
 */
public class VSeparatorPlaceholder extends BorderlessJComponent
{
    /**
     * The wrapped separator.
     */
    @Nullable
    private Separator separator;

    /**
     * Creates a new instance.
     */
    public VSeparatorPlaceholder()
    {
        this(null);

    }

    /**
     * Creates a new instance with a specified separator.
     *
     * @param separator
     */
    public VSeparatorPlaceholder(final Separator separator)
    {
        super();
        setSeparator(separator);
    }

    /**
     * @return the wrapped separator.
     */
    @Nullable
    public Separator getSeparator()
    {
        return separator;
    }

    /**
     * Sets the separator to allocate space in the layout for.
     *
     * @param separator the new separator.
     */
    public void setSeparator(@Nullable final Separator separator)
    {
        this.separator = separator;
        reshape();
    }

    /**
     * Does nothing.
     *
     * @param g ignored.
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        // do nothing
    }

    /**
     * Recalculates the required size for the wrapped separator.
     */
    private void reshape()
    {
        final int width = separator != null ? separator.getThickness() : 0;
        setMinimumSize(new Dimension(width, 0));
        setPreferredSize(new Dimension(width, 0));
        setMaximumSize(new Dimension(width, Short.MAX_VALUE));
        revalidate();
    }
}
