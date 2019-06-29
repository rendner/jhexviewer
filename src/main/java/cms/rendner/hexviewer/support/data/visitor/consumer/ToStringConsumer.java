package cms.rendner.hexviewer.support.data.visitor.consumer;

/**
 * Consumes all content into a single string.
 *
 * @author rendner
 */
public class ToStringConsumer implements IConsumer
{
    /**
     * Used to build a string from the consumed content.
     */
    protected StringBuilder resultBuilder;

    /**
     * The final result.
     */
    protected String result;

    @Override
    public void start()
    {
        resultBuilder = new StringBuilder();
    }

    @Override
    public void consume(final String content)
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
    public String content()
    {
        return result;
    }
}
