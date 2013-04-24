package ru.ifmo.patterns.client;

import java.io.Serializable;

/**
 * @author Dmitry Golovchenko
 */
public class MulOperation extends BinaryOperation implements Serializable {
	private static final long serialVersionUID = -235489044998885918L;

	@Override
	protected double compute() {
		return left * right;
	}

	protected MulOperation(double left, double right, long nodeId, Client receiver) {
		super(left, right, nodeId, receiver);
	}
}
