package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public class RunnableWorkerFactory<T extends Runnable> implements WorkerFactory<T> {
	@Override
	public Worker<T> createWorker(MessageQueue<T> taskQueue, WorkerPool owner) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
