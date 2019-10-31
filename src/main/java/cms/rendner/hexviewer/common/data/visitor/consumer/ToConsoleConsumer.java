package cms.rendner.hexviewer.common.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;

/**
 * Prints the consumable content to <code>System.out</code>.
 *
 * @author rendner-dev
 */
public class ToConsoleConsumer implements IDataConsumer
{
    @Override
    public void start()
    {
        System.out.println();
    }

    @Override
    public void consume(@NotNull final String content)
    {
        System.out.print(content);
    }

    @Override
    public void end()
    {
        System.out.println();
    }
}
