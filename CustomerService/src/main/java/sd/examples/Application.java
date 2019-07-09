package sd.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;

import sd.examples.client.ServiceAClient;
import sd.examples.client.ServiceBClient;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@RestController
@RequestMapping(value = "/")
//@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@GetMapping("/")
	public String message() {
		return "{\"message\":\"Hello World!\"}";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMessage() throws Exception {
		return "{\"message\":\"Hello World!\"}";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Customer getCustomer(@PathVariable("id") Integer id) throws Exception {
		System.out.println("id == "+id);
		Customer cust = new Customer(id, "Sibendu", "Das", new ArrayList<Order>());
		
		
		sd.examples.grpc.model.servicea.Customer c1 = null;
		ServiceAClient clientA = new ServiceAClient("profileservice", 8980);
		try {
			System.out.println("Before calling profileservice");
			c1 = sd.examples.grpc.model.servicea.Customer.newBuilder().setId(cust.getId().intValue()).build();
			c1 = clientA.getFeature(c1);
			System.out.println(c1.toString());
		}catch(Exception e) {
			System.out.println("Error calling profileservice == "+e.getMessage());
			e.printStackTrace();	
		} finally {
			clientA.shutdown();
		}

		sd.examples.grpc.model.serviceb.Customer c2 = null;
		ServiceBClient clientB = new ServiceBClient("orderservice", 8980);
		try {
			System.out.println("Before calling orderservice");
			c2 = sd.examples.grpc.model.serviceb.Customer.newBuilder().setId(cust.getId().intValue()).build();
			c2 = clientB.getOrders(c2);
			System.out.println(c2.toString());
		}catch(Exception e) {
			System.out.println("Error calling orderservice == "+e.getMessage());
			e.printStackTrace();	
		} finally {
			clientA.shutdown();
		}
		
		//System.out.println(c1+" :: "+c2);
		if (c1 != null) {
			System.out.println("Getting values returned from profileservice");
			cust.setName(c1.getName());
			cust.setAddress(c1.getAddress());
		}

		if (c2 != null && c2.getOrderList() != null) {
			System.out.println("Getting values returned from orderservice");
			Order oo = null;
			for (sd.examples.grpc.model.serviceb.Order ord : c2.getOrderList()) {

				oo = new Order(new Long(ord.getId()), ord.getDate(), new Double(ord.getTotal()), new ArrayList<Item>());
				if (ord.getItemList() != null) {
					for (sd.examples.grpc.model.serviceb.Item it : ord.getItemList()) {
						oo.getItems().add(new Item(new Long(it.getId()), it.getCode(), it.getQuantity(),
								new Double(it.getValue())));
					}
				}
				cust.getOrders().add(oo);
			}

		}
		System.out.println("Before returning");
		return cust;// new ResponseEntity<List<Customer>>(null, HttpStatus.OK);
	}

}
