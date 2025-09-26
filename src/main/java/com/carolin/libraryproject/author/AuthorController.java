package com.carolin.libraryproject.author;

import com.carolin.libraryproject.author.authorDto.AuthorDto;
import com.carolin.libraryproject.author.authorDto.AuthorMapper;
import com.carolin.libraryproject.author.authorDto.AuthorRequestDto;
import com.carolin.libraryproject.utils.HtmlSanitizer;
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

    // Public
    // Hämtar en lista av alla författare
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }


    // Public
    // Hämtar författare via efternamn i params
    @GetMapping("/lastname")
    public ResponseEntity<List<Author>> searchAuthorByLastname(@RequestParam String lastname) {
        List<Author> authors = authorService.findByLastname(lastname);

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }



    // Lägger till en författare
    // Endast admin får lägga till författare
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@Validated @RequestBody AuthorRequestDto authorRequestDto) {

        if(authorRequestDto == null) {
        return ResponseEntity.badRequest().build();
        }

        // Skydd mot XSS attack
        authorRequestDto.setFirstName(HtmlSanitizer.cleanAll(authorRequestDto.getFirstName()));
        authorRequestDto.setLastName(HtmlSanitizer.cleanAll(authorRequestDto.getLastName()));
        authorRequestDto.setNationality(HtmlSanitizer.cleanAll(authorRequestDto.getNationality()));

        Author savedAuthor = authorService.addAuthor(authorRequestDto);
        AuthorDto responseDto = authorMapper.toDto(savedAuthor);

        URI location = URI.create("/authors/" + savedAuthor.getId());

        return ResponseEntity.created(location).body(responseDto);
    }



    // Tar bort författare genom id.
    // Endast admin får ta bort en författare
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<String> deleteAuthor(@RequestParam Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();

    }
}
