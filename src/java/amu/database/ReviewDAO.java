package amu.database;

import amu.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewDAO {
    public List<Review> getReviewsByBook(Book book) {
        List<Review> reviews = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM review WHERE isbn13=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, book.getIsbn13());

            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "reviewsByBook SQL Query: " + query);

            if (resultSet.next()) {
                CustomerDAO customerDAO = new CustomerDAO();
                BookDAO bookDAO = new BookDAO();

                Review review;
                review = new Review();
                review.setId(resultSet.getInt("review.id"));
                review.setAuthor(customerDAO.findByEmail(resultSet.getString("review.author_email")));
                review.setBook(book);
                review.setScore(resultSet.getInt("review.score"));
                review.setVotes(new ArrayList<Votes>());

            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return reviews;
    }

    public List<Review> getReviewsByIsbn13(String isbn) {
        List<Review> reviews = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM review WHERE isbn13=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, isbn);

            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "reviewsByIsbn13 SQL Query: " + query);

            if (resultSet.next()) {
                CustomerDAO customerDAO = new CustomerDAO();
                BookDAO bookDAO = new BookDAO();

                Review review;
                review = new Review();
                review.setId(resultSet.getInt("review.id"));
                review.setAuthor(customerDAO.findByEmail(resultSet.getString("review.author_email")));
                review.setBook(bookDAO.findByISBN(resultSet.getString("review.isbn13")));
                review.setScore(resultSet.getInt("review.score"));
                review.setVotes(new ArrayList<Votes>());

            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);

        }

        return reviews;
    }
}
