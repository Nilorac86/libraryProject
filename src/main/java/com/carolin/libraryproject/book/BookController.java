package com.carolin.libraryproject.book;

import com.carolin.libraryproject.book.bookDto.BookDto;
import com.carolin.libraryproject.book.bookDto.BookRequestDto;
import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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


    // Sök bok efter titel med parameter
    @GetMapping("/search")
    public ResponseEntity <BookDto> getBooksByTitle(@RequestParam String title) {

        BookDto book = bookService.getBookByTitle(title);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    // Lägger till en ny bok
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> addBook(@Validated @RequestBody BookRequestDto bookRequestDto) {



        if (bookRequestDto == null) {
            return ResponseEntity.badRequest().build();
        }

        Book savedBook = null;
        try {
            savedBook = bookService.addBook(bookRequestDto);
        } catch (NoAuthorFoundException e) {
            throw e;
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



    @DeleteMapping
    public ResponseEntity<String> deleteBook(@RequestParam Long id) {
        bookService.deleteBookById(id);

        return ResponseEntity.noContent().build();
    }
}
