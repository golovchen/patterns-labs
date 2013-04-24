package ru.ifmo.patterns.client;

import com.google.common.base.Splitter;
import com.google.common.primitives.Longs;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import ru.ifmo.patterns.server.MessageQueue;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

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
		idToNode = new HashMap<>();
		for (Node node : nodes) {
			idToNode.put(node.id, node);
		}
		for (Node node : nodes) {
			if (node.isReadyToCalculate()) {
				queue.put(node.toBinaryOperation(this));
			}
		}

		do {
			Node node = acceptedNodes.take();
			if (node.parent == null) {
				return node.value;
			} else {
				if (node.parent.isReadyToCalculate()) {
					queue.put(node.parent.toBinaryOperation(this));
				}
			}
		} while (!acceptedNodes.isEmpty());

		throw new RuntimeException();
	}

	public void printResults() {

	}

	@Override
	public void submitResult(long nodeId, double result) {
		Node node = idToNode.get(nodeId);
		node.value = result;
		acceptedNodes.add(node);
	}

	public static List<Double> runClient(Iterable<String> input, String exportRmiPath, String queueRmiPath) {
		return null;
	}

	private static class Node {
		private static long idCounter = 0;

		public final long id;
		public final Class<? extends BinaryOperation> operationType;
		public Double value;
		public Node parent;
		private final Node left, right;

		public Node(double value) {
			this.value = value;
			this.operationType = null;
			this.left = null;
			this.right = null;
			id = idCounter++;
		}

		public Node(Class<? extends BinaryOperation> operationType, Node left, Node right) {
			this.value = null;
			this.operationType = null;
			this.left = null;
			this.right = null;
			id = idCounter++;
		}

		public boolean isReadyToCalculate() {
			return left.value != null && right.value != null;
		}

		public BinaryOperation toBinaryOperation(Client receiver) {
			if (operationType == AddOperation.class) {
				return new AddOperation(left.value, right.value, id, receiver);
			}
			if (operationType == SubOperation.class) {
				return new SubOperation(left.value, right.value, id, receiver);
			}
			if (operationType == MulOperation.class) {
				return new MulOperation(left.value, right.value, id, receiver);
			}
			if (operationType == DivOperation.class) {
				return new DivOperation(left.value, right.value, id, receiver);
			}
			throw new RuntimeException();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			return id == ((Node)o).id;
		}

		@Override
		public int hashCode() {
			return Longs.hashCode(id);
		}

		public static List<Node> parseExpression(String expression) throws NumberFormatException {
			Stack<Node> stack = new Stack<>();
			for (String lexeme : Splitter.on(' ').trimResults().omitEmptyStrings().split(expression)) {
				Class<? extends BinaryOperation> operationType = parseOperationType(lexeme);
				if (operationType == null) {
					Node right = stack.pop();
					Node left = stack.pop();
					stack.push(new Node(null, left, right));
					right.parent = stack.peek();
					left.parent = stack.peek();
				} else {
					stack.push(new Node(Double.parseDouble(lexeme)));
				}
			}

			List<Node> result = new ArrayList<>();
			while (!stack.isEmpty()) {
				Node node = stack.pop();
				result.add(node);
				if (node.left != null) {
					stack.push(node.left);
				}
				if (node.right != null) {
					stack.push(node.right);
				}
			}
			return result;
		}

		private static Class<? extends BinaryOperation> parseOperationType(String str) {
			switch (str) {
				case "+": return AddOperation.class;
				case "-": return SubOperation.class;
				case "*": return MulOperation.class;
				case "/": return DivOperation.class;
				default:  return null;
			}
		}
	}
}
