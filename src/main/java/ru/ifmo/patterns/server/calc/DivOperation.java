package ru.ifmo.patterns.server.calc;

import ru.ifmo.patterns.server.routing.RoutingWorker;

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

	public DivOperation(double left, double right, long nodeId, RoutingWorker receiver) {
		super(left, right, nodeId, receiver);
	}
}
