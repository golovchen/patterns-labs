package ru.ifmo.patterns.client;

import java.io.Serializable;

/**
 * @author Dmitry Golovchenko
 */
public class DivOperation extends BinaryOperation implements Serializable {
	private static final long serialVersionUID = 1774135983856774446L;

	@Override
	protected double compute() {
		return left / right;
	}

	protected DivOperation(double left, double right, long nodeId, Client receiver) {
		super(left, right, nodeId, receiver);
	}
}
