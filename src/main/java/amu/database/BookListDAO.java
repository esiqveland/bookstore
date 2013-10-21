package amu.database;

import amu.model.Book;
import amu.model.BookList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookListDAO {

    private BookList populateBookListWithBooks(BookList bookList) {
        if(bookList == null) {
            return null;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM booklist_items WHERE booklist_id=?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, bookList.getId());

            resultSet = statement.executeQuery();

            BookDAO bookDAO = new BookDAO();
            while (resultSet.next()) {
                Book book = bookDAO.getBookById(resultSet.getInt("book_id"));
                bookList.addBook(book);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return bookList;
    }

    public BookList getBookListById(int booklistId) {
        BookList bookList = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM booklist WHERE id=?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, booklistId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                bookList = new BookList();
                bookList.setId(resultSet.getInt("id"));
                bookList.setDescription(resultSet.getString("description"));
                bookList.setTitle(resultSet.getString("title"));

                bookList = populateBookListWithBooks(bookList);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return bookList;
    }
}
