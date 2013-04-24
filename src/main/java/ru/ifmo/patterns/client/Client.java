package ru.ifmo.patterns.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public interface Client extends Remote {
	void submitResult(long nodeId, double result) throws RemoteException;
}
