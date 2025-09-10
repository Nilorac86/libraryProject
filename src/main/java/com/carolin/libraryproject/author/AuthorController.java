package com.carolin.libraryproject.author;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Hämtar en lista av alla författare
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    // Hämtar författare via efternamn i sökvägen
    @GetMapping("/lastname/{lastname}")
    public ResponseEntity<List<Author>> searchAuthorByLastname(@PathVariable String lastname) {
        List<Author> authors = authorService.findByLastname(lastname);

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    // Lägger till en författare
    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody Author author) {

        if(author == null) {
        return ResponseEntity.badRequest().build();
        }


        Author savedAuthor = authorService.addAuthor(author);
        URI location = URI.create("/authors/" + savedAuthor.getId());

        return ResponseEntity.created(location).body(savedAuthor);
    }

}
