package com.carolin.libraryproject.book;

import com.carolin.libraryproject.book.bookDto.BookDto;
import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Lista av alla böcker
    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        List<BookDto> books = bookService.getAllBooks();

        if(books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    // Lista av böcker genom pageable sortering
    @GetMapping("/page")
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {

        Page<BookDto> books = bookService.getAllBooks(pageable);

        if (books.getTotalElements() > 0) {
            return ResponseEntity.ok(books);
        }

        return ResponseEntity.notFound().build();
    }


    // Sök bok efter titel men parameter
    @GetMapping("/search")
    public ResponseEntity <BookDto> getBooksByTitle(@RequestParam String title) {

        BookDto book = bookService.getBookByTitle(title);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    // Lägger till en ny bok
    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody Book book) {

        if (book == null) {
            return ResponseEntity.badRequest().build();
        }

        Book savedBook = null;
        try {
            savedBook = bookService.addBook(book);
        } catch (NoAuthorFoundException e) {
            throw new RuntimeException(e);
        }

        URI location = URI.create("books/" + savedBook.getId());

        return ResponseEntity.created(location).body(savedBook);
    }


    // Hämtar en författares alla böcker genom parameter med författarens efternamn
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDto>> searchBookByAuthorLastName(@RequestParam String lastName) {

        List<BookDto> books = bookService.getBooksByAuthorLastName(lastName);

        if (lastName == null || lastName.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }


        return ResponseEntity.ok(books);
    }

}
