package amu.model;

import java.util.List;

public class Review {

    private int id;
    private Book book;
    private Customer author;
    private int score;
    List<Votes> votes;

    public Review(int id, Book book, Customer author, int score) {
        this.id=id;
        this.book=book;
        this.author=author;
        this.score=score;
    }

    public Review() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Customer getAuthor() {
        return author;
    }

    public void setAuthor(Customer author) {
        this.author = author;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Votes> getVotes() {
        return votes;
    }

    public void setVotes(List<Votes> votes) {
        this.votes = votes;
    }
}
