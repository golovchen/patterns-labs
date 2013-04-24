package ru.ifmo.patterns.client;

/**
 * @author Dmitry Golovchenko
 */
public class DivOperation extends BinaryOperation {
	@Override
	protected double compute() {
		return left / right;
	}

	protected DivOperation(double left, double right, long expressionId, long nodeId, Client receiver) {
		super(left, right, expressionId, nodeId, receiver);
	}
}