package ru.ifmo.patterns;

/**
 * @author Dmitry Golovchenko
 */
public class MulOperation extends BinaryOperation {
	@Override
	protected double compute() {
		return left * right;
	}

	protected MulOperation(double left, double right, long expressionId, long nodeId, Client receiver) {
		super(left, right, expressionId, nodeId, receiver);
	}
}
