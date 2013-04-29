package ru.ifmo.patterns;

import org.junit.Ignore;
import org.junit.Test;
import ru.ifmo.patterns.client.*;
import ru.ifmo.patterns.server.*;
import ru.ifmo.patterns.server.calc.*;
import ru.ifmo.patterns.server.calc.RunnableWorkerFactory;
import ru.ifmo.patterns.server.routing.NativeExpr;
import ru.ifmo.patterns.server.routing.RoutingWorkerFactory;

import javax.xml.soap.SOAPException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MainTest {
	public static final String RMI_HOST = "//localhost/";
	public static final String ADD_QUEUE = RMI_HOST + "addQueue";
	public static final String SUB_QUEUE = RMI_HOST + "subQueue";
	public static final String MUL_QUEUE = RMI_HOST + "mulQueue";
	public static final String DIV_QUEUE = RMI_HOST + "divQueue";
	public static final String MAIN_QUEUE = RMI_HOST + "mainQueue";
	public static final String CLIENT = RMI_HOST + "client";
	
	@Test
	public void printResult() throws NotBoundException, InterruptedException, IOException, SOAPException {
		File input = new File("src/test/resources/input.txt");
		printFileResults(input);
	}

	@Test
	public void printResult2() throws NotBoundException, InterruptedException, IOException, SOAPException {
		File input = new File("src/test/resources/input2.txt");
		printFileResults(input);
	}

	private void printFileResults(File input) throws NotBoundException, IOException, InterruptedException, SOAPException {
		List<WorkerPool> pools = runServers();

		Client.runClient(input, "localhost", Server.PORT);

		for (WorkerPool pool : pools) {
			pool.terminate();
		}
	}

	private List<WorkerPool> runServers() throws IOException, NotBoundException, SOAPException, InterruptedException {
		LocateRegistry.createRegistry(1099);
		MessageQueueImpl<AddOperation> addQueue = new MessageQueueImpl<>();
		MessageQueueImpl<SubOperation> subQueue = new MessageQueueImpl<>();
		MessageQueueImpl<MulOperation> mulQueue = new MessageQueueImpl<>();
		MessageQueueImpl<DivOperation> divQueue = new MessageQueueImpl<>();
		addQueue.share(ADD_QUEUE);
		subQueue.share(SUB_QUEUE);
		mulQueue.share(MUL_QUEUE);
		divQueue.share(DIV_QUEUE);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Server server = new Server();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		Thread.sleep(30);

		final RoutingWorkerFactory routingFactory = new RoutingWorkerFactory(
				(MessageQueue<AddOperation>) Naming.lookup(ADD_QUEUE),
				(MessageQueue<SubOperation>)Naming.lookup(SUB_QUEUE),
				(MessageQueue<MulOperation>)Naming.lookup(MUL_QUEUE),
				(MessageQueue<DivOperation>)Naming.lookup(DIV_QUEUE),
				(MessageQueue<ResultMessage>)Naming.lookup(Server.RESULT_QUEUE)

		);

		final RunnableWorkerFactory<AddOperation> addWorkerFactory = new RunnableWorkerFactory();
		final RunnableWorkerFactory<SubOperation> subWorkerFactory = new RunnableWorkerFactory();
		final RunnableWorkerFactory<MulOperation> mulWorkerFactory = new RunnableWorkerFactory();
		final RunnableWorkerFactory<DivOperation> divWorkerFactory = new RunnableWorkerFactory();

		List<WorkerPool> pools = new ArrayList<WorkerPool>() {{
			add(createWorkerPool(MAIN_QUEUE, routingFactory, 40));
			add(createWorkerPool(ADD_QUEUE, addWorkerFactory, 10));
			add(createWorkerPool(SUB_QUEUE, subWorkerFactory, 10));
			add(createWorkerPool(MUL_QUEUE, mulWorkerFactory, 10));
			add(createWorkerPool(DIV_QUEUE, divWorkerFactory, 10));
		}};

		for (WorkerPool pool : pools) {
			pool.start();
		}
		return pools;
	}

	@Test
	@Ignore
	public void calcResult() throws IOException, NotBoundException, InterruptedException, SOAPException {
		List<WorkerPool> pools = runServers();

		List<String> input = Arrays.asList("1 2 +", "5 1 + 2 / 3 * 4 -");
		Client client = new Client(input.iterator(), "localhost", Server.PORT);
		List<Double> result = client.calcResults();
		List<Double> expectedResult = Arrays.asList(3., 5.);

		for (int i = 0; i < result.size(); i++) {
			assertEquals(expectedResult.get(i), result.get(i), 0.0001);
		}

		for (WorkerPool pool : pools) {
			pool.terminate();
		}
	}

	private static <T> WorkerPool createWorkerPool(String queuePath, WorkerFactory<T> factory, int threadCount)
			throws RemoteException, NotBoundException, MalformedURLException {
		return new WorkerPool((MessageQueue<T>)Naming.lookup(queuePath), factory, threadCount);
	}
}
