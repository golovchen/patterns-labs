package ru.ifmo.patterns;

import java.util.List;

/**
 * @author Dmitry Golovchenko
 */
public class WorkerPool {
	private final MessageQueue<?> queue;
	private final List<Thread> threads;

	public <T> WorkerPool(MessageQueue<T> queue, WorkerFactory<T> factory, int workersCount) {
		this.queue = queue;
		this.threads = null;
	}

	public void queueAccessException(Exception exception) {

	}

	public void start() {

	}

	public void terminate() {

	}
}
