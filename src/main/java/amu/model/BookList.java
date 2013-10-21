package amu.model;

import java.util.ArrayList;
import java.util.List;

public class BookList {

    private int id;
    private String title;
    private String description;
    private List<Book> bookList;
    private Customer author;

    public void addBook(Book book) {
        if(bookList == null) {
            bookList = new ArrayList<Book>();
        }
        if(!bookList.contains(book)) {
            bookList.add(book);
        }
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

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
