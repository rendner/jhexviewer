package cms.rendner.hexviewer.utils.observer;

/**
 * Helper class for install/uninstall observer without checking for <code>null</code> objects.
 *
 * @author rendner
 */
@Deprecated
public class ObserverUtils
{
    /**
     * Add an observer to an observable.
     * This method checks if the parameters are not <code>null</code> and only
     * in this case the observer is registered. Otherwise nothing will happen.
     *
     * @param observable the object to which the <code>observer</code> should be registered.
     * @param observer   the object which should be registered.
     */
    public static <T> void addObserver(final IObservable<T> observable, final IObserver<T> observer)
    {
        if (observable != null && observer != null)
        {
            observable.addObserver(observer);
        }
    }

    /**
     * Remove an observer from a observable.
     * This method checks if the parameters are not <code>null</code> and only
     * in this case the observer is unregistered. Otherwise nothing will happen.
     *
     * @param observable the object from which the <code>observer</code> should be removed.
     * @param observer   the object which should be unregistered.
     */
    public static <T> void removeObserver(final IObservable<T> observable, final IObserver<T> observer)
    {
        if (observable != null && observer != null)
        {
            observable.deleteObserver(observer);
        }
    }
}
