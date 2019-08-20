package cms.rendner.hexviewer.support.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

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

    /**
     * The result consumer.
     */
    @Nullable
    private final Consumer<String> resultConsumer;

    /**
     * Creates a new instance, without a result consumer.
     */
    public ToStringConsumer()
    {
        this(null);
    }

    /**
     * Creates a new instance with the specified result consumer.
     *
     * @param resultConsumer the consumer to be called when the final result is available.
     */
    public ToStringConsumer(@Nullable final Consumer<String> resultConsumer)
    {
        super();
        this.resultConsumer = resultConsumer;
    }

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

        if (resultConsumer != null)
        {
            resultConsumer.accept(content());
        }
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
