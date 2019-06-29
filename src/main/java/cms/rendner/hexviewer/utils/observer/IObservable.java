package cms.rendner.hexviewer.utils.observer;

/**
 * @author rendner
 */
public interface IObservable<T>
{
    void addObserver(IObserver<T> observer);

    void deleteObserver(IObserver<T> observer);

    void notifyObservers();

    void notifyObservers(T arg);
}
