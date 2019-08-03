package cms.rendner.hexviewer.support.data.visitor.consumer;

import org.jetbrains.annotations.NotNull;

public class ToConsoleConsumer implements IConsumer
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
