package cms.rendner.hexviewer.support.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;

/**
 * Consumes all content into a single string.
 *
 * @author rendner
 */
public final class ToStringConsumer implements IConsumer
{
    /**
     * Used to build a string from the consumed content.
     */
    private StringBuilder resultBuilder;

    /**
     * The final result.
     */
    @NotNull
    private String result = "";

    @Override
    public void start()
    {
        resultBuilder = new StringBuilder();
    }

    @Override
    public void consume(@NotNull final String content)
    {
        resultBuilder.append(content);
    }

    @Override
    public void end()
    {
        result = resultBuilder != null ? resultBuilder.toString() : "";
        resultBuilder = null;
    }

    /**
     * @return a concatenated string of the consumed content.
     */
    @NotNull
    public String content()
    {
        return result;
    }
}
