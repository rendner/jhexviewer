package cms.rendner.hexviewer.common.data.visitor;

import cms.rendner.hexviewer.common.data.visitor.consumer.IDataConsumer;
import cms.rendner.hexviewer.common.data.visitor.consumer.ToConsoleConsumer;
import cms.rendner.hexviewer.common.data.visitor.formatter.IRowWiseByteFormatter;
import cms.rendner.hexviewer.common.data.wrapper.RowData;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Byte visitor which uses a {@link IRowWiseByteFormatter} and a {@link IDataConsumer} to forward
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
    private final IDataConsumer consumer;

    /**
     * The number of bytes in a row.
     */
    private final int bytesPerRow;

    /**
     * Used to build a row which includes the three areas of the
     * {@link JHexViewer}.
     *
     * @see AreaId
     */
    private StringBuilder rowBuilder;

    /**
     * Used to build the part of the final row from the bytes displayed
     * in the {@link AreaId#HEX}.
     */
    private StringBuilder hexAreaBuilder;

    /**
     * Used to build the part of the final row from the bytes displayed
     * in the {@link AreaId#TEXT}.
     */
    private StringBuilder textAreaBuilder;

    /**
     * If <code>true</code> the bytes displayed in the
     * {@link AreaId#HEX} are
     * included in the final row which is passed to the <code>consumer</code>.
     */
    private boolean includeHexArea;

    /**
     * If <code>true</code> the bytes displayed in the
     * {@link AreaId#TEXT} are
     * included in the final row which is passed to the <code>consumer</code>.
     */
    private boolean includeTextArea;

    /**
     * Creates a new instance.
     *
     * @param formatter   to format byte values ​​row-based before they are written to the consumer.
     * @param bytesPerRow the number of bytes in a row, &gt;= 0.
     */
    public RowWiseByteVisitor(@NotNull final IRowWiseByteFormatter formatter, final int bytesPerRow)
    {
        this(formatter, null, bytesPerRow);
    }

    /**
     * Creates a new instance.
     *
     * @param formatter   to format byte values ​​row-based before they are written to the consumer. Can't be <code>null</code>.
     * @param consumer    the consumer to write to, if <code>null</code> a ToConsoleConsumer will be used to write to.
     * @param bytesPerRow the number of bytes in a row, &gt;= 0.
     */
    public RowWiseByteVisitor(@NotNull final IRowWiseByteFormatter formatter, @Nullable final IDataConsumer consumer, final int bytesPerRow)
    {
        super();

        this.formatter = formatter;
        this.consumer = consumer == null ? new ToConsoleConsumer() : consumer;
        this.bytesPerRow = bytesPerRow;
    }

    /**
     * Sets if the bytes of the {@link AreaId#HEX} should be included in the
     * row-based formatted string.
     *
     * @param includeHexArea if bytes of the hex-area should be included or not.
     */
    public void setIncludeHexArea(final boolean includeHexArea)
    {
        this.includeHexArea = includeHexArea;
    }

    /**
     * Sets if the bytes of the {@link AreaId#TEXT} should be included in the
     * row-based formatted string.
     *
     * @param includeTextArea if bytes of the text-area should be included or not.
     */
    public void setIncludeTextArea(final boolean includeTextArea)
    {
        this.includeTextArea = includeTextArea;
    }

    @Override
    public void start()
    {
        consumer.start();
        rowBuilder = new StringBuilder();
        hexAreaBuilder = new StringBuilder();
        textAreaBuilder = new StringBuilder();
    }

    @Override
    public void end()
    {
        consumer.end();
        rowBuilder = null;
        hexAreaBuilder = null;
        textAreaBuilder = null;
    }

    @Override
    public void visitRow(@NotNull final RowData rowData, final int leadingBytesToIgnore, final int trailingBytesToIgnore)
    {
        appendBytePlaceholder(0, leadingBytesToIgnore);

        int currentByteIndex = leadingBytesToIgnore;

        final int printableBytesSize = Math.max(currentByteIndex, rowData.size() - trailingBytesToIgnore);
        while (currentByteIndex < printableBytesSize)
        {
            appendByte(rowData.getByte(currentByteIndex), currentByteIndex);
            currentByteIndex++;
        }

        final int missingPlaceholders = bytesPerRow - currentByteIndex;
        appendBytePlaceholder(currentByteIndex, missingPlaceholders);

        consumer.consume(buildRow(rowData));

        rowBuilder.setLength(0);
        hexAreaBuilder.setLength(0);
        textAreaBuilder.setLength(0);
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

        if (includeTextArea)
        {
            rowBuilder.append(formatter.hexTextSeparator());
            rowBuilder.append(textAreaBuilder);
        }

        rowBuilder.append(formatter.rowSeparator());

        return rowBuilder.toString();
    }

    /**
     * Appends a byte to the <code>hexAreaBuilder</code> and <code>textAreaBuilder</code>.
     *
     * @param value          byte value to add.
     * @param byteIndexInRow the index of the byte in the current row.
     */
    private void appendByte(final int value, final int byteIndexInRow)
    {
        appendByteToHexArea(value, byteIndexInRow);
        appendByteToTextArea(value, byteIndexInRow);
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
     * Appends a byte to the <code>textAreaBuilder</code> if this area should be included
     * in the final formatted row.
     *
     * @param value          byte value to add.
     * @param byteIndexInRow the index of the byte in the current row.
     */
    private void appendByteToTextArea(final int value, final int byteIndexInRow)
    {
        if (includeTextArea)
        {
            textAreaBuilder.append(formatter.textByteSeparator(byteIndexInRow));
            textAreaBuilder.append(formatter.formatTextByte(value));
        }
    }

    /**
     * Fills the space of excluded bytes for the <code>hexAreaBuilder</code> and <code>textAreaBuilder</code>
     * with a configured placeholder.
     *
     * @param indexInRow the index in the current row from where to start.
     * @param repeats    number of placeholders to add.
     */
    private void appendBytePlaceholder(final int indexInRow, final int repeats)
    {
        if (repeats > 0)
        {
            appendBytePlaceholderToHexArea(indexInRow, repeats);
            appendBytePlaceholderToTextArea(indexInRow, repeats);
        }
    }

    /**
     * Fills the space in the <code>hexAreaBuilder</code> for the excluded bytes with a configured placeholder.
     * This method does nothing if the hex-area shouldn't be included the final row.
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
     * Fills the space in the <code>textAreaBuilder</code> for the excluded bytes with a configured placeholder.
     * This method does nothing if the text-area shouldn't be included the final row.
     *
     * @param indexInRow the index in the current row from where to start.
     * @param repeats    number of placeholders to add.
     */
    private void appendBytePlaceholderToTextArea(final int indexInRow, final int repeats)
    {
        if (includeTextArea)
        {
            for (int i = indexInRow; i < indexInRow + repeats; i++)
            {
                textAreaBuilder.append(formatter.textByteSeparator(i));
                textAreaBuilder.append(formatter.textBytePlaceholder(i));
            }
        }
    }
}
