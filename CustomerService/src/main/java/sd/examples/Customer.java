package sd.examples;  

import java.io.Serializable;
import java.util.Collection;

public class Customer implements Serializable {
	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	public Customer(Integer id, String name, String address, Collection<Order> orders) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.orders = orders;
	}

	private String name;
	private String address;
	private Collection<Order> orders;
	
	public Customer() {
	}		
	
	
}
