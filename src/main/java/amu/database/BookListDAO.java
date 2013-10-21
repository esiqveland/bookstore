package amu.database;

import amu.model.Book;
import amu.model.BookList;
import amu.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

            CustomerDAO customerDAO = new CustomerDAO();
            if (resultSet.next()) {
                bookList = new BookList();
                bookList.setId(resultSet.getInt("id"));
                bookList.setDescription(resultSet.getString("description"));
                bookList.setTitle(resultSet.getString("title"));

                bookList.setAuthor(customerDAO.findById(resultSet.getInt("author")));

                bookList = populateBookListWithBooks(bookList);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return bookList;
    }

    public boolean save(BookList bookList) {
        if(bookList.getAuthor() == null) {
            return false;
        }

        if(bookList.getBooks().size() == 0) {
            return false;
        }

        if(createBookList(bookList)) {
            return true;
        }
        return false;
    }

    private boolean createBookList(BookList bookList) {
        if(bookList == null) {
            return false;
        }

        boolean result = true;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO booklist (`title`, `description`, `author`) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, bookList.getTitle());
            statement.setString(2, bookList.getDescription());
            statement.setInt(3, bookList.getAuthor().getId());

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int autoid = resultSet.getInt(1);
                bookList.setId(autoid);
                saveBookListBooks(bookList);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
            result = false;
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return result;
    }

    private boolean saveBookListBooks(BookList bookList) {
        if(bookList == null) {
            return false;
        }

        for(Book book : bookList.getBooks()) {
            saveBook(bookList, book);
        }
        return true;
    }

    private void saveBook(BookList bookList, Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO booklist_items (`booklist_id`, `book_id`) VALUES (?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, bookList.getId());
            statement.setInt(2, book.getId());

            statement.executeUpdate();

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
    }

    public List<BookList> getBookListsForCustomer(Customer customer) {
        List<BookList> bookLists = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM booklist WHERE author=?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                BookList bookList = new BookList();
                bookList.setId(resultSet.getInt("id"));
                bookList.setDescription(resultSet.getString("description"));
                bookList.setTitle(resultSet.getString("title"));

                bookList.setAuthor(customer);

                bookList = populateBookListWithBooks(bookList);
                bookLists.add(bookList);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return bookLists;
    }

    public void deleteBooklist(BookList bookList, Customer customer) {
        if(!canCustomerDeleteBooklist(bookList, customer)) {
            return;
        }
        deleteBooklistBooks(bookList);
        delete(bookList, customer);
    }

    private void delete(BookList bookList, Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getConnection();

            String query = "DELETE FROM booklist WHERE id=? AND author=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, bookList.getId());
            statement.setInt(2, customer.getId());

            statement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
    }

    private void deleteBooklistBooks(BookList bookList) {
        for(Book book : bookList.getBooks()) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                connection = Database.getConnection();

                String query = "DELETE FROM booklist_items WHERE booklist_id=? AND book_id=?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, bookList.getId());
                statement.setInt(2, book.getId());

                statement.executeUpdate();
            } catch (SQLException exception) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
            } finally {
                Database.close(connection, statement, resultSet);
            }
        }
    }

    private boolean canCustomerDeleteBooklist(BookList bookList, Customer customer) {
        return bookList.getAuthor().getId() == customer.getId();
    }
}
