package sd.examples.grpc;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import sd.examples.grpc.model.Customer;
import sd.examples.grpc.model.ServiceAGrpc;

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
public class ServiceAServer {
  private static final Logger logger = Logger.getLogger(ServiceAServer.class.getName());

  private final int port;
  private final Server server;
  final static Collection<Customer> customers = new ArrayList<Customer>();
  static {
	  customers.add(Customer.newBuilder().setId(1).setName("sibendu@email").setAddress("My Address").build());
	  customers.add(Customer.newBuilder().setId(2).setName("m@email").setAddress("My Address 2").build());
	  customers.add(Customer.newBuilder().setId(3).setName("d@email").setAddress("My Address 3").build());
  }
  /**
   * Main method.  This comment makes the linter happy.
   */
  public static void main(String[] args) throws Exception {
    ServiceAServer server = new ServiceAServer(9980);
    server.start();
    server.blockUntilShutdown();
  }

  /** Create a RouteGuide server listening on {@code port} using {@code featureFile} database. */
  public ServiceAServer(int port) throws IOException {
    this(ServerBuilder.forPort(port), port);
  }

  /** Create a RouteGuide server using serverBuilder as a base and features as data. */
  public ServiceAServer(ServerBuilder<?> serverBuilder, int port) {
    this.port = port;
    server = serverBuilder.addService(new ServiceA(customers)).build();
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
        ServiceAServer.this.stop();
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

  private static class ServiceA extends ServiceAGrpc.ServiceAImplBase {
    private final Collection<Customer> customers;

    ServiceA(Collection<Customer> customers) {
      this.customers = customers; 
    }

    @Override
    public void getFeature(Customer request, StreamObserver<Customer> responseObserver) {
      responseObserver.onNext(checkCustomer(request));
      responseObserver.onCompleted();
    }


    /**
     * Gets the feature at the given point.
     *
     * @param location the location to check.
     * @return The feature object at the point. Note that an empty name indicates no feature.
     */
    private Customer checkCustomer(Customer customer) {
      for (Customer cust : customers) {
        if (cust.getId() == customer.getId()) {
          return cust;
        }
      }
      return Customer.newBuilder().setId(-999).setName("").setAddress("3").build();
    }

  }
}