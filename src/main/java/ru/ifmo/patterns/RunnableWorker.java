package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public class RunnableWorker<T extends Runnable> extends Worker<T> {
	protected RunnableWorker(MessageQueue<T> messageQueue, WorkerPool workerPool) {
		super(messageQueue, workerPool);
	}

	@Override
	protected void handle(T message) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
