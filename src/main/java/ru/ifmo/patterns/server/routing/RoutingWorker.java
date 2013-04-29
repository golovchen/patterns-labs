package ru.ifmo.patterns.server.routing;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Dmitry Golovchenko
 */
public interface RoutingWorker extends Remote {
	void submitResult(long nodeId, double result) throws RemoteException;
}
