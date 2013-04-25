package ru.ifmo.patterns.server;

/**
 * @author Dmitry Golovchenko
 */
public class RunnableWorkerFactory<T extends Runnable> implements WorkerFactory<T> {
	@Override
	public Worker<T> createWorker(MessageQueue<T> taskQueue, WorkerPool owner) {
        return new RunnableWorker<T>(taskQueue, owner);
	}
}
