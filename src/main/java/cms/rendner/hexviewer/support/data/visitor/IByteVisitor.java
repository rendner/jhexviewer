package cms.rendner.hexviewer.support.data.visitor;

/**
 * Visitor for doing operations on byte level (searching, printing, etc.).
 * <p/>
 *  An implementation of this interface should be used by a {@link cms.rendner.hexviewer.support.data.walker.ByteWalker}
 *  to iterate over a range of bytes displayed in the {@link cms.rendner.hexviewer.core.JHexViewer}.
 *
 * @author rendner
 */
public interface IByteVisitor
{
    /**
     * Notifies the visitor to initialize the required setup.
     * This method is called before the first time <code>visitByte</code> is called.
     */
    void start();

    /**
     * Is called for every byte to visit.
     *
     * @param value byte to visit.
     */
    void visitByte(int value);

    /**
     * Notifies the visitor that all bytes are visited.
     * This method is called after the last time <code>visitByte</code> was called.
     */
    void end();
}
