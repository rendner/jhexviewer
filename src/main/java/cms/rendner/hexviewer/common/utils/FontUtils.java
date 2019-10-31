package cms.rendner.hexviewer.common.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Utility for providing a monospaced font.
 *
 * @author rendner
 */
public final class FontUtils
{
    /**
     * Checks for the font <code>Courier New</code> if not available
     * the String constant for the family name of the logical font "Monospaced"
     * (<code>Font.MONOSPACED</code>) is returned.
     *
     * @return a monospaced font name.
     */
    @NotNull
    public static String getMonospacedFontName()
    {
        final String courierNew = "Courier New";
        final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Font[] fonts = localGraphicsEnvironment.getAllFonts();
        for (final Font font : fonts)
        {
            if (font.getName().equals(courierNew))
            {
                return courierNew;
            }
        }
        return Font.MONOSPACED;
    }

    /**
     * Hide constructor.
     */
    private FontUtils()
    {
    }
}
