package cms.rendner.hexviewer.model.data;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Provides the data from a string.
 *
 * @author rendner
 */
public class DefaultDataModel implements IDataModel
{
    /**
     * The bytes of the string.
     */
    @NotNull
    private final byte[] data;

    /**
     * Creates a new instance.
     *
     * @param data the string to read from. The bytes of the string are encoded as utf-8.
     */
    public DefaultDataModel(@NotNull final String data)
    {
        this(data, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance.
     *
     * @param data    the string to read from.
     * @param charset charset used to encode the bytes of the string.
     */
    public DefaultDataModel(@NotNull final String data, @NotNull final Charset charset)
    {
        super();
        this.data = data.getBytes(charset);
    }

    @Override
    public long size()
    {
        return data.length;
    }

    @Override
    public boolean isEmpty()
    {
        return data.length == 0;
    }

    @Override
    public int getByte(final long offset)
    {
        return data[(int)offset] & 0xFF;
    }
}
