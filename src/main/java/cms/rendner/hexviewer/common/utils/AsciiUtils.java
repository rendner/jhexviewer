package cms.rendner.hexviewer.common.utils;

/**
 * Utility to check ascii chars.
 *
 * @author rendner
 */
public class AsciiUtils
{
    public static final char SPACE = 0x20;
    public static final char HORIZONTAL_TAB = 0x09;
    public static final char NEWLINE = 0x0A;
    public static final char VERTICAL_TAB = 0x0B;
    public static final char FEED = 0x0C;
    public static final char CARRIAGE_RETURN = 0x0D;

    /**
     * Checks whether c is a printable character.
     * <p/>
     * A printable character is a character that occupies a printing position on a display. Printing characters are all
     * with an ASCII code &gt=; 0x20 AND &lt; 0x7F.
     *
     * @param c character to be checked.
     * @return <code>true</code> if indeed c is a printable character, <code>false</code> otherwise.
     */
    public static boolean isPrintable(final char c)
    {
        return c >= 0x20 && c < 0x7F;
    }

    /**
     * Checks whether c is a white-space character
     *
     * @param c character to be checked.
     * @return <code>true</code> if indeed c is a white-space character, <code>false</code> otherwise.
     */
    public static boolean isWhitespace(final char c)
    {
        return c == SPACE ||
                c == HORIZONTAL_TAB ||
                c == NEWLINE ||
                c == VERTICAL_TAB ||
                c == FEED ||
                c == CARRIAGE_RETURN;
    }

    /**
     * Hide constructor.
     */
    private AsciiUtils()
    {
    }
}
