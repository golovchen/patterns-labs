package ru.ifmo.patterns.server;

import ru.ifmo.patterns.server.routing.NativeExpr;

import javax.xml.soap.*;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Golovchenko
 */
public class Server {
	public static final String MAIN_QUEUE = "mainQueue";
	public static final String RESULT_QUEUE = "resultQueue";
	public static final int PORT = 1361;

	private final AtomicLong threadId = new AtomicLong();
	private final MessageQueueImpl<NativeExpr> mainQueue;
	private final MessageQueueImpl<ResultMessage> resultQueue;
	private final ConcurrentHashMap<Long, Thread> threads = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Long, Double> results = new ConcurrentHashMap<>();

	public Server() throws IOException, SOAPException {
		mainQueue = new MessageQueueImpl<>();
		resultQueue = new MessageQueueImpl<>();
		mainQueue.share(MAIN_QUEUE);
		resultQueue.share(RESULT_QUEUE);
		runMainThread();
		startListen();
	}

	private void startListen() throws IOException, SOAPException {
		ServerSocket serv = null;
		serv = new ServerSocket(PORT);
		while (!Thread.interrupted()) {
			Socket client = serv.accept();
			ConnectHandler handler = new ConnectHandler(client);
			new Thread(handler).start();
		}
	}

	private void runMainThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {
						ResultMessage resultMsg = resultQueue.take();
						results.put(resultMsg.taskId, resultMsg.result);
						Thread handler = threads.get(resultMsg.taskId);
						synchronized (handler) {
							handler.notify();
						}
					}
				} catch (InterruptedException e) {
					//OK
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	private class ConnectHandler implements Runnable {
		private Socket socket;

		private ConnectHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				String expr = getExpression();
				long id = threadId.incrementAndGet();
				threads.put(id, Thread.currentThread());
				NativeExpr nativeExpr = new NativeExpr(id, expr);
				mainQueue.put(nativeExpr);
				synchronized (Thread.currentThread()) {
					Thread.currentThread().wait();
				}
				sendRespond(results.get(id));
				threads.remove(id);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}

		private void sendRespond(Double result) throws SOAPException, IOException {
			SOAPMessage message = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
			SOAPEnvelope env = message.getSOAPPart().getEnvelope();
			Name exprName = env.createName("result");
			SOAPBody body = env.getBody();
			body.addChildElement(exprName).setValue(Double.toString(result));
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			message.writeTo(out);
			out.flush();
		}

		private String getExpression() throws IOException, SOAPException {
			SOAPMessage message = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)
					.createMessage(null, socket.getInputStream());
			SOAPEnvelope env = message.getSOAPPart().getEnvelope();
			Name exprName = env.createName("expr");
			SOAPBody body = env.getBody();
			SOAPElement element = (SOAPElement)body.getChildElements(exprName).next();
			return element.getValue();
		}
	}

	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
