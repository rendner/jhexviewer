package cms.rendner.hexviewer.common.utils;

/**
 * Utility to check ascii chars.
 *
 * @author rendner
 */
public final class AsciiUtils
{
    public static final int NULL = 0x00;
    public static final int HORIZONTAL_TAB = 0x09;
    public static final int NEWLINE = 0x0A;
    public static final int VERTICAL_TAB = 0x0B;
    public static final int FEED = 0x0C;
    public static final int CARRIAGE_RETURN = 0x0D;
    public static final int SPACE = 0x20;

    /**
     * Checks whether c is any ASCII character.
     *
     * @param c character to be checked.
     * @return <code>true</code> if indeed c is an ASCII character, <code>false</code> otherwise.
     */
    public static boolean isAscii(final int c)
    {
        return c >= 0 && c < 0x7F;
    }

    /**
     * Checks whether c is a printable character.
     * <p/>
     * A printable character is a character that occupies a printing position on a display. Printing characters are all
     * with an ASCII code &gt=; 0x20 AND &lt; 0x7F.
     *
     * @param c character to be checked.
     * @return <code>true</code> if indeed c is a printable character, <code>false</code> otherwise.
     */
    public static boolean isPrintable(final int c)
    {
        return c >= 0x20 && c < 0x7F;
    }

    /**
     * Checks whether c is a white-space character
     *
     * @param c character to be checked.
     * @return <code>true</code> if indeed c is a white-space character, <code>false</code> otherwise.
     */
    public static boolean isWhitespace(final int c)
    {
        return c == SPACE ||
                c == HORIZONTAL_TAB ||
                c == NEWLINE ||
                c == VERTICAL_TAB ||
                c == FEED ||
                c == CARRIAGE_RETURN;
    }

    /**
     * Checks whether c is a character with graphical representation.
     *
     * @param c character to be checked.
     * @return <code>true</code> if indeed c has a graphical representation as character, <code>false</code> otherwise.
     */
    public static boolean isGraphic(final int c)
    {
        return c >= 0x21 && c < 0x7F;
    }

    /**
     * Hide constructor.
     */
    private AsciiUtils()
    {
    }
}
