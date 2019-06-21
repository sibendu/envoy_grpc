package sd.examples.grpc;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import sd.examples.grpc.model.*;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample gRPC server that serve the RouteGuide (see route_guide.proto) service.
 */
public class ServiceBServer {
  private static final Logger logger = Logger.getLogger(ServiceBServer.class.getName());

  private final int port;
  private final Server server;
  final static Collection<Customer> customerOrders = new ArrayList<Customer>();
  static {
	  
	  Item i1 = Item.newBuilder().setId(1).setCode("Item A").setQuantity(3).setValue(300).build();
	  Item i2 = Item.newBuilder().setId(1).setCode("Item B").setQuantity(2).setValue(500).build();
	  Item i3 = Item.newBuilder().setId(1).setCode("Item C").setQuantity(1).setValue(1000).build();
	  
	  Order order1 = Order.newBuilder().setId(1) 
			  .setDate("01-Apr-2019")
			  .setTotal(800)
			  .addItem(i1)
			  .addItem(i2)
			  .build();
	  
	  Order order2 = Order.newBuilder().setId(1)
			  .setDate("02-Apr-2019")
			  .setTotal(1000)
			  .addItem(i3)
			  .build();
	  
	  Order order3 = Order.newBuilder().setId(1)
			  .setDate("03-Apr-2019")
			  .setTotal(1800)
			  .addItem(i1)
			  .addItem(i2)
			  .addItem(i3)
			  .build();
			  
	  Customer customer1 = Customer.newBuilder().setId(1).setName("SDas")
			  .addOrder(order1)
			  .addOrder(order2)
			  .build();
	  
	  Customer customer2 = Customer.newBuilder().setId(2).setName("DDas")
			  .addOrder(order3)
			  .build();
	  
	  Customer customer3 = Customer.newBuilder().setId(3).setName("MDas")
			  .addOrder(order1)
			  .addOrder(order2)
			  .addOrder(order3)
			  .build();
	  
	  customerOrders.add(customer1);
	  customerOrders.add(customer2);
	  customerOrders.add(customer3);
  }
  /**
   * Main method.  This comment makes the linter happy.
   */
  public static void main(String[] args) throws Exception {
    ServiceBServer server = new ServiceBServer(8980);
    server.start();
    server.blockUntilShutdown();
  }

  /** Create a RouteGuide server listening on {@code port} using {@code featureFile} database. */
  public ServiceBServer(int port) throws IOException {
    this(ServerBuilder.forPort(port), port);
  }

  /** Create a RouteGuide server using serverBuilder as a base and features as data. */
  public ServiceBServer(ServerBuilder<?> serverBuilder, int port) {
    this.port = port;
    server = serverBuilder.addService(new ServiceB(customerOrders)).build();
  }

  /** Start serving requests. */
  public void start() throws IOException {
    server.start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may has been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        ServiceBServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  /** Stop serving requests and shutdown resources. */
  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private static class ServiceB extends ServiceBGrpc.ServiceBImplBase {
    private final Collection<Customer> customerOrders;

    ServiceB(Collection<Customer> customerOrders) {
      this.customerOrders = customerOrders; 
    }

    public void getOrders(Customer request, StreamObserver<Customer> responseObserver) {
      responseObserver.onNext(checkCustomer(request));
      responseObserver.onCompleted();
    }


    private Customer checkCustomer(Customer customer) {
      for (Customer cust : customerOrders) {
        if (cust.getId() == customer.getId()) {
          return cust;
        }
      }
      return Customer.newBuilder().setId(-999).setName("").build();
    }

  }
}