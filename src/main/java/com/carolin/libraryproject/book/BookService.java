package com.carolin.libraryproject.book;

import com.carolin.libraryproject.authors.Author;
import com.carolin.libraryproject.authors.AuthorRepository;
import com.carolin.libraryproject.book.bookDto.BookDto;
import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

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


    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return bookMapper.toDtoList(books);
    }


    public BookDto getBookByTitle(String title) {
        Book book = bookRepository.searchBookByTitleIgnoreCase(title);

        return bookMapper.toDto(book);
    }



    public Book addBook(Book book) throws NoAuthorFoundException {

        Long authorId = book.getAuthor().getId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NoAuthorFoundException("No author found with id: " + authorId));
        book.setAuthor(author);

        bookRepository.save(book);

        return book;
    }


    public List<BookDto> getBooksByAuthorLastName(String lastName) {
        List<Book> books = bookRepository.searchBookByAuthorByLastname(lastName);

        if (books.isEmpty()) {
            return null;
        }
        return bookMapper.toDtoList(books);
    }


}
