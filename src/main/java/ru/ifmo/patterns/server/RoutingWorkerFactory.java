package ru.ifmo.patterns.server;

import ru.ifmo.patterns.client.*;

/**
 * @author Dmitry Golovchenko
 */
public class RoutingWorkerFactory implements WorkerFactory<BinaryOperation> {
	private final MessageQueue<AddOperation> addQueue;
	private final MessageQueue<SubOperation> subQueue;
	private final MessageQueue<MulOperation> mulQueue;
	private final MessageQueue<DivOperation> divQueue;

	public RoutingWorkerFactory(
			MessageQueue<AddOperation> addQueue,
			MessageQueue<SubOperation> subQueue,
			MessageQueue<MulOperation> mulQueue,
			MessageQueue<DivOperation> divQueue
	) {
		this.addQueue = addQueue;
		this.subQueue = subQueue;
		this.mulQueue = mulQueue;
		this.divQueue = divQueue;
	}

	@Override
	public Worker<BinaryOperation> createWorker(MessageQueue<BinaryOperation> taskQueue, WorkerPool owner) {
        return new RoutingWorker(taskQueue, owner, addQueue, subQueue, mulQueue, divQueue);
	}
}
