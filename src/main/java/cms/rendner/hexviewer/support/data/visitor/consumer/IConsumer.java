package cms.rendner.hexviewer.support.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;

/**
 * A consumer consumes content, e.g. writes the content to a console or file.
 *
 * @author rendner
 */
public interface IConsumer
{
    /**
     * Notifies the consumer to initialize the required setup.
     * This method is called before the first time <code>consume</code> is called.
     */
    void start();

    /**
     * Consumes a string.
     * @param content the content to be consumed.
     */
    void consume(@NotNull String content);

    /**
     * Notifies the consumer that all content is consumed.
     * This method is called after the last time <code>consume</code> was called.
     */
    void end();
}
