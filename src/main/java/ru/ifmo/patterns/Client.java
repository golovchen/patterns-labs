package ru.ifmo.patterns;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public interface Client extends Remote {
	void submitResult(long expressionId, long nodeId, double result) throws RemoteException;
}
