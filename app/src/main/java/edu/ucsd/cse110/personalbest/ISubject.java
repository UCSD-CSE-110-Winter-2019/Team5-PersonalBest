package edu.ucsd.cse110.personalbest;

public interface ISubject<ObserverT> {

    void register(ObserverT observer);

    void unregister(ObserverT observer);

    void notifyObservers();
}
