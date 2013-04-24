package ru.ifmo.patterns.server;

/**
 * @author Dmitry Golovchenko
 */
public interface WorkerFactory<T> {
	Worker<T> createWorker(MessageQueue<T> taskQueue, WorkerPool owner);
}
