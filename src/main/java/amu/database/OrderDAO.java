package amu.database;

import amu.model.CartItem;
import amu.model.Customer;
import amu.model.Order;
import amu.model.OrderItems;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;




public class OrderDAO {
	
	public class SimplifiedOrder {
		private int id;
		private int customer_id;
		private int address_id;
		private Calendar cal;
		private float value;
		private int orderStatus;
		
		SimplifiedOrder(int id, int customer_id, int address_id, float value, int orderStatus){
			this.id = id;
			this.customer_id = customer_id;
			this.address_id = address_id;
			this.cal = Calendar.getInstance();
			this.value = value;
			this.orderStatus = orderStatus;
		}
	}

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	
	public List<Order> browse(Customer customer) {
		List<Order> orders = new ArrayList<Order>();

		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `order` WHERE customer_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, customer.getId());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				AddressDAO addressDAO = new AddressDAO();
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				
				if (resultSet.getInt("status") == -2) continue;
				
				orders.add(new Order(resultSet.getInt("id"),
						customer,
						addressDAO.read(resultSet.getInt("address_id"), customer),
						createdDate,
						resultSet.getString("value"),
						resultSet.getInt("status")));
			
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return orders;
	}
	
	public boolean cancel(int orderId) {
		
		System.out.println("STARTING IN CANCEL");
		
		//Fetch order from DB
		SimplifiedOrder sOrder = null;
		try{
			connection = Database.getConnection();
			String query = "SELECT * FROM `order` WHERE id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, orderId);
			resultSet = statement.executeQuery();
						
			System.out.println("SELECT DONE");
			
			while (resultSet.next()) {
				sOrder = new SimplifiedOrder(resultSet.getInt("id"),
						resultSet.getInt("customer_id"),
						resultSet.getInt("address_id"),
						resultSet.getInt("value"),
						resultSet.getInt("status")
				);
			}
				
			//Update status and add negative order
			if((sOrder.orderStatus == 0) || (sOrder.orderStatus == 1)){
				sOrder.orderStatus = -1;
				query = "Update `order` SET status=-1 WHERE id=?";
				statement = connection.prepareStatement(query);
				statement.setInt(1, orderId);
				statement.executeUpdate();
				
				//Update status field on old orderitems
				query = "Update `order_items` SET status=-1 WHERE order_id=?";
				statement = connection.prepareStatement(query);
				statement.setInt(1, orderId);
				statement.executeUpdate();
				
				query = "INSERT INTO `order` (`customer_id`, `address_id`, `created`, `value`, `status`) "
						+ "VALUES(?,?,CURDATE(),?,?)";
				statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, sOrder.customer_id);
				statement.setInt(2, sOrder.address_id);
				statement.setFloat(3, sOrder.value*-1);
				statement.setInt(4, -2);
				statement.executeUpdate();
				
				resultSet = statement.getGeneratedKeys();
				
				if (resultSet.next()) {
					OrderItemsDAO orderItemsDAO = new OrderItemsDAO();
					List<OrderItems> list = orderItemsDAO.browseByOrderId(sOrder.id);
					
					for (OrderItems orderItems : list) {
						query = "INSERT INTO `order_items` (order_id, book_id, quantity, price, status)"
								+ " VALUES (?,?,?,?,?)";
						statement = connection.prepareStatement(query);
																			
						statement.setInt(1, orderItems.getOrder_id());	//Order_id		
						statement.setInt(2, orderItems.getBook_id());		
						statement.setInt(3, orderItems.getQuantity()*-1);			
						statement.setFloat(4, orderItems.getPrice());	
						statement.setInt(5, -2);	
						
						statement.executeUpdate();
					}
					
				}
				
				
				
				return true;
				}	
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}
		return false;
	}

	public boolean add(Order order) {

		try {
			connection = Database.getConnection();	

			String query = "INSERT INTO `order` (customer_id, address_id, created, value, status) VALUES (?, ?, CURDATE(), ?, ?)";
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, order.getCustomer().getId());
			statement.setInt(2, order.getAddress().getId());
			statement.setBigDecimal(3, new BigDecimal(order.getValue()));
			statement.setInt(4, order.getStatus());
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();		

			if (resultSet.next()) {
			
				Iterator<CartItem> it = order.getCart().getItems().values().iterator();
				while (it.hasNext()) {
					CartItem item = it.next();
					query = "INSERT INTO `order_items` (order_id, book_id, quantity, price, status)"
							+ " VALUES (?,?,?,?,?)";
					statement = connection.prepareStatement(query);
																		
					statement.setInt(1, resultSet.getInt(1));	
					statement.setInt(2, item.getBook().getId());		
					statement.setInt(3, item.getQuantity());			
					statement.setFloat(4, item.getBook().getPrice());	
					statement.setInt(5, order.getStatus());	
					
					statement.executeUpdate();	
				}				
				return true;
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}
		return false;
	}
}
