package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", columnDefinition = "INTEGER")
    private Long id;

    // Relationen till User (FK user_id)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relationen till Book (FK book_id)
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrowed_date", nullable = false)
    private LocalDateTime borrowedDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;


    // Lägger till datum och tid vid lån av bok samt sätter återlämningsdatum till + 14 dagar.
    @PrePersist
    protected void onCreate() {
        this.borrowedDate = LocalDateTime.now();

        if(this.dueDate == null) {
            this.dueDate = this.borrowedDate.plusDays(14);
        }
    }



    // Tom konstruktor
    public Loan() {
    }


    // Konstruktor
    public Loan(User user, Book book, LocalDateTime borrowedDate, LocalDateTime dueDate) {
        this.user = user;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
    }


    //Getters och setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
}





