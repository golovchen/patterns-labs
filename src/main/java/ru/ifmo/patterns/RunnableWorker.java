package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public class RunnableWorker extends Worker<Runnable> {
	protected RunnableWorker(MessageQueue<Runnable> messageQueue, WorkerPool workerPool) {
		super(messageQueue, workerPool);
	}

	@Override
	protected void handle(Runnable message) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
