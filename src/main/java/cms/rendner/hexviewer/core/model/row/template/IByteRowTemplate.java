package cms.rendner.hexviewer.core.model.row.template;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Describes the layout of a byte-row.
 * A byte row consist of chars which are grouped to a "byte".
 * Between the bytes, a caret can be placed.
 *
 * @author rendner
 * @see IRowTemplate
 */
public interface IByteRowTemplate extends IRowTemplate
{
    /**
     * Computes the bounds of the caret in this row.
     * A caret is always placed in front of a byte.
     *
     * @param byteIndex   the index of the byte before which the caret should be inserted
     * @param returnValue the rectangle in which the result should be stored.
     * @return <code>returnValue</code> modified to specify the bounds
     * @throws IndexOutOfBoundsException if <code>byteIndex</code> is out of range (valid range: [0, getNumberOfBytes()-1])
     */
    @NotNull
    Rectangle caretBounds(int byteIndex, @NotNull Rectangle returnValue);
}
