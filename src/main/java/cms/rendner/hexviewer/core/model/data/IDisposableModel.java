package cms.rendner.hexviewer.core.model.data;

/**
 * This interface is for models that may hold resources (such as file handles).
 * The dispose method of an IDisposableModel object is called automatically when a new IDataModel
 * is set for the JHexViewer, and the isAutoDispose method of the IDisposableModel returns <code>true</code>.
 *
 * @author rendner
 */
public interface IDisposableModel
{
    /**
     * @return <code>true</code> if the resource should automatically disposed.
     */
    boolean isAutoDispose();

    /**
     * @return <code>true</code> if the model was is disposed, otherwise <code>false</code>.
     */
    boolean disposed();

    /**
     * Frees the blocked resources.
     */
    void dispose();
}
