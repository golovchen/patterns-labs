package ru.ifmo.patterns.server;

import ru.ifmo.patterns.client.*;

import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public class RoutingWorker extends Worker<BinaryOperation> {
	private final MessageQueue<AddOperation>      addQueue;
	private final MessageQueue<SubOperation>      subQueue;
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
	protected void handle(BinaryOperation message) throws InterruptedException {
        try {
            if (message instanceof AddOperation) {
                addQueue.put((AddOperation) message);
                return;
            }
            if (message instanceof SubOperation) {
                subQueue.put((SubOperation) message);
                return;
            }
            if (message instanceof MulOperation) {
                mulQueue.put((MulOperation) message);
                return;
            }
            if (message instanceof DivOperation) {
                divQueue.put((DivOperation) message);
                return;
            }
        } catch (RemoteException e) {
            System.err.println("Exception during adding task: " + message + " into queue");
            e.printStackTrace();
        }
	}
}
