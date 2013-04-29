package ru.ifmo.patterns.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import ru.ifmo.patterns.server.Server;

import javax.xml.soap.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitry Golovchenko
 */
public class Client extends UnicastRemoteObject {
	private final Iterator<String> lines;
	private final String host;
	private final int port;

	public Client(File input, String host, int port) throws IOException {
		this(FileUtils.lineIterator(input, "UTF-8"), host, port);
	}

	public Client(Iterator<String> input, String host, int port) throws RemoteException {
		this.lines = input;
		this.port = port;
		this.host = host;
	}

	public List<Double> calcResults() throws IOException, InterruptedException, SOAPException {
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

	private double calcLine(String expr) throws IOException, InterruptedException, SOAPException {
		try (Socket socket = new Socket(host, port)) {
			//Send message
			SOAPMessage message = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
			SOAPEnvelope env = message.getSOAPPart().getEnvelope();
			Name exprName = env.createName("expr");
			SOAPBody body = env.getBody();
			body.addChildElement(exprName).setValue(expr);
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			message.writeTo(output);
			socket.shutdownOutput();

			//Receive message
			message = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)
					  .createMessage(null, socket.getInputStream());
			env = message.getSOAPPart().getEnvelope();
			exprName = env.createName("result");
			body = env.getBody();
			SOAPElement element = (SOAPElement)body.getChildElements(exprName).next();
			return Double.parseDouble(element.getValue());
		}
	}

	public void printResults() throws IOException, InterruptedException, SOAPException {
		for (Double result : calcResults()) {
			System.out.println(result);
		}
	}

	public static void main(String[] args) {
		try {
			runClient(new File("src/test/resources/input2.txt"), "localhost", Server.PORT);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void runClient(File input, String serverHost, int serverPort) throws IOException, SOAPException, InterruptedException {
		Client client = new Client(input, serverHost, serverPort);
		client.printResults();
	}
}
