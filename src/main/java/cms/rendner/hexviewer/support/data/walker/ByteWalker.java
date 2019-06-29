package cms.rendner.hexviewer.support.data.walker;

import cms.rendner.hexviewer.support.data.visitor.IByteVisitor;
import cms.rendner.hexviewer.support.data.wrapper.DataPart;
import cms.rendner.hexviewer.core.model.data.IDataModel;
import cms.rendner.hexviewer.support.data.wrapper.IDataPart;
import cms.rendner.hexviewer.utils.CheckUtils;

/**
 * Iterates over a specified range of bytes of the data model used by the {@link cms.rendner.hexviewer.core.JHexViewer}.
 *
 * @author rendner
 */
public class ByteWalker
{
    /**
     * Provides the data that is iterated over.
     */
    protected final IDataModel dataModel;

    /**
     * Creates a new instance.
     *
     * @param dataModel the data to iterate over. Cant be <code>null</code>.
     */
    public ByteWalker(final IDataModel dataModel)
    {
        super();

        CheckUtils.checkNotNull(dataModel);

        this.dataModel = dataModel;
    }

    /**
     * Iterates of the specified range of bytes and forwards each byte in this range to the <code>visitor</code>.
     *
     * @param visitor to visit the bytes of the specified range.
     * @param start   the start index of the byte, included in the range.
     * @param end     the end index of the byte, included in the range.
     */
    public void walk(final IByteVisitor visitor, final int start, final int end)
    {
        final IDataPart data = createDataPart(start, end);

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
    protected IDataPart createDataPart(final int start, final int end)
    {
        final int size = end - start + 1;
        return new DataPart(dataModel, start, size);
    }
}
