package cms.rendner.hexviewer.core.model.data.file;

import cms.rendner.hexviewer.core.model.data.IDataModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the data from a file.
 * <p/>
 * Note that this model is intended for simple cases where it is convenient to read all bytes at once.
 * It is not intended for reading in large files.
 *
 * @author rendner
 */
public class FileData implements IDataModel
{
    /**
     * The bytes of the loaded file.
     */
    private final byte[] data;

    /**
     * Creates a new instance.
     *
     * @param file the file which provides the data.
     * @throws IOException if reading content from the file fails.
     */
    public FileData(final File file) throws IOException
    {
        data = readContent(file);
    }

    @Override
    public int size()
    {
        return data == null ? 0 : data.length;
    }

    @Override
    public boolean isEmpty()
    {
        return data == null || data.length == 0;
    }

    @Override
    public int getByte(final int offset) throws IndexOutOfBoundsException
    {
        if (data == null)
        {
            throw new IndexOutOfBoundsException("Index '" + offset + "' is out of bounds.");
        }
        return data[offset];
    }

    /**
     * Reads the content from the file into <code>data</code>
     *
     * @param file the file to read from.
     * @return the data of the file.
     * @throws IOException if reading content from the file fails.
     */
    protected byte[] readContent(final File file) throws IOException
    {
        final Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);
    }
}
