package cms.rendner.hexviewer.support.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Consumes all content into a File.
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
    private final File file;

    /**
     * Used to consume all content before writing it to the file.
     */
    private StringBuilder stringConsumer;

    /**
     * Stream to write to the file.
     */
    private FileOutputStream outputStream;

    /**
     * Creates a new instance.
     *
     * @param file the target file to write to.
     *             Already existing content in this file will be replaced with the consumed content.
     */
    public ToFileConsumer(@NotNull final File file)
    {
        super();
        this.file = file;
    }

    @Override
    public void start()
    {
        stringConsumer = new StringBuilder();
        clearFileContent(file);
    }

    @Override
    public void consume(@NotNull final String content)
    {
        stringConsumer.append(content);

        if (stringConsumer.length() > flushDataThreshold)
        {
            appendToFile(file, stringConsumer);
        }
    }

    @Override
    public void end()
    {
        appendToFile(file, stringConsumer);

        if(outputStream != null)
        {
            try
            {
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes a string to a file.
     *
     * @param file    file to write to.
     * @param content content to write into the specified file. The length of the content will be set to <code>0</code>
     *                after write.
     */
    private void appendToFile(@NotNull final File file, @NotNull final StringBuilder content)
    {
        if (content.length() > 0)
        {
            if(outputStream == null)
            {
                try
                {
                    outputStream = new FileOutputStream(file, true);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            if(outputStream != null)
            {
                try
                {
                    outputStream.write(content.toString().getBytes(StandardCharsets.UTF_8));
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
     * Clears the existing content of a file.
     * @param file file to clear.
     */
    private void clearFileContent(@NotNull final File file)
    {
        try (FileOutputStream output = new FileOutputStream(file))
        {
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
