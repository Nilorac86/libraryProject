package com.carolin.libraryproject.book;
import com.carolin.libraryproject.author.Author;
import com.carolin.libraryproject.author.authorDto.AuthorMapper;
import com.carolin.libraryproject.book.bookDto.BookDto;
import com.carolin.libraryproject.book.bookDto.BookRequestDto;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private AuthorMapper authorMapper;

    public BookMapper( AuthorMapper authorMapper) {

        this.authorMapper = authorMapper;
    }

    // Mapper på bok med författare
    public BookDto toDto(Book book) {

        if (book == null) {
            return null;
        }

        return new BookDto(
                book.getTitle(),
                book.getPublicationYear(),
                book.getAvailableCopies(),
                authorMapper.toDto(book.getAuthor()));

    }


    // Mapper på bok med författare. En lista med stream
    public List<BookDto> toDtoList(List<Book> books) {
        if (books == null) {
            return Collections.emptyList();
        }

        return books.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }

    public Book toEntity(BookRequestDto bookRequestDto) {

        if (bookRequestDto == null) {
            return null;

        }
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setPublicationYear(bookRequestDto.getPublicationYear());
        book.setTotalCopies(bookRequestDto.getTotalCopies());

        Author author = new Author();
        author.setId(bookRequestDto.getAuthorId());
        book.setAuthor(author);

        return book;
    }
}