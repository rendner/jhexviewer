package cms.rendner.hexviewer.swing;


import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Component which hasn't a border.
 *
 * @author rendner
 */
public class BorderlessJComponent extends JComponent
{
    /**
     * <p>Note:  If <code>border</code> is non-<code>null</code>, this
     * method will throw an exception as borders are not supported on
     * a <code>BorderlessJComponent</code>.
     *
     * @param border the <code>Border</code> to set
     * @throws IllegalArgumentException this method is not implemented
     */
    public final void setBorder(@Nullable final Border border)
    {
        if (border != null)
        {
            throw new IllegalArgumentException("BorderlessJComponent.setBorder() not supported");
        }
    }
}
