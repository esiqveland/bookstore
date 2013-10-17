package amu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Order {
    
    private Integer id;
    private Customer customer;
    private Address address;
    private Calendar createdDate;
    private String value;
    private int status;
    private Cart cart;
    // TODO: Add OrderItems

    public Order(int id, Customer customer, Address address, Calendar createdDate, String price, int orderStatus) {
        this.id = id;
        this.customer = customer;
        this.address = address;
        this.createdDate = createdDate;
        this.value = price;
        this.status = orderStatus;
    }

    public Order(Customer customer, Address address, String subtotal) {
        this.id = null;
        this.customer = customer;
        this.address = address;
        this.createdDate = null;
        this.value = subtotal;
        this.status = 0;
    }
    
    public Order(Customer customer, Address address, String subtotal, Cart cart) {
        this.id = null;
        this.customer = customer;
        this.address = address;
        this.createdDate = null;
        this.value = subtotal;
        this.status = 0;
        this.cart = cart;
    }
    
    public Order(int status, int id) {
        this.id = null;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }
    
    public Cart getCart(){
    	return cart;
    }

    public Address getAddress() {
        return address;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public String getValue() {
        return value;
    }

    public int getStatus() {
        return status;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getStatusText() {
        switch (status)
        {
            case 2: 
                return "Delivered";
            case 1:
                return "Shipped";
            case 0:
            default:
                return "Pending";
            case -1:
                return "Canceled";
            case -2:
            	return "Counter-Order";
        }
    }
}
