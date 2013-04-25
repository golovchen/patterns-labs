package ru.ifmo.patterns.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Dmitry Golovchenko
 */
public class MessageQueueIml<T> extends UnicastRemoteObject implements MessageQueue<T> {
    public final ArrayBlockingQueue<T> queue;
    public static final int CAPACITY = 100;

	public MessageQueueIml() throws RemoteException {
        this(CAPACITY);
	}

    public MessageQueueIml(int capacity) throws RemoteException {
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

	public void share(String path) {

	}
}
