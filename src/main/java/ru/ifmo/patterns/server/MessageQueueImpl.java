package ru.ifmo.patterns.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Dmitry Golovchenko
 */
public class MessageQueueImpl<T> extends UnicastRemoteObject implements MessageQueue<T> {
    public final ArrayBlockingQueue<T> queue;
    public static final int CAPACITY = 100;

	public MessageQueueImpl() throws RemoteException {
        this(CAPACITY);
	}

    public MessageQueueImpl(int capacity) throws RemoteException {
        queue = new ArrayBlockingQueue<T>(capacity);
    }

    @Override
	public T take() throws RemoteException, InterruptedException {
        return queue.take();
	}

	@Override
	public void put(T value) throws RemoteException, InterruptedException {
        queue.put(value);
	}

	public void share(String path) throws MalformedURLException, RemoteException {
		Naming.rebind(path, this);
	}
}
