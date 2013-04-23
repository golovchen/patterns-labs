package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public abstract class BinaryOperation implements Runnable {
	public final double left, right;
	public final long expressionId, nodeId;
	private final Client receiver;

	protected BinaryOperation(double left, double right, long expressionId, long nodeId, Client receiver) {
		this.left = left;
		this.right = right;
		this.expressionId = expressionId;
		this.nodeId = nodeId;
		this.receiver = receiver;
	}

	protected abstract double compute();

	@Override
	public void run() {
		receiver.submitResult(expressionId, nodeId, compute());
	}
}
