package ru.ifmo.patterns;

import java.util.List;

/**
 * @author Dmitry Golovchenko
 */
public class WorkerPool<T> {
	private final MessageQueue<T> queue;
	private final List<Thread> threads;

	public WorkerPool(MessageQueue<T> queue, WorkerFactory factory, int workersCount) {
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
