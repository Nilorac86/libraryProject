package com.carolin.libraryproject.book;

import com.carolin.libraryproject.book.bookDto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;




@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {

        Page<Book> booksPage = bookRepository.findAll(pageable);

        return booksPage.map(bookMapper::toDto);
    }

    public BookDto getBookByTitle(String title) {
        Book book = bookRepository.searchBookByTitleIgnoreCase(title);

        return bookMapper.toDto(book);
    }


    public Book addBook(Book book) {
        bookRepository.save(book);
        return book;
    }



}
