package com.carolin.libraryproject.book;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBookByTitle(String title) {
        List<Book> books = bookRepository.searchBookByTitle(title);

        return books;
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }



}
