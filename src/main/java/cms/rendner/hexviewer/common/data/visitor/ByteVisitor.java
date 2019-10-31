package cms.rendner.hexviewer.common.data.visitor;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.data.visitor.consumer.IDataConsumer;
import cms.rendner.hexviewer.common.data.visitor.consumer.ToConsoleConsumer;
import cms.rendner.hexviewer.common.data.visitor.formatter.NoopFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Byte visitor which forwards formatted byte values to an {@link IDataConsumer}.
 *
 * @author rendner
 */
public final class ByteVisitor implements IByteVisitor
{
    /**
     * Used to format visited bytes before passing them to the consumer.
     */
    @NotNull
    private final IValueFormatter formatter;

    /**
     * Used to consume the formatted value of the visited bytes.
     */
    @NotNull
    private final IDataConsumer consumer;

    /**
     * Creates a new instance.
     *
     * @param formatter to format the byte values before they are written to the consumer, if <code>null</code> no
     *                  formatting will take place.
     */
    public ByteVisitor(@Nullable final IValueFormatter formatter)
    {
        this(null, formatter);
    }

    /**
     * Creates a new instance.
     *
     * @param consumer the consumer to write to, if <code>null</code> a {@link ToConsoleConsumer} will be used to write to.
     */
    public ByteVisitor(@Nullable final IDataConsumer consumer)
    {
        this(consumer, null);
    }

    /**
     * Creates a new instance.
     *
     * @param consumer  the consumer to write to, if <code>null</code> a {@link ToConsoleConsumer} will be used to write to.
     * @param formatter to format the byte values before they are written to the consumer, if <code>null</code> no
     *                  formatting will take place.
     */
    public ByteVisitor(@Nullable final IDataConsumer consumer, @Nullable final IValueFormatter formatter)
    {
        super();
        this.consumer = consumer == null ? new ToConsoleConsumer() : consumer;
        this.formatter = formatter == null ? new NoopFormatter() : formatter;
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
