package ru.ifmo.patterns.client;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public abstract class BinaryOperation implements Runnable, Serializable {
	private static final long serialVersionUID = 2617418064881197945L;
	public final double left, right;
	public final long nodeId;
	private final Client receiver;

	protected BinaryOperation(double left, double right, long nodeId, Client receiver) {
		this.left = left;
		this.right = right;
		this.nodeId = nodeId;
		this.receiver = receiver;
	}

	protected abstract double compute();

	@Override
	public void run() {
		try {
			receiver.submitResult(nodeId, compute());
		} catch (RemoteException e) {
			System.err.println("Can't send result to client: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
