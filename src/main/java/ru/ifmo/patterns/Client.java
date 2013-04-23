package ru.ifmo.patterns;

import java.rmi.Remote;

/**
 * @author Dmitry Golovchenko
 */
public interface Client extends Remote {
	void submitResult(long expressionId, long nodeId, double result);
}
