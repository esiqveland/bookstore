package amu.model;

public class OrderItems {

	private int order_item_id;
	private int order_id;
	private int book_id;
	private int quantity;
	private float price;
	private int status;

	public OrderItems(int order_item_id, int order_id, int book_id, int quantity, float price, int status){
		this.order_item_id = order_item_id;
		this.order_id = order_id;
		this.book_id = book_id;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
	}
	
	public int getOrder_item_id() {
		return order_item_id;
	}

	public int getOrder_id() {
		return order_id;
	}

	public int getBook_id() {
		return book_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getPrice() {
		return price;
	}

	public int getStatus() {
		return status;
	}
}
