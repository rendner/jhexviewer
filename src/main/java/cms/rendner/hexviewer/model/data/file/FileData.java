package cms.rendner.hexviewer.model.data.file;

import cms.rendner.hexviewer.model.data.IDataModel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the data read from a file.
 * <p/>
 * Note that this model is intended for simple cases where it is convenient to read all bytes at once.
 * It is not intended for reading in large files.
 *
 * @author rendner
 */
public final class FileData implements IDataModel
{
    /**
     * The bytes of the loaded file.
     */
    @NotNull
    private final byte[] data;

    /**
     * Creates a new instance.
     *
     * @param file the file which provides the data.
     * @throws IOException if reading content from the file fails.
     */
    public FileData(@NotNull final File file) throws IOException
    {
        data = readContent(file);
    }

    @Override
    public long size()
    {
        return data.length;
    }

    @Override
    public boolean isEmpty()
    {
        return data.length == 0;
    }

    @Override
    public int getByte(final long offset)
    {
        return data[(int)offset];
    }

    /**
     * Reads the content from the file into <code>data</code>
     *
     * @param file the file to read from.
     * @return the data of the file.
     * @throws IOException if reading content from the file fails.
     */
    private byte[] readContent(@NotNull final File file) throws IOException
    {
        final Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);
    }
}
