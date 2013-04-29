package ru.ifmo.patterns.server;

import java.io.Serializable;

/**
 * @author Dmitry Golovchenko
 */
public class ResultMessage implements Serializable {
	public final long taskId;
	public final double result;

	public ResultMessage(long taskId, double result) {
		this.taskId = taskId;
		this.result = result;
	}
}
