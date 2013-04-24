package ru.ifmo.patterns.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Dmitry Golovchenko
 */
public class MessageQueueIml<T> extends UnicastRemoteObject implements MessageQueue<T> {

	protected MessageQueueIml() throws RemoteException {
	}

	@Override
	public T take() throws RemoteException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void put(T value) throws RemoteException {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void share(String path) {

	}
}
