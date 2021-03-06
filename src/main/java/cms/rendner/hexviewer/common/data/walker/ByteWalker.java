package cms.rendner.hexviewer.common.data.walker;

import cms.rendner.hexviewer.common.data.visitor.IByteVisitor;
import cms.rendner.hexviewer.common.data.wrapper.DataPart;
import cms.rendner.hexviewer.model.data.IDataModel;
import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Iterates over a specified range of bytes of the data model used by the {@link JHexViewer}.
 *
 * @author rendner
 */
public final class ByteWalker
{
    /**
     * Provides the data that is iterated over.
     */
    @NotNull
    private final IDataModel dataModel;

    /**
     * Creates a new instance.
     *
     * @param dataModel the data to iterate over.
     */
    public ByteWalker(@NotNull final IDataModel dataModel)
    {
        super();

        this.dataModel = dataModel;
    }

    /**
     * Iterates of the specified range of bytes and forwards each byte in this range to the <code>visitor</code>.
     *
     * @param visitor to visit the bytes of the specified range.
     * @param start   the start index of the byte, included in the range.
     * @param end     the end index of the byte, included in the range.
     */
    public void walk(@NotNull final IByteVisitor visitor, final long start, final long end)
    {
        final DataPart data = createDataPart(start, end);

        visitor.start();

        for (int i = 0; i < data.size(); i++)
        {
            visitor.visitByte(data.getByte(i));
        }

        visitor.end();
    }

    /**
     * Creates a instance which only provides the specified range of data from the data model.
     *
     * @param start the start index of the byte, included in the range.
     * @param end   the end index of the byte, included in the range.
     * @return the part which only contains the bytes of the specified range.
     */
    @NotNull
    private DataPart createDataPart(final long start, final long end)
    {
        final long size = end - start + 1;
        return new DataPart(dataModel, start, size);
    }
}
