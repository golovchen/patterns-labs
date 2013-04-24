package ru.ifmo.patterns;

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
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
