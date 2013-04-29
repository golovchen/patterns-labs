package ru.ifmo.patterns.server.calc;

import ru.ifmo.patterns.server.MessageQueue;
import ru.ifmo.patterns.server.Worker;
import ru.ifmo.patterns.server.WorkerPool;

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
