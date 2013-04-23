package ru.ifmo.patterns;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public interface MessageQueue<T> extends Remote {
	T take() throws InterruptedException, RemoteException;
	void put(T value) throws InterruptedException, RemoteException;
}
