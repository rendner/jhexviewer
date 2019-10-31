package cms.rendner.hexviewer.model.data;

import cms.rendner.hexviewer.view.JHexViewer;

/**
 * This interface is for models that may hold resources (such as file handles).
 * The dispose method of an {@link IDisposableModel} object is called automatically when a new {@link IDataModel}
 * is set for the {@link JHexViewer}, and {@link IDisposableModel#isAutoDispose} returns <code>true</code>.
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
