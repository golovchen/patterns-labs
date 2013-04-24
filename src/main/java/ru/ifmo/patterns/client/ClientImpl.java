package ru.ifmo.patterns.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import ru.ifmo.patterns.server.MessageQueue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Golovchenko
 */
public class ClientImpl extends UnicastRemoteObject implements Client {
	private final Iterator<String> lines;
	private final MessageQueue<BinaryOperation> queue;
	private Map<Long, Node> idToNode;
	private ArrayBlockingQueue<Node> acceptedNodes = new ArrayBlockingQueue<>(100);

	public ClientImpl(File input, MessageQueue<BinaryOperation> queue) throws IOException {
		this(FileUtils.lineIterator(input, "UTF-8"), queue);
	}

	public ClientImpl(Iterator<String> input, MessageQueue<BinaryOperation> queue) throws RemoteException {
		this.lines = input;
		this.queue = queue;
	}

	private List<Double> calcResults() throws RemoteException, InterruptedException {
		List<Double> result = new ArrayList<>();
		try {
			while (lines.hasNext()) {
				result.add(calcLine(lines.next()));
			}
		} finally {
			if (lines instanceof LineIterator) {
				((LineIterator) lines).close();
			}
		}
		return result;
	}

	private double calcLine(String expr) throws RemoteException, InterruptedException {
		List<Node> nodes = Node.parseExpression(expr);
		idToNode = new ConcurrentHashMap<>();
		for (Node node : nodes) {
			idToNode.put(node.id, node);
		}
		for (Node node : nodes) {
			if (node.canBeCalculated()) {
				queue.put(node.toBinaryOperation(this));
			}
		}

		while (true) {
			Node node = acceptedNodes.take();
			if (node.parent == null) {
				return node.value;
			} else {
				if (node.parent.canBeCalculated()) {
					queue.put(node.parent.toBinaryOperation(this));
				}
			}
		}
	}

	public void printResults() throws RemoteException, InterruptedException {
		for (Double result : calcResults()) {
			System.out.println(result);
		}
	}

	@Override
	public void submitResult(long nodeId, double result) {
		Node node = idToNode.get(nodeId);
		node.value = result;
		acceptedNodes.add(node);
	}

	public static List<Double> runClient(Iterable<String> input, String exportRmiPath, String queueRmiPath)
			throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
		MessageQueue<BinaryOperation> queue = (MessageQueue<BinaryOperation>)Naming.lookup(queueRmiPath);
		ClientImpl client = new ClientImpl(input.iterator(), queue);
		Naming.rebind(exportRmiPath, client);
		return client.calcResults();
	}

	public static void runClient(File input, String exportRmiPath, String queueRmiPath)
			throws IOException, NotBoundException, InterruptedException {
		MessageQueue<BinaryOperation> queue = (MessageQueue<BinaryOperation>)Naming.lookup(queueRmiPath);
		ClientImpl client = new ClientImpl(input, queue);
		Naming.rebind(exportRmiPath, client);
		client.printResults();
	}
}
