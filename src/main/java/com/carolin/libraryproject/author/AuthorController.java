package com.carolin.libraryproject.author;

import com.carolin.libraryproject.author.authorDto.AuthorDto;
import com.carolin.libraryproject.author.authorDto.AuthorMapper;
import com.carolin.libraryproject.author.authorDto.AuthorRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
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
    @GetMapping("/lastname")
    public ResponseEntity<List<Author>> searchAuthorByLastname(@RequestParam String lastname) {
        List<Author> authors = authorService.findByLastname(lastname);

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }



    // Lägger till en författare
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@Validated @RequestBody AuthorRequestDto author) {

        if(author == null) {
        return ResponseEntity.badRequest().build();
        }

        Author savedAuthor = authorService.addAuthor(author);
        AuthorDto responseDto = authorMapper.toDto(savedAuthor);

        URI location = URI.create("/authors/" + savedAuthor.getId());

        return ResponseEntity.created(location).body(responseDto);
    }


    @DeleteMapping
    public ResponseEntity<String> deleteAuthor(@RequestParam Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();

    }
}
