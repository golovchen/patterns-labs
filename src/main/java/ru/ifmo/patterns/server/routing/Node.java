package ru.ifmo.patterns.server.routing;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.primitives.Longs;
import ru.ifmo.patterns.server.calc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

/**
* @author Dmitry Golovchenko
*/
public class Node {
	private static AtomicLong idCounter = new AtomicLong();

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
		id = idCounter.getAndIncrement();
	}

	public Node(Class<? extends BinaryOperation> operationType, Node left, Node right) {
		this.value = null;
		this.operationType = operationType;
		this.left = left;
		this.right = right;
		id = idCounter.getAndIncrement();
	}

	public boolean canBeCalculated() {
		return value == null
				&& left != null && left.value != null
				&& right != null && right.value != null;
	}

	public BinaryOperation toBinaryOperation(RoutingWorkerImpl receiver) {
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

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("id", id)
				.toString();
	}

	public static List<Node> parseExpression(String expression) throws NumberFormatException {
		Stack<Node> stack = new Stack<>();
		for (String lexeme : Splitter.on(' ').trimResults().omitEmptyStrings().split(expression)) {
			Class<? extends BinaryOperation> operationType = parseOperationType(lexeme);
			if (operationType != null) {
				Node right = stack.pop();
				Node left = stack.pop();
				stack.push(new Node(operationType, left, right));
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
