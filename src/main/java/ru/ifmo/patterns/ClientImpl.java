package ru.ifmo.patterns;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * @author Dmitry Golovchenko
 */
public class ClientImpl extends UnicastRemoteObject implements Client {
	public ClientImpl(File input, MessageQueue<BinaryOperation> queue) throws RemoteException {
	}

	public ClientImpl(Iterable<String> input, MessageQueue<BinaryOperation> queue) throws RemoteException {
	}

	public void printResults() {

	}

	@Override
	public void submitResult(long expressionId, long nodeId, double result) {
	}

	public static List<Double> runClient(Iterable<String> input, String exportRmiPath, String queueRmiPath) {
		return null;
	}
}
