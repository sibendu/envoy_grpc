package sd.examples;

import java.io.Serializable;
import java.util.Collection;

public class Order implements Serializable {

	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Collection<Item> getItems() {
		return items;
	}
	public void setItems(Collection<Item> items) {
		this.items = items;
	}
	public Order() {
		
	}
	public Order(Long id, String date, Double total, Collection<Item> items) {
		super();
		this.id = id;
		this.date = date;
		this.total = total;
		this.items = items;
	}
	private String date;
	private Double total;
	private Collection<Item> items;
}
