package cms.rendner.hexviewer.support.data.formatter;

import cms.rendner.hexviewer.core.formatter.offset.IOffsetValueFormatter;
import cms.rendner.hexviewer.core.formatter.IValueFormatter;
import cms.rendner.hexviewer.utils.CheckUtils;

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
    protected final IOffsetValueFormatter offsetFormatter;

    /**
     * Used to format the byte values of the hex-area.
     */
    protected final IValueFormatter hexByteFormatter;

    /**
     * Used to format the byte values of the ascii-area.
     */
    protected final IValueFormatter asciiByteFormatter;

    /**
     * Used to separate rows.
     */
    protected String rowSeparator = System.lineSeparator();

    /**
     * Used to separate areas.
     */
    protected String areaSeparator = " | ";

    /**
     * Used to separate bytes of the hex-area.
     */
    protected String hexByteSeparator = " ";

    // todo add comment
    protected String hexByteDoubleSeparator = "  ";

    /**
     * Used to separate bytes of the ascii-area.
     */
    protected String asciiByteSeparator = null;

    /**
     * Used to print an excluded byte of the hex-area.
     */
    protected String hexBytePlaceholder = "  ";

    /**
     * Used to print an excluded byte of the ascii-area.
     */
    protected String asciiBytePlaceholder = ".";

    /**
     * Creates a new instance.
     *
     * @param offsetFormatter    formatter to format the values of the offset-area. Can't be <code>null</code>.
     * @param hexByteFormatter   formatter to format the values of the hex-area. Can't be <code>null</code>.
     * @param asciiByteFormatter formatter to format the values of the ascii-area. Can't be <code>null</code>.
     */
    public RowWiseByteFormatter(final IOffsetValueFormatter offsetFormatter, final IValueFormatter hexByteFormatter, final IValueFormatter asciiByteFormatter)
    {
        super();

        CheckUtils.checkNotNull(offsetFormatter);
        CheckUtils.checkNotNull(hexByteFormatter);
        CheckUtils.checkNotNull(asciiByteFormatter);

        this.offsetFormatter = offsetFormatter;
        this.hexByteFormatter = hexByteFormatter;
        this.asciiByteFormatter = asciiByteFormatter;
    }

    @Override
    public String rowSeparator()
    {
        return rowSeparator;
    }

    @Override
    public String formatAsciiByte(final int value)
    {
        return asciiByteFormatter.format(value);
    }

    @Override
    public String formatHexByte(final int value)
    {
        return hexByteFormatter.format(value);
    }

    @Override
    public String formatRowOffset(final int rowIndex, final int byteOffset)
    {
        return offsetFormatter.format(byteOffset);
    }

    @Override
    public String hexByteSeparator(final int indexOfByteInRow)
    {
        if (indexOfByteInRow == 0)
        {
            return "";
        }

        return isMultipleOf(indexOfByteInRow, 8) ? hexByteDoubleSeparator : hexByteSeparator;
    }

    @Override
    public String asciiByteSeparator(final int indexOfByteInRow)
    {
        if (indexOfByteInRow == 0)
        {
            return "";
        }

        return asciiByteSeparator;
    }

    @Override
    public String hexBytePlaceholder(final int indexInRow)
    {
        return hexBytePlaceholder;
    }

    @Override
    public String asciiBytePlaceholder(final int indexInRow)
    {
        return asciiBytePlaceholder;
    }

    @Override
    public String offsetHexSeparator()
    {
        return areaSeparator;
    }

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
