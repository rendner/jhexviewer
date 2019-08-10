package cms.rendner.hexviewer.core.model.data.file;

import cms.rendner.hexviewer.core.model.data.IDataModel;
import cms.rendner.hexviewer.core.model.data.IDisposableModel;
import cms.rendner.hexviewer.utils.ByteSizeConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Provides the data from a file.
 * <p/>
 * Note: This model is a PoC and only reads max the first 50 MB from a file.
 * This model can be used for huge files. The data of the file is read via RandomAccessFile/FileChannel/MappedByteBuffer.
 *
 * @author rendner
 * @see RandomAccessFile
 * @see FileChannel
 * @see MappedByteBuffer
 */
public class MappedFileData implements IDisposableModel, IDataModel
{
    /**
     * Maximal size which is read from a file.
     */
    private static final int MAX_MAPPABLE_BYTES = 50 * ByteSizeConstants.ONE_MB;

    /**
     * Indicates if the file was already disposed.
     */
    private boolean disposed;

    /**
     * To read from the file.
     */
    private RandomAccessFile raf;

    /**
     * The file channel associated with the <code>raf</code>.
     */
    private FileChannel fileChannel;

    /**
     * Region of mapped bytes directly into memory.
     */
    private MappedByteBuffer mappedByteBuffer;

    /**
     * Creates a new instance.
     *
     * @param file the file which provides the data.
     * @throws IOException if reading content from the file fails.
     */
    public MappedFileData(@NotNull final File file) throws IOException
    {
        mapFileContent(file);
    }

    @Override
    public int size()
    {
        return mappedByteBuffer == null ? 0 : mappedByteBuffer.limit();
    }

    @Override
    public boolean isEmpty()
    {
        return mappedByteBuffer == null || mappedByteBuffer.limit() == 0;
    }

    @Override
    public int getByte(final int offset)
    {
        if (mappedByteBuffer == null)
        {
            throw new IndexOutOfBoundsException("Index '" + offset + "' is out of bounds.");
        }
        return mappedByteBuffer.get(offset);
    }

    /**
     * Maps the content of the file into memory.
     *
     * @param file the file to read.
     * @throws IOException if reading content from the file fails.
     */
    private void mapFileContent(@NotNull final File file) throws IOException
    {
        try
        {
            raf = new RandomAccessFile(file, "r");
            fileChannel = raf.getChannel();
            final int maxMappableSize = (int) Math.min(fileChannel.size(), MAX_MAPPABLE_BYTES);
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, maxMappableSize);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            unmapFileContent();
            throw e;
        }
    }

    /**
     * Unmaps the file content.
     */
    private void unmapFileContent()
    {
        closeQuietly(fileChannel);
        fileChannel = null;

        closeQuietly(raf);
        raf = null;

        mappedByteBuffer = null;
    }

    /**
     * Equivalent to Closeable.close(), except any exceptions will be ignored.
     *
     * @param closeable the objects to close, may be <code>null</code> or already closed.
     */
    private void closeQuietly(@Nullable final Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dispose()
    {
        if (!disposed)
        {
            unmapFileContent();
            disposed = true;
        }
    }

    @Override
    public boolean isAutoDispose()
    {
        return true;
    }

    @Override
    public boolean disposed()
    {
        return disposed;
    }
}
