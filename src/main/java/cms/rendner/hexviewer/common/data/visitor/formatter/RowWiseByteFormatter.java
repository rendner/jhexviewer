package cms.rendner.hexviewer.common.data.visitor.formatter;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.data.formatter.bytes.AsciiByteFormatter;
import cms.rendner.hexviewer.common.data.formatter.bytes.HexByteFormatter;
import cms.rendner.hexviewer.common.data.formatter.offset.OffsetFormatter;
import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Allows a full customization of how a row (including all three areas of the {@link JHexViewer}
 * ​​should be formatted to get a printable representation.
 *
 * @author rendner
 */
public final class RowWiseByteFormatter implements IRowWiseByteFormatter
{
    /**
     * Used to format the values of the offset-area.
     */
    @NotNull
    private final IValueFormatter offsetFormatter;

    /**
     * Used to format the byte values of the hex-area.
     */
    @NotNull
    private final IValueFormatter hexByteFormatter;

    /**
     * Used to format the byte values of the ascii-area.
     */
    @NotNull
    private final IValueFormatter asciiByteFormatter;

    /**
     * Used to separate rows.
     */
    @NotNull
    private final String rowSeparator = System.lineSeparator();

    /**
     * Used to separate areas.
     */
    private final String areaSeparator = " | ";

    /**
     * Creates a new instance which uses the following value formatter:
     * <ul>
     *     <li>offset-area: {@link OffsetFormatter}</li>
     *     <li>hex-area: {@link HexByteFormatter}</li>
     *     <li>ascii-area: {@link AsciiByteFormatter}</li>
     * </ul>
     */
    public RowWiseByteFormatter()
    {
        this(null, null, null);
    }

    /**
     * Creates a new instance.
     * <p/>
     * In case that any of the applied formatter is <code>null</code>, the following formatters will be used:
     * <ul>
     *     <li>offset-area: {@link OffsetFormatter}</li>
     *     <li>hex-area: {@link HexByteFormatter}</li>
     *     <li>ascii-area: {@link AsciiByteFormatter}</li>
     * </ul>
     *
     * @param offsetFormatter    formatter to format the values of the offset-area.
     * @param hexByteFormatter   formatter to format the values of the hex-area.
     * @param asciiByteFormatter formatter to format the values of the ascii-area.
     */
    public RowWiseByteFormatter(@Nullable final IValueFormatter offsetFormatter,
                                @Nullable final IValueFormatter hexByteFormatter,
                                @Nullable final IValueFormatter asciiByteFormatter)
    {
        super();

        this.offsetFormatter = offsetFormatter == null ? new OffsetFormatter(true, "h") : offsetFormatter;
        this.hexByteFormatter = hexByteFormatter == null ? new HexByteFormatter() : hexByteFormatter;
        this.asciiByteFormatter = asciiByteFormatter == null ? new AsciiByteFormatter() : asciiByteFormatter;
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

        return isMultipleOf(indexOfByteInRow, 8) ? "  " : " ";
    }

    @NotNull
    @Override
    public String asciiByteSeparator(final int indexOfByteInRow)
    {
        return "";
    }

    @NotNull
    @Override
    public String hexBytePlaceholder(final int indexInRow)
    {
        return "  ";
    }

    @NotNull
    @Override
    public String asciiBytePlaceholder(final int indexInRow)
    {
        return ".";
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

    private boolean isMultipleOf(final int multipleToCheck, final int value)
    {
        return (multipleToCheck % value) == 0;
    }
}
