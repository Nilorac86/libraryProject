package com.carolin.libraryproject.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {



     Book searchBookByTitleIgnoreCase(String title);


     // NativeQuery med SQL, Sökfunktion på författarens efternamn
     @Query( value = "SELECT authors.author_id, authors.first_name, authors.last_name, " +
             "books.book_id, books.title, books.available_copies, books.publication_year, books.total_copies " +
             "FROM books " +
             "JOIN authors ON books.author_id = authors.author_id  " +
             "WHERE LOWER(authors.last_name)  = LOWER(:last_name)",

             nativeQuery = true )
     List<Book> searchBookByAuthorByLastname(@Param("last_name") String lastname);

     Optional<Book> findByTitleAndAuthorId(String title, Long authorId);
}
