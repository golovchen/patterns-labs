package ru.ifmo.patterns.server.routing;

import ru.ifmo.patterns.server.MessageQueue;
import ru.ifmo.patterns.server.ResultMessage;
import ru.ifmo.patterns.server.Worker;
import ru.ifmo.patterns.server.WorkerPool;
import ru.ifmo.patterns.server.calc.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Golovchenko
 */
public class RoutingWorkerImpl extends Worker<NativeExpr> implements RoutingWorker {
	private final MessageQueue<AddOperation> addQueue;
	private final MessageQueue<SubOperation> subQueue;
	private final MessageQueue<MulOperation> mulQueue;
	private final MessageQueue<DivOperation> divQueue;
	private final MessageQueue<ResultMessage> resultQueue;

	private Map<Long, Node> idToNode;
	private ArrayBlockingQueue<Node> acceptedNodes = new ArrayBlockingQueue<>(100);

	public RoutingWorkerImpl(MessageQueue<NativeExpr> messageQueue, WorkerPool workerPool, MessageQueue<AddOperation> addQueue, MessageQueue<SubOperation> subQueue, MessageQueue<MulOperation> mulQueue, MessageQueue<DivOperation> divQueue, MessageQueue<ResultMessage> resultQueue) throws RemoteException {
		super(messageQueue, workerPool);
		this.addQueue = addQueue;
		this.subQueue = subQueue;
		this.mulQueue = mulQueue;
		this.divQueue = divQueue;
		this.resultQueue = resultQueue;
		UnicastRemoteObject.exportObject(this);
	}

	private void addToProperQueue(BinaryOperation message) throws InterruptedException {
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

	@Override
	protected void handle(NativeExpr message) throws InterruptedException, RemoteException {
		List<Node> nodes = Node.parseExpression(message.expresstion);
		idToNode = new ConcurrentHashMap<>();
		for (Node node : nodes) {
			idToNode.put(node.id, node);
		}
		for (Node node : nodes) {
			if (node.canBeCalculated()) {
				addToProperQueue(node.toBinaryOperation(this));
			}
		}

		Node node;
		do {
			node = acceptedNodes.take();
			if (node.parent == null) {
				resultQueue.put(new ResultMessage(message.taskId, node.value));
			} else {
				if (node.parent.canBeCalculated()) {
					addToProperQueue(node.parent.toBinaryOperation(this));
				}
			}
		} while (node.parent != null);
	}


	@Override
	public void submitResult(long nodeId, double result) {
		Node node = idToNode.get(nodeId);
		node.value = result;
		acceptedNodes.add(node);
	}
}
