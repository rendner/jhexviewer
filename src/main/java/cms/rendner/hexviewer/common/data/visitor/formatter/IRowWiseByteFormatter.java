package cms.rendner.hexviewer.common.data.visitor.formatter;

import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Allows a full customization of how a row (including all three areas of the {@link JHexViewer}
 * ​​should be formatted to get a printable representation.
 *
 * @author rendner
 */
public interface IRowWiseByteFormatter
{
    /**
     * Formats a byte to a text character.
     *
     * @param value byte to format.
     * @return the text character representing the byte value.
     */
    @NotNull
    String formatTextByte(int value);

    /**
     * Formats a byte to a hex character.
     *
     * @param value byte to format.
     * @return the hex character representing the byte value.
     */
    @NotNull
    String formatHexByte(int value);

    /**
     * Formats the row offset to a printable representation.
     *
     * @param rowIndex   the index of the row for which the offset should be formatted.
     * @param byteOffset the offset to format.
     * @return a printable representation of the offset.
     */
    @NotNull
    String formatRowOffset(int rowIndex, long byteOffset);

    /**
     * @return Returns the row separator string. Usually this is the line separator.
     */
    @NotNull
    String rowSeparator();

    /**
     * Returns the separator to use in front of a hex byte.
     *
     * @param indexOfByteInRow index of the hex byte to print.
     * @return the separator,
     */
    @NotNull
    String hexByteSeparator(int indexOfByteInRow);

    /**
     * Returns the separator to use in front of the text byte.
     *
     * @param indexOfByteInRow index of the text byte to print.
     * @return the separator,
     */
    @NotNull
    String textByteSeparator(int indexOfByteInRow);

    /**
     * Returns a placeholder for a specific omitted hex byte to fill the space of the omitted hex byte.
     *
     * @param indexInRow the index of the byte to omit.
     * @return the placeholder (e.g. a " ").
     */
    @NotNull
    String hexBytePlaceholder(int indexInRow);

    /**
     * Returns a placeholder for a specific omitted text byte to fill the space of the omitted text byte.
     *
     * @param indexInRow the index of the byte to omit.
     * @return the placeholder (e.g. a ".").
     */
    @NotNull
    String textBytePlaceholder(int indexInRow);

    /**
     * @return the separator to visually separate the hex-area from the offset-area (e.g. a "|").
     */
    @NotNull
    String offsetHexSeparator();

    /**
     * @return the separator to visually separate the text-area from the hex-area (e.g. a "|").
     */
    @NotNull
    String hexTextSeparator();
}
