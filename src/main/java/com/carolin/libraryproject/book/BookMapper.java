package com.carolin.libraryproject.book;
import com.carolin.libraryproject.authors.authorDto.AuthorMapper;
import com.carolin.libraryproject.book.bookDto.BookDto;
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
}