package com.carolin.libraryproject.book;

import com.carolin.libraryproject.book.bookDto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {

        Page<BookDto> books = bookService.getAllBooks(pageable);

        if (books.getTotalElements() > 0) {
            return ResponseEntity.ok(books);
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/search")
    public ResponseEntity <BookDto> getBooksByTitle(@RequestParam String title) {

        BookDto book = bookService.getBookByTitle(title);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody Book book) {

        if (book == null) {
            return ResponseEntity.badRequest().build();
        }

        Book savedBook = bookService.addBook(book);
        URI location = URI.create("books/" + savedBook.getId());

        return ResponseEntity.created(location).body(savedBook);
    }

}
