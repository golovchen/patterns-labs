package ru.ifmo.patterns.server.calc;

import ru.ifmo.patterns.server.routing.RoutingWorker;

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

	public AddOperation(double left, double right, long nodeId, RoutingWorker receiver) {
		super(left, right, nodeId, receiver);
	}
}
