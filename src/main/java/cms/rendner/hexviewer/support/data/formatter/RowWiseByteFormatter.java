package cms.rendner.hexviewer.support.data.formatter;

import cms.rendner.hexviewer.core.formatter.IValueFormatter;
import cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * Allows a full customization of how a row (including all three areas of the {@link cms.rendner.hexviewer.core.JHexViewer}
 * ​​should be formatted to get a printable representation.
 *
 * @author rendner
 */
public class RowWiseByteFormatter implements IRowWiseByteFormatter
{
    /**
     * Used to format the values of the offset-area.
     */
    @NotNull
    protected final IOffsetValueFormatter offsetFormatter;

    /**
     * Used to format the byte values of the hex-area.
     */
    @NotNull
    protected final IValueFormatter hexByteFormatter;

    /**
     * Used to format the byte values of the ascii-area.
     */
    @NotNull
    protected final IValueFormatter asciiByteFormatter;

    /**
     * Used to separate rows.
     */
    @NotNull
    protected String rowSeparator = System.lineSeparator();

    /**
     * Used to separate areas.
     */
    protected String areaSeparator = " | ";

    /**
     * Used to separate bytes of the hex-area.
     */
    @NotNull
    protected String hexByteSeparator = " ";

    // todo add comment
    @NotNull
    protected String hexByteDoubleSeparator = "  ";

    /**
     * Used to separate bytes of the ascii-area.
     */
    @NotNull
    protected String asciiByteSeparator = "";

    /**
     * Used to print an excluded byte of the hex-area.
     */
    @NotNull
    protected String hexBytePlaceholder = "  ";

    /**
     * Used to print an excluded byte of the ascii-area.
     */
    @NotNull
    protected String asciiBytePlaceholder = ".";

    /**
     * Creates a new instance.
     *
     * @param offsetFormatter    formatter to format the values of the offset-area.
     * @param hexByteFormatter   formatter to format the values of the hex-area.
     * @param asciiByteFormatter formatter to format the values of the ascii-area.
     */
    public RowWiseByteFormatter(@NotNull final IOffsetValueFormatter offsetFormatter,
                                @NotNull final IValueFormatter hexByteFormatter,
                                @NotNull final IValueFormatter asciiByteFormatter)
    {
        super();

        this.offsetFormatter = offsetFormatter;
        this.hexByteFormatter = hexByteFormatter;
        this.asciiByteFormatter = asciiByteFormatter;
    }

    @NotNull
    @Override
    public String rowSeparator()
    {
        return rowSeparator;
    }

    @NotNull
    @Override
    public String formatAsciiByte(final int value)
    {
        return asciiByteFormatter.format(value);
    }

    @NotNull
    @Override
    public String formatHexByte(final int value)
    {
        return hexByteFormatter.format(value);
    }

    @NotNull
    @Override
    public String formatRowOffset(final int rowIndex, final int byteOffset)
    {
        return offsetFormatter.format(byteOffset);
    }

    @NotNull
    @Override
    public String hexByteSeparator(final int indexOfByteInRow)
    {
        if (indexOfByteInRow == 0)
        {
            return "";
        }

        return isMultipleOf(indexOfByteInRow, 8) ? hexByteDoubleSeparator : hexByteSeparator;
    }

    @NotNull
    @Override
    public String asciiByteSeparator(final int indexOfByteInRow)
    {
        if (indexOfByteInRow == 0)
        {
            return "";
        }

        return asciiByteSeparator;
    }

    @NotNull
    @Override
    public String hexBytePlaceholder(final int indexInRow)
    {
        return hexBytePlaceholder;
    }

    @NotNull
    @Override
    public String asciiBytePlaceholder(final int indexInRow)
    {
        return asciiBytePlaceholder;
    }

    @NotNull
    @Override
    public String offsetHexSeparator()
    {
        return areaSeparator;
    }

    @NotNull
    @Override
    public String hexAsciiSeparator()
    {
        return areaSeparator;
    }

    // todo add comment
    protected boolean isMultipleOf(final int multipleToCheck, final int value)
    {
        return (multipleToCheck % value) == 0;
    }
}
