package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public interface WorkerFactory<T> {
	Worker<T> createWorker(MessageQueue<T> taskQueue, WorkerPool owner);
}
