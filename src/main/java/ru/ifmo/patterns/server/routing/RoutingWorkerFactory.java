package ru.ifmo.patterns.server.routing;

import ru.ifmo.patterns.server.*;
import ru.ifmo.patterns.server.calc.*;
import ru.ifmo.patterns.server.routing.RoutingWorkerImpl;

import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public class RoutingWorkerFactory implements WorkerFactory<NativeExpr> {
	private final MessageQueue<AddOperation> addQueue;
	private final MessageQueue<SubOperation> subQueue;
	private final MessageQueue<MulOperation> mulQueue;
	private final MessageQueue<DivOperation> divQueue;
	private final MessageQueue<ResultMessage> resultQueue;

	public RoutingWorkerFactory(
			MessageQueue<AddOperation> addQueue,
			MessageQueue<SubOperation> subQueue,
			MessageQueue<MulOperation> mulQueue,
			MessageQueue<DivOperation> divQueue,
	        MessageQueue<ResultMessage> resultQueue
	) {
		this.addQueue = addQueue;
		this.subQueue = subQueue;
		this.mulQueue = mulQueue;
		this.divQueue = divQueue;
		this.resultQueue = resultQueue;
	}

	@Override
	public Worker<NativeExpr> createWorker(MessageQueue<NativeExpr> taskQueue, WorkerPool owner) throws RemoteException {
        return new RoutingWorkerImpl(taskQueue, owner, addQueue, subQueue, mulQueue, divQueue, resultQueue);
	}
}
