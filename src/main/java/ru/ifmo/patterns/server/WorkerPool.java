package ru.ifmo.patterns.server;

/**
 * @author Dmitry Golovchenko
 */
public class WorkerPool {
	private final MessageQueue<?> queue;
	private final Thread[] threads;

	public <T> WorkerPool(MessageQueue<T> queue, WorkerFactory<T> factory, int workersCount) {
		this.queue = queue;
        this.threads = new Thread[workersCount];
        for (int i = 0; i < workersCount; i++) {
            threads[i] = new Thread(factory.createWorker(queue, this));
        }
    }

	public void queueAccessException(Exception exception) {
        System.err.println("Fatal error: " + exception);
        terminate();
    }

	public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

	public void terminate() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
