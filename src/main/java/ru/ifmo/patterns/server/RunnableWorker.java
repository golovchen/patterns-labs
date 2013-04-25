package ru.ifmo.patterns.server;

/**
 * @author Dmitry Golovchenko
 */
public class RunnableWorker<T extends Runnable> extends Worker<T> {
	protected RunnableWorker(MessageQueue<T> messageQueue, WorkerPool workerPool) {
		super(messageQueue, workerPool);
	}

	@Override
	protected void handle(T message) {
        message.run();
	}
}
