package cms.rendner.hexviewer.support.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Consumes all content into a UTF-8 encoded file.
 *
 * @author rendner
 */
public final class ToFileConsumer implements IConsumer
{
    /**
     * Specifies after how many characters the collected content should be written to the file.
     */
    private static final long flushDataThreshold = 32_000;

    /**
     * Target file to write to.
     */
    @NotNull
    private final Path path;

    /**
     * Used to consume all content before writing it to the file.
     */
    private StringBuilder stringConsumer;

    /**
     * Writer to write to the file.
     */
    private BufferedWriter writer;

    /**
     * Creates a new instance.
     *
     * @param path the target file to write to.
     *             Already existing content in this file will be replaced with the consumed content.
     */
    public ToFileConsumer(@NotNull final Path path)
    {
        super();
        this.path = path;
    }

    @Override
    public void start()
    {
        stringConsumer = new StringBuilder();
    }

    @Override
    public void consume(@NotNull final String content)
    {
        stringConsumer.append(content);

        if (stringConsumer.length() > flushDataThreshold)
        {
            writeQuietly(stringConsumer);
        }
    }

    @Override
    public void end()
    {
        writeQuietly(stringConsumer);
        closeQuietly(writer);
    }

    /**
     * Writes a string to a file.
     *
     * @param content content to write into the file. The length of the content will be set to <code>0</code>
     *                after write.
     */
    private void writeQuietly(@NotNull final StringBuilder content)
    {
        if (content.length() > 0)
        {
            if (writer == null)
            {
                writer = openQuietly(path);
            }

            if (writer != null)
            {
                try
                {
                    writer.write(content.toString());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                content.setLength(0);
            }
        }
    }

    /**
     * Creates a buffered writer with the char set <code>StandardCharsets.UTF_8</code> to write to the specified file.
     * <p/>
     * If the file already exists, the existing content will be replaced by the new one written to the created buffer.
     *
     * @param path the path to the file.
     * @return the buffered writer, or <code>null</code> if no buffer could be created.
     */
    @Nullable
    private BufferedWriter openQuietly(@NotNull final Path path)
    {
        try
        {
            return Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Closes a Closeable instance.
     * @param closeable the instance to close.
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
}
