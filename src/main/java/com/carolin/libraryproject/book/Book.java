package com.carolin.libraryproject.book;

import com.carolin.libraryproject.authors.Author;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", columnDefinition = "INTEGER")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publication_year")
    private int publicationYear;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies = 1;  // Standardvärde

    @Column(name = "total_copies", nullable = false)
    private int totalCopies = 1;  // Standardvärde

    // FK relation till Author
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "author_id", nullable = true)
   // @JsonIgnore ??
    private Author author;

    // Tom konstruktor
    public Book() {}

    // Getters och setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getPublicationYear() { return publicationYear; }

    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public int getAvailableCopies() { return availableCopies; }

    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getTotalCopies() { return totalCopies; }

    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }
}


