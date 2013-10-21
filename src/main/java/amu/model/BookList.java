package amu.model;

import java.util.ArrayList;
import java.util.List;

public class BookList {

    private int id;
    private String title;
    private String description;
    private List<Book> books;
    private Customer author;

    public void addBook(Book book) {
        if(books == null) {
            books = new ArrayList<Book>();
        }
        if(!books.contains(book)) {
            books.add(book);
        }
    }

    public BookList() {
        books = new ArrayList<>();
    }
    public Customer getAuthor() {
        return author;
    }

    public void setAuthor(Customer author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
