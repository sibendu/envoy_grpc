package sd.examples.client;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import sd.examples.grpc.model.serviceb.Customer;
import sd.examples.grpc.model.serviceb.ServiceBGrpc;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample client code that makes gRPC calls to the server.
 */
public class ServiceBClient {
	private static final Logger logger = Logger.getLogger(ServiceBClient.class.getName());

	private final ManagedChannel channel;
	private final ServiceBGrpc.ServiceBBlockingStub blockingStub;
	private final ServiceBGrpc.ServiceBStub asyncStub;

	public static void main(String[] args) throws InterruptedException {

		ServiceBClient client = new ServiceBClient("orderservice", 8980);
		try {
			Customer customer = Customer.newBuilder().setId(2).build();
			customer = client.getOrders(customer);
			System.out.println(customer.toString());
		} finally {
			client.shutdown();
		}
	}
	/** Construct client for accessing RouteGuide server at {@code host:port}. */
	public ServiceBClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
	}

	/**
	 * Construct client for accessing RouteGuide server using the existing channel.
	 */
	public ServiceBClient(ManagedChannelBuilder<?> channelBuilder) {
		channel = channelBuilder.build();
		blockingStub = ServiceBGrpc.newBlockingStub(channel);
		asyncStub = ServiceBGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	public Customer getOrders(Customer customer) {
		info("*** getOrders: id = ", customer.getId());
		try {
			customer = blockingStub.getOrders(customer);
		} catch (StatusRuntimeException e) {
			warning("RPC failed: {0}", e.getStatus());
			return null;
		}
		return customer;
	}

	private void info(String msg, Object... params) {
		logger.log(Level.INFO, msg, params);
	}

	private void warning(String msg, Object... params) {
		logger.log(Level.WARNING, msg, params);
	}
}
