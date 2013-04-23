package ru.ifmo.patterns;

import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public abstract class Worker<T> implements Runnable {
	private final MessageQueue<T> messageQueue;
	private final WorkerPool workerPool;

	protected Worker(MessageQueue<T> messageQueue, WorkerPool workerPool) {
		this.messageQueue = messageQueue;
		this.workerPool = workerPool;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				T task = messageQueue.take();
				try {
					handle(task);
				} catch (Exception e) {
					System.err.println("Exception during handling task " + task + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			workerPool.queueAccessException(e);
		} catch (InterruptedException e) {
			//Everything is OK
		}
	}

	protected abstract void handle(T message);
}
