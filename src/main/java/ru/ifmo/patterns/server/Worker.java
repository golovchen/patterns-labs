package ru.ifmo.patterns.server;

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
		while (!Thread.currentThread().isInterrupted()) {
			T task = null;
			try {
				task = messageQueue.take();
				handle(task);
			} catch (RemoteException e) {
				workerPool.queueAccessException(e);
				return;
			} catch (InterruptedException e) {
				return;
			} catch (Exception e) {
				System.err.println("Exception during handling task " + task + ": " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	protected abstract void handle(T message) throws InterruptedException, RemoteException;
}
