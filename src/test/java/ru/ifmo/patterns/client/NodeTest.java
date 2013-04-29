package ru.ifmo.patterns.client;

import org.junit.Test;
import ru.ifmo.patterns.server.routing.Node;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Golovchenko
 */
public class NodeTest {
	@Test
	public void testParseExpression() throws Exception {
		List<Node> actual = Node.parseExpression("3");
		assertEquals(1, actual.size());
		assertEquals(3., actual.get(0).value, 0.0001);
	}

	@Test
	public void testParseExpression2() throws Exception {
		List<Node> actual = Node.parseExpression("3 1 +");
		assertEquals(3, actual.size());
	}
}
