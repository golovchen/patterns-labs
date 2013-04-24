package ru.ifmo.patterns.client;

import java.io.Serializable;

/**
 * @author Dmitry Golovchenko
 */
public class SubOperation extends BinaryOperation implements Serializable {
	private static final long serialVersionUID = 3994008752340519043L;

	@Override
	protected double compute() {
		return left - right;
	}

	protected SubOperation(double left, double right, long nodeId, Client receiver) {
		super(left, right, nodeId, receiver);
	}
}
