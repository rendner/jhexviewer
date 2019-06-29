package cms.rendner.hexviewer.support.data.visitor;

import cms.rendner.hexviewer.support.data.formatter.NopFormatter;
import cms.rendner.hexviewer.support.data.visitor.consumer.IConsumer;
import cms.rendner.hexviewer.support.data.visitor.consumer.ToConsoleConsumer;
import cms.rendner.hexviewer.core.formatter.IValueFormatter;

/**
 * Byte visitor which uses a {@link IValueFormatter} and a {@link IConsumer} to forward formatted byte values to a
 * specified <code>consumer</code>.
 *
 * @author rendner
 */
public class ByteVisitor implements IByteVisitor
{
    /**
     * Used to format visited bytes before passing them to the consumer.
     */
    protected final IValueFormatter formatter;

    /**
     * Used to consume the formatted value of the visited bytes.
     */
    protected final IConsumer consumer;

    /**
     * Creates a new instance.
     *
     * @param formatter to format the byte values before they are written to the consumer, if <code>null</code> no
     *                  formatting will take place.
     */
    public ByteVisitor(final IValueFormatter formatter)
    {
        this(null, formatter);
    }

    /**
     * Creates a new instance.
     *
     * @param consumer the consumer to write to, if <code>null</code> a ToConsoleConsumer will be used to write to.
     */
    public ByteVisitor(final IConsumer consumer)
    {
        this(consumer, null);
    }

    /**
     * Creates a new instance.
     *
     * @param consumer  the consumer to write to, if <code>null</code> a ToConsoleConsumer will be used to write to.
     * @param formatter to format the byte values before they are written to the consumer, if <code>null</code> no
     *                  formatting will take place.
     */
    public ByteVisitor(final IConsumer consumer, final IValueFormatter formatter)
    {
        super();
        this.consumer = consumer == null ? new ToConsoleConsumer() : consumer;
        this.formatter = formatter == null ? new NopFormatter() : formatter;
    }

    @Override
    public void visitByte(final int value)
    {
        consumer.consume(formatter.format(value));
    }

    @Override
    public void start()
    {
        consumer.start();
    }

    @Override
    public void end()
    {
        consumer.end();
    }
}
