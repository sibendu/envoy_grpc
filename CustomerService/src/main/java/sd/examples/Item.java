package sd.examples;

import java.io.Serializable;
import java.util.Collection;

public class Item implements Serializable {
	private Long id;
	public Item() {
		
	}
	public Item(Long id, String code, Integer quantity, Double value) {
		super();
		this.id = id;
		this.code = code;
		this.quantity = quantity;
		this.value = value;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	private String code;
	private Integer quantity;
	private Double value;
	
}
