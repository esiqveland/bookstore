package amu.model;

import java.util.Calendar;

public class SimpleOrder {


	private int id;
	private int customer_id;
	private int address_id;
	private Calendar cal;
	private float value;
	private int orderStatus;
	
	public SimpleOrder(int id, int customer_id, int address_id, float value, int orderStatus){
		this.id = id;
		this.customer_id = customer_id;
		this.address_id = address_id;
		this.cal = Calendar.getInstance();
		this.value = value;
		this.orderStatus = orderStatus;
	}
	
	public int getId() {
		return id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public int getAddress_id() {
		return address_id;
	}

	public Calendar getCal() {
		return cal;
	}

	public float getValue() {
		return value;
	}

	public int getOrderStatus() {
		return orderStatus;
	}
	
	public void setOrderStatus(int status){
		this.orderStatus = status;
	}
}