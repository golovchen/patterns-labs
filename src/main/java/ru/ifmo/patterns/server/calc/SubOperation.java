package ru.ifmo.patterns.server.calc;

import ru.ifmo.patterns.server.routing.RoutingWorker;

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

	public SubOperation(double left, double right, long nodeId, RoutingWorker receiver) {
		super(left, right, nodeId, receiver);
	}
}
