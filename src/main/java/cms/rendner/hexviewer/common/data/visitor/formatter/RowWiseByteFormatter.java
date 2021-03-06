package cms.rendner.hexviewer.common.data.visitor.formatter;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.data.formatter.bytes.AsciiByteFormatter;
import cms.rendner.hexviewer.common.data.formatter.bytes.HexByteFormatter;
import cms.rendner.hexviewer.common.data.formatter.offset.IOffsetFormatter;
import cms.rendner.hexviewer.common.data.formatter.offset.OffsetFormatter;
import cms.rendner.hexviewer.common.utils.IndexUtils;
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
    private final IOffsetFormatter offsetFormatter;

    /**
     * Used to format the byte values of the hex-area.
     */
    @NotNull
    private final IValueFormatter hexByteFormatter;

    /**
     * Used to format the byte values of the text-area.
     */
    @NotNull
    private final IValueFormatter textByteFormatter;

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
     * Indicates if a separator should be used to separate multiples of 8 bytes.
     */
    private final boolean useMultipleOf8Separator;

    /**
     * Creates a new instance which uses the following value formatter:
     * <ul>
     *     <li>offset-area: {@link OffsetFormatter}</li>
     *     <li>hex-area: {@link HexByteFormatter}</li>
     *     <li>text-area: {@link AsciiByteFormatter}</li>
     * </ul>
     *
     * @param bytesPerRow number of bytes per row.
     */
    public RowWiseByteFormatter(final int bytesPerRow)
    {
        this(bytesPerRow, null, null, null);
    }

    /**
     * Creates a new instance.
     * <p/>
     * In case that any of the applied formatter is <code>null</code>, the following formatters will be used:
     * <ul>
     *     <li>offset-area: {@link OffsetFormatter}</li>
     *     <li>hex-area: {@link HexByteFormatter}</li>
     *     <li>text-area: {@link AsciiByteFormatter}</li>
     * </ul>
     *
     * @param bytesPerRow        number of bytes per row.
     * @param offsetFormatter    formatter to format the values of the offset-area.
     * @param hexByteFormatter   formatter to format the values of the hex-area.
     * @param textByteFormatter formatter to format the values of the text-area.
     */
    public RowWiseByteFormatter(final int bytesPerRow,
                                @Nullable final IOffsetFormatter offsetFormatter,
                                @Nullable final IValueFormatter hexByteFormatter,
                                @Nullable final IValueFormatter textByteFormatter)
    {
        super();

        this.offsetFormatter = offsetFormatter == null ? new OffsetFormatter(true, "h") : offsetFormatter;
        this.hexByteFormatter = hexByteFormatter == null ? new HexByteFormatter() : hexByteFormatter;
        this.textByteFormatter = textByteFormatter == null ? new AsciiByteFormatter() : textByteFormatter;

        this.useMultipleOf8Separator = IndexUtils.isMultipleOf(bytesPerRow, 8);
    }

    @NotNull
    @Override
    public String rowSeparator()
    {
        return rowSeparator;
    }

    @NotNull
    @Override
    public String formatTextByte(final int value)
    {
        return textByteFormatter.format(value);
    }

    @NotNull
    @Override
    public String formatHexByte(final int value)
    {
        return hexByteFormatter.format(value);
    }

    @NotNull
    @Override
    public String formatRowOffset(final int rowIndex, final long byteOffset)
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

        if (useMultipleOf8Separator && IndexUtils.isMultipleOf(indexOfByteInRow, 8))
        {
            return " ┊ ";
        }

        return " ";
    }

    @NotNull
    @Override
    public String textByteSeparator(final int indexOfByteInRow)
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
    public String textBytePlaceholder(final int indexInRow)
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
    public String hexTextSeparator()
    {
        return areaSeparator;
    }
}
