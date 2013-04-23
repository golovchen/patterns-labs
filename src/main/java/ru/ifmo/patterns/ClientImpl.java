package ru.ifmo.patterns;

import java.io.File;

/**
 * @author Dmitry Golovchenko
 */
public class ClientImpl implements Client {
	public ClientImpl(File input, MessageQueue<BinaryOperation> queue) {

	}

	public ClientImpl(Iterable<String> input, MessageQueue<BinaryOperation> queue) {

	}

	public void printResults() {

	}

	@Override
	public void submitResult(long expressionId, long nodeId, double result) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
