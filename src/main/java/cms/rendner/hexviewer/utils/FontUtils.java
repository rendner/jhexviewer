package cms.rendner.hexviewer.utils;

import java.awt.*;

/**
 * Utility for providing a monospaced font.
 *
 * @author rendner
 */
public class FontUtils
{
    /**
     * Checks for the font <code>Courier New</code> if not available
     * the String constant for the family name of the logical font "Monospaced"
     * (<code>Font.MONOSPACED</code>) is returned.
     *
     * @return a monospaced font name.
     */
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
}
