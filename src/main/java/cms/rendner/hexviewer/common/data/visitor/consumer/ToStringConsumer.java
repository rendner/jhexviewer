package cms.rendner.hexviewer.common.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Builds a string of the consumable content.
 *
 * @author rendner
 */
public final class ToStringConsumer implements IDataConsumer
{
    /**
     * Used to build a string from the consumed content.
     */
    private StringBuilder resultBuilder;

    /**
     * The result consumer.
     */
    @NotNull
    private final Consumer<String> resultConsumer;

    /**
     * Creates a new instance with the specified result consumer.
     *
     * @param resultConsumer the consumer to be called when the final result is available.
     */
    public ToStringConsumer(@NotNull final Consumer<String> resultConsumer)
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
        resultConsumer.accept(resultBuilder.toString());
        resultBuilder = null;
    }
}
