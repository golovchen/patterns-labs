package ru.ifmo.patterns.client;

import java.io.Serializable;

/**
 * @author Dmitry Golovchenko
 */
public class AddOperation extends BinaryOperation implements Serializable {
	private static final long serialVersionUID = -5553334356942156724L;

	@Override
	protected double compute() {
		return left + right;
	}

	protected AddOperation(double left, double right, long nodeId, Client receiver) {
		super(left, right, nodeId, receiver);
	}
}
