package ru.ifmo.patterns;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Dmitry Golovchenko
 */
public class MessageQueueIml<T>  implements MessageQueue<T> {

	@Override
	public T take() throws RemoteException {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void put(T value) throws RemoteException {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
