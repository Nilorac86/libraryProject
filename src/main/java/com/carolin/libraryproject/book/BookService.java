package com.carolin.libraryproject.book;

import com.carolin.libraryproject.authors.Author;
import com.carolin.libraryproject.authors.AuthorRepository;
import com.carolin.libraryproject.book.bookDto.BookDto;
import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

    private final BookRepository bookRepository;
    private final BookDto.BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookDto.BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    // Hämtar böcker baserat på sökning genom, size (antal svar), sort(ex title,asc,desc), page 0->.
    public Page<BookDto> getAllBooks(Pageable pageable) {

        Page<Book> booksPage = bookRepository.findAll(pageable);

        return booksPage.map(bookMapper::toDto);
    }

    // En lista på alla böcker
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return bookMapper.toDtoList(books);
    }

    // Hitta bok genom att söka på bokens titel
    public BookDto getBookByTitle(String title) throws EntityNotFoundException {
        Book book = bookRepository.searchBookByTitleIgnoreCase(title);
        if (book == null) {
            throw new EntityNotFoundException("Book: '"+ title + "' not found");
        }

        return bookMapper.toDto(book);
    }


    // Lägger till en bok
    public Book addBook(Book book) throws NoAuthorFoundException {

        Long authorId = book.getAuthor().getId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NoAuthorFoundException("No author found with id: " + authorId));
        book.setAuthor(author);

        Optional<Book> bookOptional = bookRepository.findByTitleAndAuthorId(book.getTitle(), authorId);
        if (bookOptional.isPresent()) {
            return bookOptional.get();
        }

        bookRepository.save(book);

        return book;
    }

    // Returnerar en lista med en författares böker genom sökning på författarens Efternamn
    public List<BookDto> getBooksByAuthorLastName(String lastName) throws NoAuthorFoundException {
        List<Book> books = bookRepository.searchBookByAuthorByLastname(lastName);

        if (books.isEmpty()) {
            throw new NoAuthorFoundException("No author with name: '" + lastName + "' found");
        }
        return bookMapper.toDtoList(books);
    }


}
