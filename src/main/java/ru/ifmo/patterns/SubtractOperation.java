package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public class SubtractOperation extends BinaryOperation {
	@Override
	protected double compute() {
		return left - right;
	}

	protected SubtractOperation(double left, double right, long expressionId, long nodeId, Client receiver) {
		super(left, right, expressionId, nodeId, receiver);
	}
}
