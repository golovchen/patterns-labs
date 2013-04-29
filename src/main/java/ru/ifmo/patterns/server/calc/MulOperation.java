package ru.ifmo.patterns.server.calc;

import ru.ifmo.patterns.server.routing.RoutingWorker;

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

	public MulOperation(double left, double right, long nodeId, RoutingWorker receiver) {
		super(left, right, nodeId, receiver);
	}
}
