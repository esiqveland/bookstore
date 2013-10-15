package amu.database;

import amu.model.CartItem;
import amu.model.Customer;
import amu.model.Order;

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
			
			connection.close();
			
			/*
			 * Save order items
			 */
			
			if (resultSet.next()) {
				
				connection = Database.getConnection();
				
				Iterator<CartItem> it = order.getCart().getItems().values().iterator();
				while (it.hasNext()) {
					CartItem item = it.next();
					query = "INSERT INTO `order_items` (order_id, book_id, quantity, price, status)"
							+ " VALUES (?,?,?,?,?)";
					statement = connection.prepareStatement(query);
																		
					statement.setInt(1, resultSet.getInt(1));	//Order_id		
					statement.setInt(2, item.getBook().getId());		
					statement.setInt(3, item.getQuantity());			
					statement.setFloat(4, item.getBook().getPrice());	
					statement.setInt(5, order.getStatus());	
					
					statement.executeUpdate();
					
					
				}
				connection.close();
				
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
