package amu.database;

import amu.model.*;

import java.sql.*;
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

            String query = "SELECT * FROM review WHERE isbn13=? ORDER BY score DESC";
            statement = connection.prepareStatement(query);
            statement.setString(1, book.getIsbn13());

            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "reviewsByBook SQL Query: " + query);

            while (resultSet.next()) {
                CustomerDAO customerDAO = new CustomerDAO();
                BookDAO bookDAO = new BookDAO();

                Review review;
                review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setAuthor(customerDAO.findByEmail(resultSet.getString("author_email")));
                review.setBook(book);
                review.setScore(resultSet.getInt("score"));
                review.setContent(resultSet.getString("content"));
                review.setVotes(new ArrayList<Votes>());
                reviews.add(review);

            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return reviews;
    }

    public List<Review> getReviewsByIsbn13(String isbn) {
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(isbn);
        return getReviewsByBook(book);
    }

    public Review createReview(Review review, Customer author) {
        return createReview(review.getBook().getIsbn13(), author, review.getContent());
    }

    public Review createReview(String isbn13, Customer author, String content) {
        if(!bookExists(isbn13)) {
            return null;
        }
        Connection connection = null;
        PreparedStatement statement = null;
        int result = -1;
        try {
            connection = Database.getConnection();

            String query = "INSERT INTO `review` (`isbn13`, `author_email`, `content`, `score`) " +
                    "VALUES (?, ?, ?, 0)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, isbn13);
            statement.setString(2, author.getEmail());
            statement.setString(3, content);

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()){
                result=rs.getInt(1);
            }

            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "reviewsByBook SQL Query: " + query);
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }
        return getReviewById(result);
    }

    private boolean bookExists(String isbn13) {
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(isbn13);
        if(book != null) {
            return true;
        }
        return false;
    }

    public boolean voteForReview(int reviewId, Customer customer) {
        Review review = getReviewById(reviewId);
        return voteForReview(review, customer);
    }

    public boolean voteForReview(Review review, Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        boolean returnVal = true;

        try {
            connection = Database.getConnection();

            String query;
            int score = 1;
            if(haveCustomerVotedBefore(review, customer)) {
                score = -1;
                query = "DELETE FROM votes WHERE review_id = ? AND customer_id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, review.getId());
                statement.setInt(2, customer.getId());
            } else {
                query = "INSERT INTO votes (vote_value, review_id, customer_id) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setInt(1, score);
                statement.setInt(2, review.getId());
                statement.setInt(3, customer.getId());
            }

            statement.executeUpdate();

            preparedStatement = updateReviewScore(review, connection, preparedStatement, score);
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "voteForReview SQL Query: " + query);

        } catch (SQLException exception) {
            returnVal = false;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
            Database.close(connection, preparedStatement);
        }
        return returnVal;
    }

    private PreparedStatement updateReviewScore(Review review, Connection connection, PreparedStatement preparedStatement, int scoreDiff) throws SQLException {
        String scoreReviewQuery = "UPDATE review SET score=? WHERE id=?";
        preparedStatement = connection.prepareStatement(scoreReviewQuery);
        preparedStatement.setInt(1, review.getScore()+scoreDiff);
        preparedStatement.setInt(2, review.getId());

        preparedStatement.executeUpdate();
        review.setScore(review.getScore()+1);
        return preparedStatement;
    }

    private boolean haveCustomerVotedBefore(Review review, Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean returnVal = false;
        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM votes WHERE review_id=? AND customer_id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, review.getId());
            statement.setInt(2, customer.getId());

            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "haveCustomerVotedBefore SQL Query: " + query);

            if (resultSet.next()) {
                returnVal = true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return returnVal;
    }

    public Review getReviewById(int reviewid) {
        Review reviews = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM review WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, reviewid);


            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "reviewsById SQL Query: " + query);

            if (resultSet.next()) {
                CustomerDAO customerDAO = new CustomerDAO();
                BookDAO bookDAO = new BookDAO();

                Review review;
                review = new Review();
                review.setId(resultSet.getInt("review.id"));
                review.setAuthor(customerDAO.findByEmail(resultSet.getString("review.author_email")));
                review.setBook(bookDAO.findByISBN(resultSet.getString("review.isbn13")));
                review.setScore(resultSet.getInt("review.score"));
                review.setContent(resultSet.getString("review.content"));
                review.setVotes(new ArrayList<Votes>());
                reviews = review;

            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);

        }

        return reviews;
    }
}
