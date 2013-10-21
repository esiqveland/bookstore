package amu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Customer;
import amu.model.Order;
import amu.model.OrderItems;

public class OrderItemsDAO {

	public List<OrderItems> browseByOrderId(int orderId) {
        List<OrderItems> orderItems = new ArrayList<OrderItems>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

		try {
			connection = Database.getConnection();
			String query = "SELECT * FROM `order_items` WHERE order_id=?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, orderId);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				orderItems.add(new OrderItems(resultSet.getInt("order_item_id"),
						resultSet.getInt("order_id"),
						resultSet.getInt("book_id"),
						resultSet.getInt("quantity"),
						resultSet.getFloat("price"),
						resultSet.getInt("status"))
				);
			}
		} catch (SQLException exception) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
		} finally {
			Database.close(connection, statement, resultSet);
		}

		return orderItems;
	}

}
