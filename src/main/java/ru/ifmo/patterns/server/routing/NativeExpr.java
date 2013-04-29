package ru.ifmo.patterns.server.routing;

import java.io.Serializable;

/**
 * @author Dmitry Golovchenko
 */
public class NativeExpr implements Serializable {
	public final long taskId;
	public final String expresstion;

	public NativeExpr(long taskId, String expression) {
		this.taskId = taskId;
		this.expresstion = expression;
	}
}
