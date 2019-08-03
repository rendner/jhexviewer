package cms.rendner.hexviewer.core.formatter.lookup;

import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

/**
 * Generates lookup tables with precalculated/converted strings.
 * These tables can be used to render the bytes in another format (hex, ascii, unicode, etc.).
 * Each entry of the tables contain a formatted string mapped by the value represented by the formatted string.
 *
 * @author rendner
 */
public class LookupTableFactory
{
    /**
     * The dot.
     */
    @NotNull
    private static final String DOT = ".";

    /**
     * The lookup table filled with hex values if created.
     */
    private static ILookupTable hexValueTable;

    /**
     * The lookup table filled with ascii values if created.
     */
    private static ILookupTable asciiValueTable;

    /**
     * The lookup table filled with unicode values if created.
     */
    private static ILookupTable unicodeValueTable;

    /**
     * Creates a hex lookup table with 256 entries for the byte values '0x00' - '0xFF'.
     */
    @NotNull
    public synchronized static ILookupTable createHexTable()
    {
        if (hexValueTable == null)
        {
            hexValueTable = generateHexTable();
        }

        return hexValueTable;
    }

    /**
     * Creates an ASCII lookup table with 256 entries for the byte values '0x00' - '0xFF'.
     * <p/>
     * ASCII defines 128 characters, which map to the numbers 0–127.
     * If an ASCII char in this range isn't printable a "." is used instead.
     */
    @NotNull
    public synchronized static ILookupTable createAsciiTable()
    {
        if (asciiValueTable == null)
        {
            asciiValueTable = generateAsciiTable();
        }

        return asciiValueTable;
    }

    /**
     * Creates an unicode lookup table with 256 entries for the byte values '0x00' - '0xFF'.
     * <p/>
     * Unicode is a superset of ASCII, and the numbers 0–128 have the same meaning in ASCII as they have in Unicode.
     * If an unicode char in this range isn't printable a "." is used instead.
     */
    @NotNull
    public synchronized static ILookupTable createUnicodeTable()
    {
        if (unicodeValueTable == null)
        {
            unicodeValueTable = generateUnicodeTable();
        }

        return unicodeValueTable;
    }

    /**
     * Creates a hex lookup table with 256 entries for the byte values '0x00' - '0xFF'.
     */
    @NotNull
    private static ILookupTable generateHexTable()
    {
        final String[] result = new String[0xFF + 1];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = String.format("%02X", i);
        }

        return new LookupTable(result, result.length - 1);
    }

    /**
     * Creates an unicode lookup table with 256 entries for the byte values '0x00' - '0xFF'.
     */
    @NotNull
    private static ILookupTable generateUnicodeTable()
    {
        final String[] result = new String[0xFF + 1];

        char c;
        for (int i = 0; i < result.length; i++)
        {
            c = (char) i;
            if (isPrintableUnicodeChar(c))
            {
                result[i] = String.valueOf(c);
            }
            else
            {
                result[i] = DOT;
            }
        }

        return new LookupTable(result, result.length - 1);
    }

    /**
     * Creates an ASCII lookup table with 256 entries for the byte values '0x00' - '0xFF'.
     */
    @NotNull
    private static ILookupTable generateAsciiTable()
    {
        final String[] result = new String[0xFF + 1];

        char c;
        for (int i = 0; i < result.length; i++)
        {
            c = (char) i;
            if (isPrintableAsciiChar(c))
            {
                result[i] = String.valueOf(c);
            }
            else
            {
                result[i] = DOT;
            }
        }

        return new LookupTable(result, result.length - 1);
    }

    private static boolean isPrintableUnicodeChar(final char c)
    {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    private static boolean isPrintableAsciiChar(final char c)
    {
        return c >= 0x20 && c < 0x7F;
    }
}
