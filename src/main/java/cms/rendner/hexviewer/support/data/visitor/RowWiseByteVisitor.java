package cms.rendner.hexviewer.support.data.visitor;

import cms.rendner.hexviewer.support.data.formatter.IRowWiseByteFormatter;
import cms.rendner.hexviewer.support.data.visitor.consumer.IConsumer;
import cms.rendner.hexviewer.support.data.visitor.consumer.ToConsoleConsumer;
import cms.rendner.hexviewer.support.data.wrapper.RowData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Byte visitor which uses a {@link IRowWiseByteFormatter} and a {@link IConsumer} to forward
 * formatted rows of byte values to a specified <code>consumer</code>.
 *
 * @author rendner
 */
public final class RowWiseByteVisitor implements IRowWiseByteVisitor
{
    /**
     * Used to format visited rows of bytes.
     * The formatted result will be forwarded to the consumer.
     */
    @NotNull
    private final IRowWiseByteFormatter formatter;

    /**
     * Used to consume the formatted value of the visited rows of bytes.
     */
    @NotNull
    private final IConsumer consumer;

    /**
     * The number of bytes in a row.
     */
    private final int bytesPerRow;

    /**
     * Used to build a row which includes the three areas of the
     * {@link cms.rendner.hexviewer.core.JHexViewer}.
     *
     * @see cms.rendner.hexviewer.core.view.areas.AreaId
     */
    private StringBuilder rowBuilder;

    /**
     * Used to build the part of the final row from the bytes displayed
     * in the {@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX}.
     */
    private StringBuilder hexAreaBuilder;

    /**
     * Used to build the part of the final row from the bytes displayed
     * in the {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII}.
     */
    private StringBuilder asciiAreaBuilder;

    /**
     * If <code>true</code> the bytes displayed in the
     * {@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} are
     * included in the final row which is passed to the <code>consumer</code>.
     */
    private boolean includeHexArea;

    /**
     * If <code>true</code> the bytes displayed in the
     * {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII} are
     * included in the final row which is passed to the <code>consumer</code>.
     */
    private boolean includeAsciiArea;

    /**
     * Creates a new instance.
     *
     * @param formatter to format byte values ​​row-based before they are written to the consumer.
     * @param bytesPerRow the number of bytes in a row, &gt;= 0.
     */
    public RowWiseByteVisitor(@NotNull final IRowWiseByteFormatter formatter, final int bytesPerRow)
    {
        this(formatter, null, bytesPerRow);
    }

    /**
     * Creates a new instance.
     *
     * @param consumer  the consumer to write to, if <code>null</code> a ToConsoleConsumer will be used to write to.
     * @param formatter to format byte values ​​row-based before they are written to the consumer. Can't be <code>null</code>.
     * @param bytesPerRow the number of bytes in a row, &gt;= 0.
     */
    public RowWiseByteVisitor(@NotNull final IRowWiseByteFormatter formatter, @Nullable final IConsumer consumer, final int bytesPerRow)
    {
        super();

        this.formatter = formatter;
        this.consumer = consumer == null ? new ToConsoleConsumer() : consumer;
        this.bytesPerRow = bytesPerRow;
    }

    /**
     * Sets if the bytes of the {@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} should be included in the
     * row-based formatted string.
     *
     * @param includeHexArea if bytes of the hex-area should be included or not.
     */
    public void setIncludeHexArea(final boolean includeHexArea)
    {
        this.includeHexArea = includeHexArea;
    }

    /**
     * Sets if the bytes of the {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII} should be included in the
     * row-based formatted string.
     *
     * @param includeAsciiArea if bytes of the ascii-area should be included or not.
     */
    public void setIncludeAsciiArea(final boolean includeAsciiArea)
    {
        this.includeAsciiArea = includeAsciiArea;
    }

    @Override
    public void start()
    {
        consumer.start();
        rowBuilder = new StringBuilder();
        hexAreaBuilder = new StringBuilder();
        asciiAreaBuilder = new StringBuilder();
    }

    @Override
    public void end()
    {
        consumer.end();
        rowBuilder = null;
        hexAreaBuilder = null;
        asciiAreaBuilder = null;
    }

    @Override
    public void visitRow(@NotNull final RowData rowData, final int leadingBytesToIgnore, final int trailingBytesToIgnore)
    {
        appendBytePlaceholder(0, leadingBytesToIgnore);

        int currentByteIndex = leadingBytesToIgnore;

        final int printableBytesSize = Math.max(currentByteIndex, rowData.size() - trailingBytesToIgnore);
        while(currentByteIndex < printableBytesSize )
        {
            appendByte(rowData.getByte(currentByteIndex), currentByteIndex);
            currentByteIndex++;
        }

        final int missingPlaceholders = bytesPerRow - currentByteIndex;
        appendBytePlaceholder(currentByteIndex, missingPlaceholders);

        consumer.consume(buildRow(rowData));

        rowBuilder.setLength(0);
        hexAreaBuilder.setLength(0);
        asciiAreaBuilder.setLength(0);
    }

    /**
     * Builds the formatted row string.
     *
     * @param rowData the row data already used to format the bytes
     * @return a string which represents the formatted row, including the enabled areas.
     */
    @NotNull
    private String buildRow(@NotNull final RowData rowData)
    {
        rowBuilder.append(formatter.formatRowOffset(rowData.rowIndex(), rowData.offset()));

        if (includeHexArea)
        {
            rowBuilder.append(formatter.offsetHexSeparator());
            rowBuilder.append(hexAreaBuilder);
        }

        if (includeAsciiArea)
        {
            rowBuilder.append(formatter.hexAsciiSeparator());
            rowBuilder.append(asciiAreaBuilder);
        }

        rowBuilder.append(formatter.rowSeparator());

        return rowBuilder.toString();
    }

    /**
     * Appends a byte to the <code>hexAreaBuilder</code> and <code>asciiAreaBuilder</code>.
     *
     * @param value          byte value to add.
     * @param byteIndexInRow the index of the byte in the current row.
     */
    private void appendByte(final int value, final int byteIndexInRow)
    {
        appendByteToHexArea(value, byteIndexInRow);
        appendByteToAsciiArea(value, byteIndexInRow);
    }

    /**
     * Appends a byte to the <code>hexAreaBuilder</code> if this area should be included
     * in the final formatted row.
     *
     * @param value          byte value to add.
     * @param byteIndexInRow the index of the byte in the current row.
     */
    private void appendByteToHexArea(final int value, final int byteIndexInRow)
    {
        if (includeHexArea)
        {
            hexAreaBuilder.append(formatter.hexByteSeparator(byteIndexInRow));
            hexAreaBuilder.append(formatter.formatHexByte(value));
        }
    }

    /**
     * Appends a byte to the <code>asciiAreaBuilder</code> if this area should be included
     * in the final formatted row.
     *
     * @param value          byte value to add.
     * @param byteIndexInRow the index of the byte in the current row.
     */
    private void appendByteToAsciiArea(final int value, final int byteIndexInRow)
    {
        if (includeAsciiArea)
        {
            asciiAreaBuilder.append(formatter.asciiByteSeparator(byteIndexInRow));
            asciiAreaBuilder.append(formatter.formatAsciiByte(value));
        }
    }

    /**
     * Fills the space of excluded bytes for the <code>hexAreaBuilder</code> and <code>asciiAreaBuilder</code>
     * with a configured placeholder.
     *
     * @param indexInRow the index in the current row from where to start.
     * @param repeats    number of placeholders to add.
     */
    private void appendBytePlaceholder(final int indexInRow, final int repeats)
    {
        if(repeats > 0)
        {
            appendBytePlaceholderToHexArea(indexInRow, repeats);
            appendBytePlaceholderToAsciiArea(indexInRow, repeats);
        }
    }

    /**
     * Fills the space in the <code>hexAreaBuilder</code> for the excluded bytes with a configured placeholder.
     * This method does nothing if the hex area shouldn't be included the final row.
     *
     * @param indexInRow the index in the current row from where to start.
     * @param repeats    number of placeholders to add.
     */
    private void appendBytePlaceholderToHexArea(final int indexInRow, final int repeats)
    {
        if (includeHexArea)
        {
            for (int i = indexInRow; i < indexInRow + repeats; i++)
            {
                hexAreaBuilder.append(formatter.hexByteSeparator(i));
                hexAreaBuilder.append(formatter.hexBytePlaceholder(i));
            }
        }
    }

    /**
     * Fills the space in the <code>asciiAreaBuilder</code> for the excluded bytes with a configured placeholder.
     * This method does nothing if the ascii area shouldn't be included the final row.
     *
     * @param indexInRow the index in the current row from where to start.
     * @param repeats    number of placeholders to add.
     */
    private void appendBytePlaceholderToAsciiArea(final int indexInRow, final int repeats)
    {
        if (includeAsciiArea)
        {
            for (int i = indexInRow; i < indexInRow + repeats; i++)
            {
                asciiAreaBuilder.append(formatter.asciiByteSeparator(i));
                asciiAreaBuilder.append(formatter.asciiBytePlaceholder(i));
            }
        }
    }
}
