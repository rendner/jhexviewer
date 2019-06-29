package cms.rendner.hexviewer.core.model.data;

import cms.rendner.hexviewer.utils.CheckUtils;

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
    private final byte[] data;

    /**
     * Creates a new instance which is empty.
     */
    public DefaultDataModel()
    {
        super();

        this.data = new byte[0];
    }

    /**
     * Creates a new instance.
     *
     * @param data the string to read from. The bytes of the string are encoded as utf-8.
     */
    public DefaultDataModel(final String data)
    {
        this(data, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance.
     *
     * @param data    the string to read from.
     * @param charset charset used to encode the bytes of the string.
     */
    public DefaultDataModel(final String data, final Charset charset)
    {
        super();

        CheckUtils.checkNotNull(data);
        CheckUtils.checkNotNull(charset);

        this.data = data.getBytes(charset);
    }

    @Override
    public int size()
    {
        return data.length;
    }

    @Override
    public boolean isEmpty()
    {
        return data.length == 0;
    }

    @Override
    public int getByte(final int offset) throws IndexOutOfBoundsException
    {
        return data[offset] & 0xFF;
    }
}
