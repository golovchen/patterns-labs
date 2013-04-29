package ru.ifmo.patterns.server;

import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public interface WorkerFactory<T> {
	Worker<T> createWorker(MessageQueue<T> taskQueue, WorkerPool owner) throws RemoteException;
}
