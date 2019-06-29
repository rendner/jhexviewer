package cms.rendner.hexviewer.support.data.visitor.consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Consumes all content into a File.
 *
 * @author rendner
 */
public class ToFileConsumer implements IConsumer
{
    /**
     * Specifies after how many characters the collected content should be written to the file.
     */
    private static final long flushDataThreshold = 2000;

    /**
     * Target file to write to.
     */
    protected File file;

    /**
     * Used to consume all content before writing it to the file.
     */
    protected StringBuilder stringConsumer;

    /**
     * Creates a new instance.
     *
     * @param file the target file to write to.
     *             Already existing content in this file will be replaced with the consumed content.
     */
    public ToFileConsumer(final File file)
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
    public void consume(final String content)
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
    }

    /**
     * Writes a string to a file.
     *
     * @param file    file to write to.
     * @param content content to write into the specified file. The length of the content will be set to <code>0</code>
     *                after write.
     */
    private void appendToFile(final File file, final StringBuilder content)
    {
        if (content.length() > 0)
        {
            try (FileOutputStream output = new FileOutputStream(file, true))
            {
                output.write(content.toString().getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            content.setLength(0);
        }
    }

    /**
     * Clears the existing content of a file.
     * @param file file to clear.
     */
    private void clearFileContent(final File file)
    {
        try (FileOutputStream output = new FileOutputStream(file))
        {
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
