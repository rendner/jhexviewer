package cms.rendner.hexviewer.support.data.visitor.consumer;

public class ToConsoleConsumer implements IConsumer
{
    @Override
    public void start()
    {
        System.out.println();
    }

    @Override
    public void consume(String content)
    {
        System.out.print(content);
    }

    @Override
    public void end()
    {
        System.out.println();
    }
}
