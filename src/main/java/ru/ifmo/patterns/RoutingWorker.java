package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public class RoutingWorker extends Worker<BinaryOperation> {
	private final MessageQueue<AddOperation>      addQueue;
	private final MessageQueue<SubOperation> subQueue;
	private final MessageQueue<MulOperation>      mulQueue;
	private final MessageQueue<DivOperation>      divQueue;

	public RoutingWorker(
			MessageQueue<BinaryOperation> messageQueue,
			WorkerPool workerPool,
			MessageQueue<AddOperation> addQueue,
			MessageQueue<SubOperation> subQueue,
			MessageQueue<MulOperation> mulQueue,
			MessageQueue<DivOperation> divQueue
	) {
		super(messageQueue, workerPool);
		this.addQueue = addQueue;
		this.subQueue = subQueue;
		this.mulQueue = mulQueue;
		this.divQueue = divQueue;
	}

	@Override
	protected void handle(BinaryOperation message) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
