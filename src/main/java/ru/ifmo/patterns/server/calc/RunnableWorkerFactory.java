package ru.ifmo.patterns.server.calc;

import ru.ifmo.patterns.server.MessageQueue;
import ru.ifmo.patterns.server.Worker;
import ru.ifmo.patterns.server.WorkerFactory;
import ru.ifmo.patterns.server.WorkerPool;

/**
 * @author Dmitry Golovchenko
 */
public class RunnableWorkerFactory<T extends Runnable> implements WorkerFactory<T> {
	@Override
	public Worker<T> createWorker(MessageQueue<T> taskQueue, WorkerPool owner) {
        return new RunnableWorker<T>(taskQueue, owner);
	}
}
